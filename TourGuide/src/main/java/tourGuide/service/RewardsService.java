package tourGuide.service;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rewardCentral.RewardCentral;
import tourGuide.Model.AttractionResponseDTO;
import tourGuide.user.User;
import tourGuide.user.UserReward;
import tourGuide.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

// TODO  à passer en SVC mais enlever du bean ???
public class RewardsService {
    private Logger logger = LoggerFactory.getLogger(RewardsService.class);
    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

    // proximity in miles
    private int defaultProximityBuffer = 10;
    private int proximityBuffer = defaultProximityBuffer;
    private int attractionProximityRange = 200;
    private final GpsService gpsService;
    private final RewardCentral rewardsCentral;

    public RewardsService(GpsService gpsService, RewardCentral rewardCentral) {
        this.gpsService = gpsService;
        this.rewardsCentral = rewardCentral;
    }

    public void setProximityBuffer(int proximityBuffer) {
        this.proximityBuffer = proximityBuffer;
    }

    public void setDefaultProximityBuffer() {
        proximityBuffer = defaultProximityBuffer;
    }

    // TODO l'appel  à getRewardPoints ( est long apparemment... en fait rewardsCentral.getAttractionRewardPoints), à voir si on laisse
    public void calculateRewards(User user) {
        //logger.debug("calculateRewards pour user : " + user.getUserName());
        List<VisitedLocation> userLocations = user.getVisitedLocations();
        List<Attraction> attractions = gpsService.getAttractions();
        for (VisitedLocation visitedLocation : userLocations) {
            for (Attraction attraction : attractions) {
                if (user.getUserRewards().stream().filter(r -> r.attraction.attractionName.equals(attraction.attractionName)).count() == 0) {
                    if (nearAttraction(visitedLocation, attraction)) {
                        logger.debug("calculateRewards => ******************* passage nearAttraction : " + user.getUserName());
                        user.addUserReward(new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user)));
                    }
                }
            }
        }
    }

    public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
        return Utils.calculateDistance(attraction, location) > attractionProximityRange ? false : true;
    }

    // TODO proximityBuffer à 10 et distance toujours > 3000 donc pas de rewards ...
    private boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
        double distance = Utils.calculateDistance(attraction, visitedLocation.location);
        //logger.debug("nearAttraction, distance, proximityBuffer :" +  distance + " / " + proximityBuffer);
        return distance > proximityBuffer ? false : true;
    }

    // TODO  getAttractionRewardPoints est longue en temps de réponse : sleep...
    private int getRewardPoints(Attraction attraction, User user) {
        return rewardsCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
    }

    /**
     * OBA Pour calcul du reward Point en future asynchrone, pour une attraction
     *
     * @param attractionResponse
     * @param user
     * @return
     */
    private ExecutorService executor = Executors.newFixedThreadPool(5);

    private CompletableFuture<AttractionResponseDTO> getAttractionResponseWithRewardPoint(AttractionResponseDTO attractionResponse, User user) {
        return CompletableFuture.supplyAsync(() -> {
            int reward = rewardsCentral.getAttractionRewardPoints(attractionResponse.getAttractionId(), user.getUserId());
            attractionResponse.setRewardsPoints(reward);
            return attractionResponse;
        }, executor);
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