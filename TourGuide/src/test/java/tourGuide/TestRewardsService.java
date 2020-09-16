package tourGuide;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.junit.Test;
import rewardCentral.RewardCentral;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.GpsService;
import tourGuide.service.GpsServiceImpl;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;
import tourGuide.user.UserReward;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestRewardsService {

    @Test
    public void userGetRewards() {
        GpsService gpsService = new GpsServiceImpl(new GpsUtil());
        RewardsService rewardsService = new RewardsService(gpsService, new RewardCentral());

        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(gpsService, rewardsService);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        Attraction attraction = gpsService.getAttractions().get(0);
        user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
        tourGuideService.trackUserLocation(user);
        List<UserReward> userRewards = user.getUserRewards();
        tourGuideService.tracker.stopTracking();
        assertTrue(userRewards.size() == 1);
    }

    @Test
    public void isWithinAttractionProximity() {
        GpsService gpsService = new GpsServiceImpl(new GpsUtil());
        RewardsService rewardsService = new RewardsService(gpsService, new RewardCentral());
        Attraction attraction = gpsService.getAttractions().get(0);
        assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
    }

    // TODO / Needs fixed - can throw ConcurrentModificationException
    // TODO / stopTracking mis après new TourGuideService pour éviter l'ajout de rewards par le tracker ...
    @Test
    public void nearAllAttractions() {
        GpsService gpsService = new GpsServiceImpl(new GpsUtil());
        RewardsService rewardsService = new RewardsService(gpsService, new RewardCentral());
        rewardsService.setProximityBuffer(Integer.MAX_VALUE);

        InternalTestHelper.setInternalUserNumber(1);
        TourGuideService tourGuideService = new TourGuideService(gpsService, rewardsService);
        tourGuideService.tracker.stopTracking();

        User user = tourGuideService.getAllUsers().get(0);
        rewardsService.calculateRewards(user);
        List<UserReward> userRewards = tourGuideService.getUserRewards(user);

        assertEquals(gpsService.getAttractions().size(), userRewards.size());
    }

}
