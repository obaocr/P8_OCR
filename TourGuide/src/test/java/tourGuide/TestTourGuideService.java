package tourGuide;

import gpsUtil.GpsUtil;
import gpsUtil.location.VisitedLocation;
import org.junit.Ignore;
import org.junit.Test;
import rewardCentral.RewardCentral;
import tourGuide.Model.AttractionResponse;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.GpsService;
import tourGuide.service.GpsServiceImpl;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;
import tripPricer.Provider;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestTourGuideService {

    @Test
    public void getUserLocation() {
        GpsService gpsService = new GpsServiceImpl(new GpsUtil());
        RewardsService rewardsService = new RewardsService(gpsService, new RewardCentral());
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(gpsService, rewardsService);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);
        tourGuideService.tracker.stopTracking();
        assertTrue(visitedLocation.userId.equals(user.getUserId()));
    }

    // TODO OBA - Tests getAllUsersCurrentLocation
    // TODO OBA -  Tester plus finement ... par exemple les résultats attendus comme les UserName.. et que la location ne soit pas vide ...
    // TODO A voir pour mocker ?
    @Test
    public void getAllUsersCurrentLocationShouldReturnUsersLocations() {
        GpsService gpsService = new GpsServiceImpl(new GpsUtil());
        RewardsService rewardsService = new RewardsService(gpsService, new RewardCentral());
        InternalTestHelper.setInternalUserNumber(55);
        TourGuideService tourGuideService = new TourGuideService(gpsService, rewardsService);
        tourGuideService.tracker.stopTracking();
        assertTrue(tourGuideService.getAllUsersCurrentLocation().size() == 55);
    }

    @Test
    public void addUser() {
        GpsService gpsService = new GpsServiceImpl(new GpsUtil());
        RewardsService rewardsService = new RewardsService(gpsService, new RewardCentral());
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(gpsService, rewardsService);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

        tourGuideService.addUser(user);
        tourGuideService.addUser(user2);

        User retrivedUser = tourGuideService.getUser(user.getUserName());
        User retrivedUser2 = tourGuideService.getUser(user2.getUserName());

        tourGuideService.tracker.stopTracking();

        assertEquals(user, retrivedUser);
        assertEquals(user2, retrivedUser2);
    }

    @Test
    public void getAllUsers() {
        GpsService gpsService = new GpsServiceImpl(new GpsUtil());
        RewardsService rewardsService = new RewardsService(gpsService, new RewardCentral());
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(gpsService, rewardsService);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

        tourGuideService.addUser(user);
        tourGuideService.addUser(user2);

        List<User> allUsers = tourGuideService.getAllUsers();

        tourGuideService.tracker.stopTracking();

        assertTrue(allUsers.contains(user));
        assertTrue(allUsers.contains(user2));
    }

    @Test
    public void trackUser() {
        GpsService gpsService = new GpsServiceImpl(new GpsUtil());
        RewardsService rewardsService = new RewardsService(gpsService, new RewardCentral());
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(gpsService, rewardsService);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);

        tourGuideService.tracker.stopTracking();

        assertEquals(user.getUserId(), visitedLocation.userId);
    }

    @Ignore // Not yet implemented
    @Test
    public void getNearbyAttractions() {
        GpsService gpsService = new GpsServiceImpl(new GpsUtil());
        RewardsService rewardsService = new RewardsService(gpsService, new RewardCentral());
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(gpsService, rewardsService);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);

        List<AttractionResponse> attractions = tourGuideService.getNearByAttractions(user.getUserName());

        tourGuideService.tracker.stopTracking();

        assertEquals(5, attractions.size());
    }

    // TODO OBA => à voir, pas d'annotations / j'ai ajouté ?
    @Test
    public void getTripDeals() {
        GpsService gpsService = new GpsServiceImpl(new GpsUtil());
        RewardsService rewardsService = new RewardsService(gpsService, new RewardCentral());
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(gpsService, rewardsService);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

        List<Provider> providers = tourGuideService.getTripDeals(user);

        tourGuideService.tracker.stopTracking();

        assertEquals(5, providers.size());
    }


}
