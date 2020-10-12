package tourGuide;

import gpsUtil.location.VisitedLocation;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import tourGuide.Model.AttractionResponseDTO;
import tourGuide.Proxies.GpsProxy;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.GpsProxyService;
import tourGuide.service.GpsProxyServiceImpl;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;
import tripPricer.Provider;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@ContextConfiguration
@SpringBootTest
public class TestTourGuideService {

    @MockBean
    private GpsProxy gpsProxy;

    // TODO => plante...
    @Test
    public void getUserLocation() {
        GpsProxyService gpsProxyService = new GpsProxyServiceImpl();
        RewardsService rewardsService = new RewardsService();
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(gpsProxyService, rewardsService);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user).get(0);
        tourGuideService.tracker.stopTracking();
        assertTrue(visitedLocation.userId.equals(user.getUserId()));
    }

    // TODO OBA - Tests getAllUsersCurrentLocation
    // TODO OBA -  Tester plus finement ... par exemple les résultats attendus comme les UserName.. et que la location ne soit pas vide ...
    @Test
    public void getAllUsersCurrentLocationShouldReturnUsersLocations() {
        GpsProxyService gpsProxyService = new GpsProxyServiceImpl();
        RewardsService rewardsService = new RewardsService();
        InternalTestHelper.setInternalUserNumber(55);
        TourGuideService tourGuideService = new TourGuideService(gpsProxyService, rewardsService);
        tourGuideService.tracker.stopTracking();
        assertTrue(tourGuideService.getAllUsersCurrentLocation().size() == 55);
    }

    @Test
    public void addUser() {
        GpsProxyService gpsProxyService = new GpsProxyServiceImpl();
        RewardsService rewardsService = new RewardsService();
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(gpsProxyService, rewardsService);
        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");
        tourGuideService.addUser(user);
        tourGuideService.addUser(user2);
        User retrievedUser = tourGuideService.getUser(user.getUserName());
        User retrievedUser2 = tourGuideService.getUser(user2.getUserName());
        tourGuideService.tracker.stopTracking();
        assertEquals(user, retrievedUser);
        assertEquals(user2, retrievedUser2);
    }

    @Test
    public void getAllUsers() {
        GpsProxyService gpsProxyService = new GpsProxyServiceImpl();
        RewardsService rewardsService = new RewardsService();
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(gpsProxyService, rewardsService);
        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");
        tourGuideService.addUser(user);
        tourGuideService.addUser(user2);
        List<User> allUsers = tourGuideService.getAllUsers();
        tourGuideService.tracker.stopTracking();
        assertTrue(allUsers.contains(user));
        assertTrue(allUsers.contains(user2));
    }

    // TODO => plante
    @Test
    public void trackUser() {
        GpsProxyService gpsProxyService = new GpsProxyServiceImpl();
        RewardsService rewardsService = new RewardsService();
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(gpsProxyService, rewardsService);
        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user).get(0);
        tourGuideService.tracker.stopTracking();
        assertEquals(user.getUserId(), visitedLocation.userId);
    }

    @Test
    public void getTripDeals() {
        GpsProxyService gpsProxyService = new GpsProxyServiceImpl();
        RewardsService rewardsService = new RewardsService();
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(gpsProxyService, rewardsService);
        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        List<Provider> providers = tourGuideService.getTripDeals(user);
        tourGuideService.tracker.stopTracking();
        assertEquals(5, providers.size());
    }

    // TODO => plante
    @Test
    public void getNearbyAttractions() {
        GpsProxyService gpsProxyService = new GpsProxyServiceImpl();
        RewardsService rewardsService = new RewardsService();
        InternalTestHelper.setInternalUserNumber(1);
        TourGuideService tourGuideService = new TourGuideService(gpsProxyService, rewardsService);
        User user = tourGuideService.getAllUsers().get(0);
        List<AttractionResponseDTO> attractions = tourGuideService.getNearByAttractions(user.getUserName());
        tourGuideService.tracker.stopTracking();
        assertEquals(5, attractions.size());
    }
}
