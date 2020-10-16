package tourGuide;

<<<<<<< HEAD
=======
import org.apache.commons.lang3.time.StopWatch;
>>>>>>> feature/optim
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import tourGuide.Model.Location;
import tourGuide.Model.VisitedLocation;
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
public class TestPerformanceTrackUser {

    @Autowired
    private TourGuideService tourGuideService;

<<<<<<< HEAD
    @Disabled("test intégration performance")
=======
    @Disabled("Integration")
>>>>>>> feature/optim
    @Test
    public void highVolumeTrackLocation() {

        // Users should be incremented up to 100,000, and test finishes within 15 minutes
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

        System.out.println("**** nb users : " + tourGuideService.getAllUsers().size());
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        tourGuideService.trackUserLocationBulk(tourGuideService.getAllUsers());

        stopWatch.stop();
        tourGuideService.tracker.stopTracking();
        // 15 minutes => 900 secondes max selon la demande, pour 100.000 users
        System.out.println("highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
        assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }
}
