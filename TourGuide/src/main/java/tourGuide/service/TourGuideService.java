package tourGuide.service;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tourGuide.Model.AttractionResponseDTO;
import tourGuide.Model.UserPreferencesDTO;
import tourGuide.helper.InternalTestHelper;
import tourGuide.tracker.Tracker;
import tourGuide.user.User;
import tourGuide.user.UserPreferences;
import tourGuide.user.UserReward;
import tourGuide.util.Utils;
import tripPricer.Provider;

import javax.money.Monetary;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class TourGuideService {

    private Logger logger = LoggerFactory.getLogger(TourGuideService.class);
    private final GpsService gpsService;
    private final RewardsService rewardsService;
    private final TripPricerService trTripPricerService = new TripPricerServiceImpl();
    public final Tracker tracker;
    boolean testMode = true;
    private final Integer nbMaxAttractions=5;

    /**
     * TourGuideService constructor
     *
     * @param gpsService
     * @param rewardsService
     */
    // TODO => constructeur TourGuideService apparemment ne sert que pour les test !!! gpsUtil etant instanciée car bean dans TourGuideModule ? ) voir
    // TODO => A voir comme possibilité de ne pas lancer le tracker pour certains tests... / à voir  ? sauf que certains tests en ont besoin
    public TourGuideService(GpsService gpsService, RewardsService rewardsService) {
        // Set Locale to "US" to fix the crash of the gpsUtil JAR ...
        Locale.setDefault(Locale.US);
        this.gpsService = gpsService;
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
        logger.debug("getUserLocation");
        return user.getUserRewards();
    }

    // TODO NB : trackUserLocation peut être, sera amélioré dans le chantier global du projet
    // TODO : pas de pb de performance, on peut laisser en l'état
    // TODO ==> je remplace trackUserLocation par trackUserLocationBulk
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

    public List<User> getAllUsers() {
        return internalUserMap.values().stream().collect(Collectors.toList());
    }

    public void addUser(User user) {
        logger.info("addUser");
        if (!internalUserMap.containsKey(user.getUserName())) {
            internalUserMap.put(user.getUserName(), user);
        }
    }

    // TODO Put pour preferences
    //  Url avec /Id et en body un JSON d'un user pref .. ex UserPreferencesDTO ...
    //      Sinon autre méthode avec class "StdSerializer", @JsonSerialize(using = CurrencyUnitSerializer.class), @NotNull, private CurrencyUnit currency;
    // TODO : pas de pb de performance, on peut laisser en l'état
    public List<Provider> getTripDeals(User user) {
        logger.info("getTripDeals");
        int cumulatativeRewardPoints = user.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();
        List<Provider> providers = trTripPricerService.getPrice(tripPricerApiKey, user.getUserId(), user.getUserPreferences().getNumberOfAdults(),
                user.getUserPreferences().getNumberOfChildren(), user.getUserPreferences().getTripDuration(), cumulatativeRewardPoints);
        user.setTripDeals(providers);
        return providers;
    }

    // TODO perfs  à voir ...
    // TODO 27/08/2020 ...  à voir gpsUtil.getUserLocation, asynchrone
    // TODO appel en HTTP de GPS, 1/ regarder par curiosité en Spring en restTemplate mais deprecated...,
    //  2/ Regarder FeignClient, annotation pour dire que le projet est client http
    // C'est appelé actuellment en séquentiel pour chaque utilisateur
    // A faire : l'appelant (tracker) doit faire apppel en asynchrone, et l'appelant doit être capable de savoir que les n appels sont faits pour se remettre
    // en wait ..

    // TODO voir pour une méthode externe  du code intérieur pour ne pas avoir de code dupliqué ...
    // TODO Remplaé par trackUserLocationBulk
    /*public VisitedLocation trackUserLocation(User user) {
        logger.info("trackUserLocation");
        VisitedLocation visitedLocation = gpsService.getUserLocation(user.getUserId());
        user.addToVisitedLocations(visitedLocation);
        rewardsService.calculateRewards(user);
        return visitedLocation;
    }*/

    /**************************************************************************************
    // *********** trackUserLocationForAllUsers Async *************************************
    // ************************************************************************************/

    // TODO Gérer les exceptions
    // TODO 100 Thread max sinon ne sert plus  à rien...

    private ExecutorService executorTrackUserLocation = Executors.newFixedThreadPool(200);

    // TODO ... trackUserLocation pour un User en asycnhrone
    private CompletableFuture<VisitedLocation> getTrackUserLocationAsync(User user) {
        //logger.info("trackUserLocationAsync for user : " + user.getUserName());
        return CompletableFuture.supplyAsync(() -> {
            VisitedLocation visitedLocation = gpsService.getUserLocation(user.getUserId());
            user.addToVisitedLocations(visitedLocation);
            rewardsService.calculateRewards(user);
            return visitedLocation;
        }, executorTrackUserLocation);
    }


    public List<VisitedLocation> trackUserLocation(User user) {
        List<User> users = new ArrayList<>();
        users.add(user);
        return this.trackUserLocationBulk(users);
    }

    public List<VisitedLocation> trackUserLocationBulk(List<User> users){
        logger.info("trackUserLocationForAllUsers");
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
        logger.debug("attractionResponsesWithRewardPoint size :" + visitedLocations.size());
        // TODO Finalement l'appelant n'a pas besoin des VisitedLocation
        return visitedLocations;
    }

    // *********************************************************

    /**
     * getNearByAttractionsAsyncMgt : calcul en parallel avec Completable Future
     * @param userName
     * @return
     */
    public List<AttractionResponseDTO> getNearByAttractions(String userName) {
        logger.info("getNearByAttractions");
        List<AttractionResponseDTO> attractionResponses = new ArrayList<>();
        logger.debug("getAllUsers.size:" + getAllUsers().size());
        VisitedLocation visitedLocation = getUserLocation(getUser(userName));
        // Première étape pour ne retenir que les 5 premier items ...
        for (Attraction attraction : gpsService.getAttractions()) {
            Double distance = Utils.calculateDistance(attraction, visitedLocation.location);
            AttractionResponseDTO attractionResponse = new AttractionResponseDTO();
            attractionResponse.setAttractionName(attraction.attractionName);
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
            logger.debug("User : " + user.getUserId() + ' ' + user.getUserName());
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
