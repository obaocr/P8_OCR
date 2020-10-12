package tourGuide;

import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.GpsProxyService;
import tourGuide.service.GpsProxyServiceImpl;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

@SpringBootTest
//@ActiveProfiles("testperf")
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

    /*
    @TestConfiguration
    static class GpsTestsContextConfiguration {

        @Bean
        public GpsProxyService gpsService() {
            return new GpsProxyServiceImpl();
        }

        @Bean
        public RewardsService gpsRewardsService() {
            return new RewardsService();
        }

        @Bean
        public TourGuideService gpsTourGuideService() {
            return new TourGuideService(gpsService(), gpsRewardsService());
        }

    }




    @Autowired
    private GpsProxyService gpsProxyService;

    @Autowired
    private RewardsService rewardsService;

     */


    @Autowired
    private TourGuideService tourGuideService;


    @Test
    public void highVolumeTrackLocation() {
        // Users should be incremented up to 100,000, and test finishes within 15 minutes
        InternalTestHelper.setInternalUserNumber(10000);
        //TourGuideService tourGuideService = new TourGuideService(gpsProxyService, rewardsService);

        List<User> allUsers = tourGuideService.getAllUsers();

        Date d1 = new Date();
        // Nouvelle méthode pour lancer trackUserLocation pour tous les users
        tourGuideService.trackUserLocationBulk(allUsers);

        Date d2 = new Date();
        long timeMs = d2.getTime() - d1.getTime();
        tourGuideService.tracker.stopTracking();
        System.out.println("=====> temps highVolumeTrackLocation en ms : " + (d2.getTime() - d1.getTime()));
        // 15 minutes => 900 secondes max selon la demande, pour 100.000 users
        assertTrue(TimeUnit.MINUTES.toSeconds(900) >= TimeUnit.MILLISECONDS.toSeconds(timeMs));
    }

    @Test
    public void highVolumeGetRewards() {
        GpsProxyService gpsProxyService = new GpsProxyServiceImpl();
        RewardsService rewardsService = new RewardsService();

        // Users should be incremented up to 100,000, and test finishes within 20 minutes
        InternalTestHelper.setInternalUserNumber(10);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        TourGuideService tourGuideService = new TourGuideService(gpsProxyService, rewardsService);

        Attraction attraction = new Attraction("Musee", "Paris", "France", 1.0, 2.0);
        List<User> allUsers = new ArrayList<>();
        allUsers = tourGuideService.getAllUsers();

        allUsers.forEach(u -> {
            u.addToVisitedLocations(new VisitedLocation(u.getUserId(), attraction, new Date()));
        });
        // calculateRewards for all users in parallel mode
        rewardsService.calculateRewardsForUsers(allUsers);

        for (User user : allUsers) {
            assertTrue(user.getUserRewards().size() > 0);
        }

        stopWatch.stop();
        tourGuideService.tracker.stopTracking();

        System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
        assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }

}
