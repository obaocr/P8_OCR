package tourGuide.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tourGuide.Model.Attraction;
import tourGuide.Model.AttractionResponseDTO;
import tourGuide.Model.Location;
import tourGuide.Model.VisitedLocation;
import tourGuide.helper.InternalTestHelper;
import tourGuide.tracker.Tracker;
import tourGuide.user.User;
import tourGuide.user.UserReward;
import tourGuide.util.EntityNotFoundException;
import tourGuide.util.Utils;
import tripPricer.Provider;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * TourGuideService / Main SVC for the application
 */
@Service
public class TourGuideService {

    private Logger logger = LoggerFactory.getLogger(TourGuideService.class);
    private final RewardsService rewardsService;
    private final GpsProxyService gpsProxyService;
    private final TripPricerService trTripPricerService = new TripPricerServiceImpl();
    public final Tracker tracker;
    boolean testMode = true;
    private final Integer nbMaxAttractions=5;

    /**
     * TourGuideService constructor
     */
    public TourGuideService(GpsProxyService gpsProxyService, RewardsService rewardsService) {
        // Set Locale to "US" to fix the crash of the gpsUtil JAR ...
        Locale.setDefault(Locale.US);
        this.rewardsService = rewardsService;
        this.gpsProxyService = gpsProxyService;

        if (testMode) {
            logger.debug("TestMode enabled / Initializing users");
            initializeInternalUsers();
            logger.debug("Finished initializing users");
        }
        tracker = new Tracker(this);
        addShutDownHook();
    }

    public RewardsService getRewardsService() {
        return rewardsService;
    }

    public List<UserReward> getUserRewards(User user) {
        logger.debug("getUserLocation");
        return user.getUserRewards();
    }

    public VisitedLocation getUserLocation(User user) {
        logger.debug("getUserLocation");
        List<User> users = new ArrayList<>();
        users.add(user);
        VisitedLocation visitedLocation = (user.getVisitedLocations().size() > 0) ?
                user.getLastVisitedLocation() :
        trackUserLocationBulk(users).get(0);
        return visitedLocation;
    }

    /**
     * Method to get the last location for all users
     * @return al list of UserCurrentLocation
     */
    public Map<String, Location> getAllUsersCurrentLocation() {
        logger.info("getAllUsersCurrentLocation");
        Map<String, Location> mapUserLocation = new HashMap<>();
        for (User u : getAllUsers()) {
            if (u.getVisitedLocations().size() > 0) {
                mapUserLocation.put(u.getUserId().toString(), u.getLastVisitedLocation().location);
            }
        }
        return mapUserLocation;
    }

    public User getUser(String userName) {
        return internalUserMap.get(userName);
    }

    public User getUserCtrl(String userName) {
        User user = internalUserMap.get(userName);
        if (user == null) {
            throw new EntityNotFoundException("No user found for : " + userName);
        } else {
            return user;
        }
    }

    public List<User> getAllUsers() {
        return internalUserMap.values().stream().collect(Collectors.toList());
    }

    public void addUser(User user) {
        //logger.info("addUser");
        if (!internalUserMap.containsKey(user.getUserName())) {
            internalUserMap.put(user.getUserName(), user);
        }
    }

    public List<Provider> getTripDeals(User user) {
        logger.info("getTripDeals");
        int cumulatativeRewardPoints = user.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();
        List<Provider> providers = trTripPricerService.getPrice(tripPricerApiKey, user.getUserId(), user.getUserPreferences().getNumberOfAdults(),
                user.getUserPreferences().getNumberOfChildren(), user.getUserPreferences().getTripDuration(), cumulatativeRewardPoints);
        user.setTripDeals(providers);
        return providers;
    }

    /**************************************************************************************
    // *********** trackUserLocationForAllUsers Async *************************************
    // ************************************************************************************/

    private final ExecutorService executorTrackUserLocation = Executors.newFixedThreadPool(40);

    private CompletableFuture<VisitedLocation> getTrackUserLocationAsync(User user) {
        return CompletableFuture.supplyAsync(() -> {
            VisitedLocation visitedLocation = gpsProxyService.gpsUserLocation(user.getUserId());
            user.addToVisitedLocations(visitedLocation);
            return visitedLocation;
        }, executorTrackUserLocation).exceptionally(e -> {
            logger.error("getTrackUserLocationAsync for user :" + user.getUserName() + e.toString());
            return null;
        });
    }


    public List<VisitedLocation> trackUserLocation(User user) {
        List<User> users = new ArrayList<>();
        users.add(user);
        return this.trackUserLocationBulk(users);
    }

    public List<VisitedLocation> trackUserLocationBulk(List<User> users){
        logger.info("trackUserLocationForAllUsers");
        Date d1 = new Date();

        List<VisitedLocation> visitedLocations = new ArrayList<>();

        List<CompletableFuture<VisitedLocation>> trackUserLocationFuture = users.stream()
                .map(u -> getTrackUserLocationAsync(u))
                .collect(Collectors.toList());

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(trackUserLocationFuture
                .toArray(new CompletableFuture[trackUserLocationFuture.size()]));

        CompletableFuture<List<VisitedLocation>> allCompletableFuture = allFutures.thenApply(
                future -> {
                    return trackUserLocationFuture.stream()
                            .map(a -> a.join()).collect(Collectors.toList());
                }
        );
        visitedLocations = allCompletableFuture.join();

        Date d2 = new Date();
        logger.debug("trackUserLocationBulk Part1 OK, Size / Time ms  :" + visitedLocations.size() + " / " + (d2.getTime() - d1.getTime()));

        // Process "CalculateReward" Bulk
        Integer nbCalcRewardProcess =  rewardsService.calculateRewardsForUsers(users);
        Date d3 = new Date();
        logger.debug("calculateRewardsForUsers Part2 OK, Size / Time ms  :" + nbCalcRewardProcess + " / " + (d3.getTime() - d2.getTime()));
        logger.debug("trackUserLocationBulk / Time ms  :" + (d3.getTime() - d1.getTime()));

        return visitedLocations;
    }

    /*************************************************************************************
    // *********** getNearByAttractions parallel mode ************************************
    // *********** call Gps en micro SVC              ************************************
    // ***********************************************************************************/

     /**
     * getNearByAttractionsAsyncMgt : calcul en parallel avec Completable Future
     * @param userName
     * @return
     */
    public List<AttractionResponseDTO> getNearByAttractions(String userName) {
        logger.info("getNearByAttractions");
        List<AttractionResponseDTO> attractionResponses = new ArrayList<>();
        logger.debug("getAllUsers.size:" + getAllUsers().size());

        // Appel GpsApi micro service
        VisitedLocation visitedLocation = gpsProxyService.gpsUserLocation(getUser(userName).getUserId());

        // Appel GpsApi micro service
        List<Attraction> attractions = gpsProxyService.gpsAttractions();
        logger.info("Appel GpsApi micro service attractionMappers , nb attractions:"+attractions.size());

        for (Attraction attraction : attractions) {
            Location loc1 = new Location(attraction.longitude, attraction.latitude);
            Location loc2 = new Location(visitedLocation.location.longitude, visitedLocation.location.latitude);
            Double distance = Utils.calculateDistance(loc1, loc2);
            AttractionResponseDTO attractionResponse = new AttractionResponseDTO();
            attractionResponse.setAttractionName(attraction.attractionName);
            attractionResponse.setAttractionId(attraction.attractionId.toString());
            attractionResponse.setCity(attraction.city);
            attractionResponse.setState(attraction.state);
            attractionResponse.setLatitude(attraction.latitude);
            attractionResponse.setLongitude(attraction.longitude);
            attractionResponse.setDistanceWithCurrLoc(distance);
            attractionResponse.setRewardsPoints(0);
            attractionResponses.add(attractionResponse);
        }

        logger.debug("attractionResponses size : " + attractionResponses.size());
        logger.debug("nbMaxAttractions : " + nbMaxAttractions);
        // Sort the list by Distance and keep 5 first items
        // cf. https://bezkoder.com/java-sort-arraylist-of-objects/
        attractionResponses = (ArrayList<AttractionResponseDTO>) attractionResponses
                .stream().sorted(Comparator.comparing(AttractionResponseDTO::getDistanceWithCurrLoc)).limit(nbMaxAttractions)
                .collect(Collectors.toList());

        logger.debug("attractionResponses size apres sort: " + attractionResponses.size());
        // Appels en // pour calcul de Rewards car peut mettre du temps unitairement
        Date d1 = new Date();

        List<AttractionResponseDTO> attractionResponsesRewards = new ArrayList<>();
        attractionResponsesRewards = rewardsService.getAllAttractionResponseWithRewardPoint(attractionResponses, getUser(userName));

        Date d2 = new Date();
        logger.debug("temps d'appel rewards complatable future en ms : " + (d2.getTime() - d1.getTime()));

        // Tri car le paraléllisme ne rend pas dans l'ordre
        List<AttractionResponseDTO> attractionResponsesResult = (ArrayList<AttractionResponseDTO>) attractionResponsesRewards
                .stream().sorted(Comparator.comparing(AttractionResponseDTO::getDistanceWithCurrLoc)).collect(Collectors.toList());

        return attractionResponsesResult;
    }

    private void addShutDownHook() {
        logger.info("addShutDownHook");
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                tracker.stopTracking();
            }
        });
    }

    /**********************************************************************************
     *
     * Methods Below: For Internal Testing
     *
     **********************************************************************************/
    private static final String tripPricerApiKey = "test-server-api-key";
    // Database connection will be used for external users, but for testing purposes internal users are provided and stored in memory
    private final Map<String, User> internalUserMap = new HashMap<>();

    private void initializeInternalUsers() {
        IntStream.range(0, InternalTestHelper.getInternalUserNumber()).forEach(i -> {
            String userName = "internalUser" + i;
            String phone = "000";
            String email = userName + "@tourGuide.com";
            User user = new User(UUID.randomUUID(), userName, phone, email);
            generateUserLocationHistory(user);
            //logger.debug("User : " + user.getUserId() + ' ' + user.getUserName());
            internalUserMap.put(userName, user);
        });
        logger.debug("Created " + InternalTestHelper.getInternalUserNumber() + " internal test users.");
    }

    private void generateUserLocationHistory(User user) {
        IntStream.range(0, 3).forEach(i -> {
            user.addToVisitedLocations(new VisitedLocation(user.getUserId(), new Location(generateRandomLatitude(), generateRandomLongitude()), getRandomTime()));
        });
    }

    private double generateRandomLongitude() {
        double leftLimit = -180;
        double rightLimit = 180;
        return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
    }

    private double generateRandomLatitude() {
        double leftLimit = -85.05112878;
        double rightLimit = 85.05112878;
        return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
    }

    private Date getRandomTime() {
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
        return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
    }

}
