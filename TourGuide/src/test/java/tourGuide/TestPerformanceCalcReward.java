package tourGuide;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import tourGuide.Model.Attraction;
import tourGuide.Model.Location;
import tourGuide.Model.VisitedLocation;
import tourGuide.Proxies.GpsProxy;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.GpsProxyService;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

@SpringBootTest
@ContextConfiguration(classes = PerfConfig.class)
@EnableAutoConfiguration
public class TestPerformanceCalcReward {

    @Autowired
    private TourGuideService tourGuideService;

    @Autowired
    private GpsProxyService gpsProxyService;

    /**
     * Test de charge pour CalculateReward
     *  1 "new UserReward" par User est déclenché
     */
    @Disabled("Integration")
    @Test
    public void highVolumeGetRewards() {
        // Users should be incremented up to 100,000, and test finishes within 20 minutes
        int internalUserNumber = 100;
        for (int i = 0; i < internalUserNumber; i++) {
            String userName = "internalUser" + i;
            String phone = "000";
            String email = userName + "@tourGuide.com";
            User user = new User(UUID.randomUUID(), userName, phone, email);
            tourGuideService.addUser(user);
        };

        Attraction attraction = gpsProxyService.gpsAttractions().get(0);
        List<User> allUsers = tourGuideService.getAllUsers();

        allUsers.forEach(u -> {
            u.addToVisitedLocations(new VisitedLocation(u.getUserId(), attraction, new Date()));
        });

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // calculateRewards for all users in parallel mode
        tourGuideService.getRewardsService().calculateRewardsForUsers(allUsers);

        stopWatch.stop();
        tourGuideService.tracker.stopTracking();

        System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
        assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }

}

