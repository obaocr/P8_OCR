package tourGuide.service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tourGuide.Model.AttractionResponse;
import tourGuide.Model.UserPrefResponse;
import tourGuide.helper.InternalTestHelper;
import tourGuide.tracker.Tracker;
import tourGuide.user.User;
import tourGuide.user.UserPreferences;
import tourGuide.user.UserReward;
import tripPricer.Provider;
import tripPricer.TripPricer;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class TourGuideService {
    private Logger logger = LoggerFactory.getLogger(TourGuideService.class);
    private final GpsUtil gpsUtil;
    private final RewardsService rewardsService;
    private final TripPricer tripPricer = new TripPricer();
    public final Tracker tracker;
    boolean testMode = true;

    /**
     * TourGuideService constructor
     *
     * @param gpsUtil
     * @param rewardsService
     */
    public TourGuideService(GpsUtil gpsUtil, RewardsService rewardsService) {
        // Set Locale to "US" to fix the crash of the gpsUtil JAR ...
        Locale.setDefault(Locale.US);
        this.gpsUtil = gpsUtil;
        this.rewardsService = rewardsService;

        if (testMode) {
            logger.info("TestMode enabled");
            logger.debug("Initializing users");
            initializeInternalUsers();
            logger.debug("Finished initializing users");
        }
        tracker = new Tracker(this);
        addShutDownHook();
    }

    public List<UserReward> getUserRewards(User user) {
        return user.getUserRewards();
    }

    public VisitedLocation getUserLocation(User user) {
        VisitedLocation visitedLocation = (user.getVisitedLocations().size() > 0) ?
                user.getLastVisitedLocation() :
                trackUserLocation(user);
        return visitedLocation;
    }

    // TODO OBA (Finished, to be validates)  - getAllUsersCurrentLocation

    /**
     * Method to get the last location for all users
     *
     * @return al list of UserCurrentLocation
     */
    public Map<String, Location> getAllUsersCurrentLocation() {
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

    public List<User> getAllUsers() {
        return internalUserMap.values().stream().collect(Collectors.toList());
    }

    public void addUser(User user) {
        if (!internalUserMap.containsKey(user.getUserName())) {
            internalUserMap.put(user.getUserName(), user);
        }
    }

    public UserPreferences getUserPreferences(String userName) {
        User user = this.getUser(userName);
        if(user != null) {
            return user.getUserPreferences();
        } else {
            return null;
        }
    }

    // TODO Put pour preferences
    //  Url avec /Id et en body un JSON d'un user pref .. ex UserPreferencesDTO ...
    //      Sinon autre méthode avec class "StdSerializer", @JsonSerialize(using = CurrencyUnitSerializer.class), @NotNull, private CurrencyUnit currency;

    public List<Provider> getTripDeals(User user) {
        int cumulatativeRewardPoints = user.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();
        List<Provider> providers = tripPricer.getPrice(tripPricerApiKey, user.getUserId(), user.getUserPreferences().getNumberOfAdults(),
                user.getUserPreferences().getNumberOfChildren(), user.getUserPreferences().getTripDuration(), cumulatativeRewardPoints);
        user.setTripDeals(providers);
        return providers;
    }

    // TODO  à voir gpsUtil.getUserLocation, asynchrone
    public VisitedLocation trackUserLocation(User user) {
        VisitedLocation visitedLocation = gpsUtil.getUserLocation(user.getUserId());
        user.addToVisitedLocations(visitedLocation);
        rewardsService.calculateRewards(user);
        return visitedLocation;
    }

    // TODO Calculer les rewards
    // TODO faire classe de test
    // TODO ne sort pas dans l'ordre les champs
    // TODO voir pour setRewardsPoints, quel calcul de points ...
    //  TODO A voir pour RewardCentral.getAttractionRewardPoints(UUID attractionId, UUID userId) => pourquoi y a t-il un sleep ?????
        // TODO pour simuler temps de réponse externe ? dans ce cas pour les 5 destinations faire en asychrone les 5 appels en même temps ?
        // TODO => faire les 5 appels à Rewards en //
    public List<AttractionResponse> getNearByAttractions(String userName) {
        List<AttractionResponse> attractionResponses = new ArrayList<>();
        VisitedLocation visitedLocation = getUserLocation(getUser(userName));
        for (Attraction attraction : gpsUtil.getAttractions()) {
            Double distance = rewardsService.getDistance(attraction, visitedLocation.location);
            AttractionResponse attractionResponse = new AttractionResponse();
            attractionResponse.setAttractionName(attraction.attractionName);
            attractionResponse.setCity(attraction.city);
            attractionResponse.setState(attraction.state);
            attractionResponse.setLatitude(attraction.latitude);
            attractionResponse.setLongitude(attraction.longitude);
            attractionResponse.setDistanceWithCurrLoc(distance);
            attractionResponse.setRewardsPoints(0); // TODO utiliser RewardCentral.getAttractionRewardPoints , appels en parallele asynchrone ?
            attractionResponses.add(attractionResponse);
        }
        // Sort the list by Distance and keep 5 first items
        // cf. https://bezkoder.com/java-sort-arraylist-of-objects/
        ArrayList<AttractionResponse> sortedAttractionResponses = (ArrayList<AttractionResponse>) attractionResponses
                .stream().sorted(Comparator.comparing(AttractionResponse::getDistanceWithCurrLoc)).limit(5)
                .collect(Collectors.toList());

        // Appels pour calculer les Rewards en //

        return sortedAttractionResponses;
    }

    private void addShutDownHook() {
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
