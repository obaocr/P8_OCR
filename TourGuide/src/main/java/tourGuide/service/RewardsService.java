package tourGuide.service;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourGuide.Model.AttractionMapper;
import tourGuide.Model.AttractionResponseDTO;
import tourGuide.Model.RewardPointsMapper;
import tourGuide.Proxies.GpsProxy;
import tourGuide.Proxies.RewardProxy;
import tourGuide.user.User;
import tourGuide.user.UserReward;
import tourGuide.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class RewardsService {
    private Logger logger = LoggerFactory.getLogger(RewardsService.class);
    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

    @Autowired
    private RewardProxy rewardProxy;

    @Autowired
    private GpsProxyService gpsProxyService;

    // proximity in miles
    private int defaultProximityBuffer = 10;
    private int proximityBuffer = defaultProximityBuffer;
    private int attractionProximityRange = 200;

    //private GpsProxyService gpsProxyService;

    public RewardsService() {
    }

    /*public RewardsService(GpsProxyService gpsProxyService) {
        this.gpsProxyService = gpsProxyService;
    }

     */

    public void setProximityBuffer(int proximityBuffer) {
        this.proximityBuffer = proximityBuffer;
    }

    public void setDefaultProximityBuffer() {
        proximityBuffer = defaultProximityBuffer;
    }

    public void calculateRewards(User user) {
        List<VisitedLocation> userLocations = user.getVisitedLocations();
        List<Attraction> attractions = getGpsAttractions();
        for (VisitedLocation visitedLocation : userLocations) {
            for (Attraction attraction : attractions) {
                if (user.getUserRewards().stream().filter(r -> r.attraction.attractionName.equals(attraction.attractionName)).count() == 0) {
                    if (nearAttraction(visitedLocation, attraction)) {
                        logger.debug("calculateRewards => ******************* passage nearAttraction : " + user.getUserName());
                        int reward = getRewardPoints(attraction.attractionId, user.getUserId());
                        user.addUserReward(new UserReward(visitedLocation, attraction, reward));
                    }
                }
            }
        }
    }

    public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
        return Utils.calculateDistance(attraction, location) > attractionProximityRange ? false : true;
    }

    private boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
        double distance = Utils.calculateDistance(attraction, visitedLocation.location);
        return distance > proximityBuffer ? false : true;
    }

    // TODO apparemment fait planter si on utiliser dans les tests de perfs !!!!!
    private Integer getRewardPoints(UUID attractionId, UUID userId) {
        // Appel micro service
        RewardPointsMapper rewardPointsMapper = rewardProxy.getAttractionRewardPoints(attractionId.toString(), userId.toString());
        return rewardPointsMapper.getPoints();
    }

    private List<Attraction> getGpsAttractions() {
        List<Attraction> attractions = gpsProxyService.gpsAttractions();
        return attractions;
    }

    /*************************************************************************************
     // *********** Calculate Rewards for N users parallel mode   *************************
     // ***********************************************************************************/

    private final ExecutorService executorCalcReward = Executors.newFixedThreadPool(100);

    private CompletableFuture<Boolean> calculateRewardsAsync(User user) {
        return CompletableFuture.supplyAsync(() -> {
            calculateRewards(user);
            return true;
        }, executorCalcReward);
    }

    public Integer calculateRewardsForUsers(List<User> users) {
        logger.debug("calculateRewardsForUsers size: " + users.size());
        List<Boolean> results = new ArrayList<>();

        List<CompletableFuture<Boolean>> calculateRewardsFuture = users.stream()
                .map(u -> calculateRewardsAsync(u))
                .collect(Collectors.toList());
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(calculateRewardsFuture
                .toArray(new CompletableFuture[calculateRewardsFuture.size()]));
        CompletableFuture<List<Boolean>> allCompletableFuture = allFutures.thenApply(
                future -> {
                    return calculateRewardsFuture.stream()
                            .map(a -> a.join()).collect(Collectors.toList());
                }
        );
        results = allCompletableFuture.join();
        Integer nbResultOK = 0;
        for (Boolean b : results) {
            if (b == true) {
                nbResultOK++;
            }
        }
        logger.debug("calculateRewardsForUsers nbResultOK:" + nbResultOK);
        return nbResultOK;
    }

    /**************************************************************************************
     // *********** Pour calcul du reward Point en future  Async **************************
     // ***********************************************************************************/

    /**
     * OBA Pour calcul du reward Point en future asynchrone, pour une attraction
     *
     * @param attractionResponse
     * @param user
     * @return
     */
    private final ExecutorService executorReward = Executors.newFixedThreadPool(5);

    private CompletableFuture<AttractionResponseDTO> getAttractionResponseWithRewardPoint(AttractionResponseDTO attractionResponse, User user) {
        return CompletableFuture.supplyAsync(() -> {
            int reward = getRewardPoints(UUID.fromString(attractionResponse.getAttractionId()), user.getUserId());
            attractionResponse.setRewardsPoints(reward);
            return attractionResponse;
        }, executorReward);
    }

    /**
     * OBA Pour calcul en parallel et en future des Rewards points pour une list attractions
     *
     * @param attractionResponses
     * @param user
     * @return List<AttractionResponse> with rewards points
     */
    // TODO gérer les exceptions, cf. docs
    public List<AttractionResponseDTO> getAllAttractionResponseWithRewardPoint(List<AttractionResponseDTO> attractionResponses, User user) {
        logger.debug("attractionResponses size" + attractionResponses.size());
        List<AttractionResponseDTO> attractionResponsesWithRewardPoint = new ArrayList<>();

        List<CompletableFuture<AttractionResponseDTO>> attractionsRespWithRewardPointFuture = attractionResponses.stream()
                .map(a -> getAttractionResponseWithRewardPoint(a, user))
                .collect(Collectors.toList());

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(attractionsRespWithRewardPointFuture
                .toArray(new CompletableFuture[attractionsRespWithRewardPointFuture.size()]));

        CompletableFuture<List<AttractionResponseDTO>> allCompletableFuture = allFutures.thenApply(
                future -> {
                    return attractionsRespWithRewardPointFuture.stream()
                            .map(a -> a.join()).collect(Collectors.toList());
                }
        );
        attractionResponsesWithRewardPoint = allCompletableFuture.join();
        logger.debug("attractionResponsesWithRewardPoint size" + attractionResponsesWithRewardPoint.size());
        return attractionResponsesWithRewardPoint;
    }
}