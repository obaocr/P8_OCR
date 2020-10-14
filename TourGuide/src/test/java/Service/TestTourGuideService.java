package Service;

import gpsUtil.location.VisitedLocation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tourGuide.Model.*;
import tourGuide.Proxies.GpsProxy;
import tourGuide.Proxies.RewardProxy;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.GpsProxyService;
import tourGuide.service.GpsProxyServiceImpl;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(SpringExtension.class)
public class TestTourGuideService {

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

    }

    @Autowired
    private GpsProxyService gpsProxyService;

    @Autowired
    private RewardsService rewardsService;

    @MockBean
    private GpsProxy gpsProxy;

    @MockBean
    private RewardProxy rewardProxy;

    @Test
    public void getUserLocation() {

        AttractionMapper attraction1 = new AttractionMapper("Musee", "Paris", "France", UUID.randomUUID(), 1.0, 2.0);
        List<AttractionMapper> attractions = new ArrayList<>();
        attractions.add(attraction1);
        Mockito.when(gpsProxy.gpsGetAttractions()).thenReturn(attractions);

        UUID userId = UUID.randomUUID();
        LocationMapper locationMapper = new LocationMapper(1.0, 1.0);
        Date dt = new Date();
        VisitedLocationMapper visitedLocationMapper = new VisitedLocationMapper(userId, locationMapper, dt);
        Mockito.when(gpsProxy.gpsGetUserLocation(anyString())).thenReturn(visitedLocationMapper);

        RewardPointsMapper rewardPointsMapper = new RewardPointsMapper(765);
        Mockito.when(rewardProxy.getAttractionRewardPoints(anyString(), anyString())).thenReturn(rewardPointsMapper);

        InternalTestHelper.setInternalUserNumber(1);
        TourGuideService tourGuideService = new TourGuideService(gpsProxyService, rewardsService);

        User user = tourGuideService.getAllUsers().get(0);
        VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user).get(0);
        tourGuideService.tracker.stopTracking();
        assertTrue(visitedLocation.userId.equals(user.getUserId()));
    }

    @Test
    public void getAllUsersCurrentLocationShouldReturnUsersLocations() {
        InternalTestHelper.setInternalUserNumber(55);

        AttractionMapper attraction1 = new AttractionMapper("Musee", "Paris", "France", UUID.randomUUID(), 1.0, 2.0);
        List<AttractionMapper> attractions = new ArrayList<>();
        attractions.add(attraction1);
        Mockito.when(gpsProxy.gpsGetAttractions()).thenReturn(attractions);

        UUID userId = UUID.randomUUID();
        LocationMapper locationMapper = new LocationMapper(1.0, 1.0);
        Date dt = new Date();
        VisitedLocationMapper visitedLocationMapper = new VisitedLocationMapper(userId, locationMapper, dt);
        Mockito.when(gpsProxy.gpsGetUserLocation(anyString())).thenReturn(visitedLocationMapper);

        RewardPointsMapper rewardPointsMapper = new RewardPointsMapper(765);
        Mockito.when(rewardProxy.getAttractionRewardPoints(anyString(), anyString())).thenReturn(rewardPointsMapper);

        TourGuideService tourGuideService = new TourGuideService(gpsProxyService, rewardsService);
        tourGuideService.tracker.stopTracking();
        assertTrue(tourGuideService.getAllUsersCurrentLocation().size() == 55);
    }

    @Test
    public void addUser() {
        InternalTestHelper.setInternalUserNumber(0);

        AttractionMapper attraction1 = new AttractionMapper("Musee", "Paris", "France", UUID.randomUUID(), 1.0, 2.0);
        List<AttractionMapper> attractions = new ArrayList<>();
        attractions.add(attraction1);
        Mockito.when(gpsProxy.gpsGetAttractions()).thenReturn(attractions);

        UUID userId = UUID.randomUUID();
        LocationMapper locationMapper = new LocationMapper(1.0, 1.0);
        Date dt = new Date();
        VisitedLocationMapper visitedLocationMapper = new VisitedLocationMapper(userId, locationMapper, dt);
        Mockito.when(gpsProxy.gpsGetUserLocation(anyString())).thenReturn(visitedLocationMapper);

        RewardPointsMapper rewardPointsMapper = new RewardPointsMapper(765);
        Mockito.when(rewardProxy.getAttractionRewardPoints(anyString(), anyString())).thenReturn(rewardPointsMapper);

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
        InternalTestHelper.setInternalUserNumber(0);

        AttractionMapper attraction1 = new AttractionMapper("Musee", "Paris", "France", UUID.randomUUID(), 1.0, 2.0);
        List<AttractionMapper> attractions = new ArrayList<>();
        attractions.add(attraction1);
        Mockito.when(gpsProxy.gpsGetAttractions()).thenReturn(attractions);

        UUID userId = UUID.randomUUID();
        LocationMapper locationMapper = new LocationMapper(1.0, 1.0);
        Date dt = new Date();
        VisitedLocationMapper visitedLocationMapper = new VisitedLocationMapper(userId, locationMapper, dt);
        Mockito.when(gpsProxy.gpsGetUserLocation(anyString())).thenReturn(visitedLocationMapper);

        RewardPointsMapper rewardPointsMapper = new RewardPointsMapper(765);
        Mockito.when(rewardProxy.getAttractionRewardPoints(anyString(), anyString())).thenReturn(rewardPointsMapper);

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

    @Test
    public void trackUser() {
        InternalTestHelper.setInternalUserNumber(0);

        AttractionMapper attraction1 = new AttractionMapper("Musee", "Paris", "France", UUID.randomUUID(), 1.0, 2.0);
        List<AttractionMapper> attractions = new ArrayList<>();
        attractions.add(attraction1);
        Mockito.when(gpsProxy.gpsGetAttractions()).thenReturn(attractions);

        UUID userId = UUID.randomUUID();
        LocationMapper locationMapper = new LocationMapper(1.0, 1.0);
        Date dt = new Date();
        VisitedLocationMapper visitedLocationMapper = new VisitedLocationMapper(userId, locationMapper, dt);
        Mockito.when(gpsProxy.gpsGetUserLocation(anyString())).thenReturn(visitedLocationMapper);

        RewardPointsMapper rewardPointsMapper = new RewardPointsMapper(765);
        Mockito.when(rewardProxy.getAttractionRewardPoints(anyString(), anyString())).thenReturn(rewardPointsMapper);

        TourGuideService tourGuideService = new TourGuideService(gpsProxyService, rewardsService);
        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user).get(0);
        tourGuideService.tracker.stopTracking();
        assertEquals(user.getUserId(), visitedLocation.userId);
    }

    @Test
    public void getNearbyAttractions() {
        InternalTestHelper.setInternalUserNumber(1);

        List<AttractionMapper> attractions = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            AttractionMapper attraction = new AttractionMapper("Musee" + i, "Paris", "France", UUID.randomUUID(), 1.0 + 1, 2.0 + 1);
            attractions.add(attraction);
        }
        Mockito.when(gpsProxy.gpsGetAttractions()).thenReturn(attractions);

        UUID userId = UUID.randomUUID();
        LocationMapper locationMapper = new LocationMapper(1.0, 1.0);
        Date dt = new Date();
        VisitedLocationMapper visitedLocationMapper = new VisitedLocationMapper(userId, locationMapper, dt);
        Mockito.when(gpsProxy.gpsGetUserLocation(anyString())).thenReturn(visitedLocationMapper);

        RewardPointsMapper rewardPointsMapper = new RewardPointsMapper(765);
        Mockito.when(rewardProxy.getAttractionRewardPoints(anyString(), anyString())).thenReturn(rewardPointsMapper);

        TourGuideService tourGuideService = new TourGuideService(gpsProxyService, rewardsService);
        User user = tourGuideService.getAllUsers().get(0);
        List<AttractionResponseDTO> attractionDTOs = tourGuideService.getNearByAttractions(user.getUserName());
        tourGuideService.tracker.stopTracking();
        assertEquals(5, attractionDTOs.size());
    }
}
