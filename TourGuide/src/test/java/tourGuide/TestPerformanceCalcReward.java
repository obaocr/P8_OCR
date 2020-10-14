package tourGuide;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
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

    @Disabled("Integration")
    @Test
    public void highVolumeGetRewards() {

        // Users should be incremented up to 100,000, and test finishes within 20 minutes
        List<User> allUsers = new ArrayList<>();
        int internalUserNumber = 10000;
        for (int i = 0; i < internalUserNumber; i++) {
            String userName = "internalUser" + i;
            String phone = "000";
            String email = userName + "@tourGuide.com";
            User user = new User(UUID.randomUUID(), userName, phone, email);
            for (int j = 0; j < 3; j++) {
                double leftLimit = -180;
                double rightLimit = 180;
                double lng = leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
                leftLimit = -85.05112878;
                rightLimit = 85.05112878;
                double lat = leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
                LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
                Date dt = Date.from(localDateTime.toInstant(ZoneOffset.UTC));
                user.addToVisitedLocations(new VisitedLocation(user.getUserId(), new Location(lat, lng), dt));
            }
            tourGuideService.addUser(user);
        };

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Attraction attraction = new Attraction("Musee", "Paris", "France", 1.0, 2.0);
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

