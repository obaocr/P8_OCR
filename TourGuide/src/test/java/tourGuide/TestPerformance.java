package tourGuide;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;
import rewardCentral.RewardCentral;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.GpsService;
import tourGuide.service.GpsServiceImpl;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class TestPerformance {

    /*
     * A note on performance improvements:
     *
     *     The number of users generated for the high volume tests can be easily adjusted via this method:
     *
     *     		InternalTestHelper.setInternalUserNumber(100000);
     *
     *
     *     These tests can be modified to suit new solutions, just as long as the performance metrics
     *     at the end of the tests remains consistent.
     *
     *     These are performance metrics that we are trying to hit:
     *
     *     highVolumeTrackLocation: 100,000 users within 15 minutes:
     *     		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
     *
     *     highVolumeGetRewards: 100,000 users within 20 minutes:
     *          assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
     */

    @Test
    public void highVolumeTrackLocation() {
        GpsService gpsService = new GpsServiceImpl(new GpsUtil());
        RewardsService rewardsService = new RewardsService(gpsService, new RewardCentral());
        // Users should be incremented up to 100,000, and test finishes within 15 minutes
        InternalTestHelper.setInternalUserNumber(1000);
        TourGuideService tourGuideService = new TourGuideService(gpsService, rewardsService);
        tourGuideService.tracker.stopTracking();

        List<User> allUsers = new ArrayList<>();
        allUsers = tourGuideService.getAllUsers();

        Date d1 = new Date();
        /*for (User user : allUsers) {
        stopWatch.start();
            tourGuideService.trackUserLocation(user);
        }*/
        tourGuideService.trackUserLocationForAllUsers(allUsers);

        Date d2 = new Date();
        long timeMs = d2.getTime() - d1.getTime();
        //tourGuideService.tracker.stopTracking();
        System.out.println("temps highVolumeTrackLocation en ms : " + (d2.getTime() - d1.getTime()));
        // 15 minutes => 900 secondes max, pour 100.000 users
        assertTrue(TimeUnit.MINUTES.toSeconds(900) >= TimeUnit.MILLISECONDS.toSeconds(timeMs));
    }

    @Test
    public void highVolumeGetRewards() {
        GpsService gpsService = new GpsServiceImpl(new GpsUtil());
        RewardsService rewardsService = new RewardsService(gpsService, new RewardCentral());

        // Users should be incremented up to 100,000, and test finishes within 20 minutes
        InternalTestHelper.setInternalUserNumber(100);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // TODO OBA : attention TourGuideService lance le Tracker.. on ne devrait pas, pas propre ...
        // TODO =>
        TourGuideService tourGuideService = new TourGuideService(gpsService, rewardsService);

        Attraction attraction = gpsService.getAttractions().get(0);
        List<User> allUsers = new ArrayList<>();
        allUsers = tourGuideService.getAllUsers();

        allUsers.forEach(u -> {
            u.addToVisitedLocations(new VisitedLocation(u.getUserId(), attraction, new Date()));
            rewardsService.calculateRewards(u);
        });

        for (User user : allUsers) {
            assertTrue(user.getUserRewards().size() > 0);
        }
        stopWatch.stop();
        tourGuideService.tracker.stopTracking();

        System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
        assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }

}
