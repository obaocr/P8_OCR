package tourGuide.service;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import tourGuide.Model.*;
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
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * RewardsService
 */

public class RewardsService {
    private Logger logger = LoggerFactory.getLogger(RewardsService.class);
    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;
    private Integer nbAddReward = 0;

    @Autowired
    private RewardProxy rewardProxy;

    @Autowired
    private GpsProxyService gpsProxyService;

    // proximity in miles
    private int defaultProximityBuffer = 10;
    private int proximityBuffer = defaultProximityBuffer;
    private int attractionProximityRange = 200;

    public RewardsService() {
    }

    public void setProximityBuffer(int proximityBuffer) {
        this.proximityBuffer = proximityBuffer;
    }

    public void setDefaultProximityBuffer() {
        proximityBuffer = defaultProximityBuffer;
    }

    public void calculateRewards(User user, List<Attraction> attractions) {
        List<VisitedLocation> userLocations = user.getVisitedLocations();
        userLocations.stream().forEach( visitedLocation -> {
            attractions.stream().forEach(attraction -> {
                if (user.getUserRewards().stream().filter(r -> r.attraction.attractionName.equals(attraction.attractionName)).count() == 0
                        && nearAttraction(visitedLocation, attraction)){
                    int reward = getRewardPoints(attraction.attractionId, user.getUserId());
                    user.addUserReward(new UserReward(visitedLocation, attraction, reward));
                    this.nbAddReward++;
                }
            });
        });

        /*
        for (VisitedLocation visitedLocation : userLocations) {
            for (Attraction attraction : attractions) {
                if (user.getUserRewards().stream().filter(r -> r.attraction.attractionName.equals(attraction.attractionName)).count() == 0
                    && nearAttraction(visitedLocation, attraction)){
                    int reward = getRewardPoints(attraction.attractionId, user.getUserId());
                    user.addUserReward(new UserReward(visitedLocation, attraction, reward));
                    this.nbAddReward++;
                }
            }
        }

         */

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

    private CompletableFuture<Boolean> calculateRewardsAsync(User user, List<Attraction> attractions) {
        return CompletableFuture.supplyAsync(() -> {
            calculateRewards(user, attractions);
            return true;
        }, executorCalcReward).exceptionally(e -> {
            logger.error("calculateRewardsAsync for user :" + user.getUserName() + e.toString());
            return false;
        });
    }

    public Integer calculateRewardsForUsers(List<User> users) {
        logger.debug("calculateRewardsForUsers size: " + users.size() + " users");
        this.nbAddReward = 0;
        final List<Attraction> attractions = getGpsAttractions();
        List<Boolean> results = new ArrayList<>();

        List<CompletableFuture<Boolean>> calculateRewardsFuture = users.stream()
                .map(u -> calculateRewardsAsync(u, attractions))
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
        logger.debug("nb appels CalculateReward OK:" + nbResultOK);
        return nbResultOK;
    }

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
        }, executorReward).exceptionally(e -> {
            logger.error("getAttractionResponseWithRewardPoint for user :" + user.getUserName() + e.toString());
            return null;
        });
    }

    /**
     * Calculate Rewards points in parallel mode with future for an attraction list
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