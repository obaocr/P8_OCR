package tourGuide;

import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

@SpringBootTest
@ContextConfiguration(classes = PerfConfig.class)
@EnableAutoConfiguration
public class TestPerformanceCalcReward {

    @Autowired
    private TourGuideService tourGuideService;

    @Disabled("Integration")
    @Test
    public void highVolumeGetRewards() {

        // Users should be incremented up to 100,000, and test finishes within 20 minutes
        InternalTestHelper.setInternalUserNumber(1000);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Attraction attraction = new Attraction("Musee", "Paris", "France", 1.0, 2.0);
        List<User> allUsers = new ArrayList<>();
        allUsers = tourGuideService.getAllUsers();

        allUsers.forEach(u -> {
            u.addToVisitedLocations(new VisitedLocation(u.getUserId(), attraction, new Date()));
        });
        // calculateRewards for all users in parallel mode
        tourGuideService.getRewardsService().calculateRewardsForUsers(allUsers);

        stopWatch.stop();
        tourGuideService.tracker.stopTracking();

        System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
        assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }

}

