package tourGuide;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

@SpringBootTest
@ContextConfiguration(classes = PerfConfig.class)
@EnableAutoConfiguration
public class TestPerformanceTrackUser {

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
}
