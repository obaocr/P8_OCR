package tourGuide;

import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
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
import java.util.stream.IntStream;

import static org.junit.Assert.assertTrue;

@SpringBootTest
@ContextConfiguration(classes = PerfConfig.class)
@EnableAutoConfiguration
public class TestPerformanceTrackUser {

    @Autowired
    private TourGuideService tourGuideService;

    @Disabled("Integration")
    @Test
    public void highVolumeTrackLocation() {
        // Users should be incremented up to 100,000, and test finishes within 15 minutes
        List<User> allUsers = new ArrayList<>();
        int internalUserNumber = 1000;
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

        //InternalTestHelper.setInternalUserNumber(1000);
        allUsers = tourGuideService.getAllUsers();
        System.out.println("**** nb users : " + allUsers.size());

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
}
