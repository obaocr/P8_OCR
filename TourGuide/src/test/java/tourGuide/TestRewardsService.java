package tourGuide;

import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.hamcrest.core.AnyOf;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Any;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tourGuide.Model.AttractionMapper;
import tourGuide.Model.LocationMapper;
import tourGuide.Model.RewardPointsMapper;
import tourGuide.Model.VisitedLocationMapper;
import tourGuide.Proxies.GpsProxy;
import tourGuide.Proxies.RewardProxy;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.GpsProxyService;
import tourGuide.service.GpsProxyServiceImpl;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;
import tourGuide.user.UserReward;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;

//@SpringBootTest
//@ContextConfiguration
@ExtendWith(SpringExtension.class)
public class TestRewardsService {

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
    public void isWithinAttractionProximity() {
        //GpsProxyService gpsProxyService = new GpsProxyServiceImpl();
        //RewardsService rewardsService = new RewardsService();
        Attraction attraction = new Attraction("Musee", "Paris", "France", 1.0, 2.0);
        assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
    }

    @Test
    public void userGetRewards() {
        AttractionMapper attraction1 = new AttractionMapper("Musee", "Paris", "France", UUID.randomUUID(), 1.0, 2.0);
        List<AttractionMapper> attractions = new ArrayList<>();
        attractions.add(attraction1);
        Mockito.when(gpsProxy.gpsGetAttractions()).thenReturn(attractions);

        UUID userId = UUID.randomUUID();
        LocationMapper locationMapper = new LocationMapper(1.0,1.0);
        Date dt = new Date();
        VisitedLocationMapper visitedLocationMapper = new VisitedLocationMapper(userId, locationMapper, dt);
        Mockito.when(gpsProxy.gpsGetUserLocation(userId.toString())).thenReturn(visitedLocationMapper);

        RewardPointsMapper rewardPointsMapper = new RewardPointsMapper(765);
        Mockito.when(rewardProxy.getAttractionRewardPoints(anyString(), anyString())).thenReturn(rewardPointsMapper);

        rewardsService.setProximityBuffer(Integer.MAX_VALUE);
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(gpsProxyService, rewardsService);
        User user = new User(userId, "jon", "000", "jon@tourGuide.com");
        Attraction attraction = new Attraction("Musee", "Paris", "France", 1.0, 1.0);
        user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
        tourGuideService.trackUserLocation(user);

        List<UserReward> userRewards = user.getUserRewards();
        tourGuideService.tracker.stopTracking();
        assertTrue(userRewards.size() == 1);
    }

    @Test
    public void nearAllAttractions() {
        AttractionMapper attraction1 = new AttractionMapper("Musee", "Paris", "France", UUID.randomUUID(), 1.0, 2.0);
        AttractionMapper attraction2 = new AttractionMapper("Gare", "Paris", "France", UUID.randomUUID(), 10.0, 2.0);
        List<AttractionMapper> attractions = new ArrayList<>();
        attractions.add(attraction1);
        attractions.add(attraction2);
        Mockito.when(gpsProxy.gpsGetAttractions()).thenReturn(attractions);

        rewardsService.setProximityBuffer(Integer.MAX_VALUE);
        InternalTestHelper.setInternalUserNumber(1);
        TourGuideService tourGuideService = new TourGuideService(gpsProxyService, rewardsService);

        User user = tourGuideService.getAllUsers().get(0);
        RewardPointsMapper rewardPointsMapper = new RewardPointsMapper(555);
        Mockito.when(rewardProxy.getAttractionRewardPoints(anyString(), anyString())).thenReturn(rewardPointsMapper);

        rewardsService.calculateRewards(user);
        List<UserReward> userRewards = tourGuideService.getUserRewards(user);
        tourGuideService.tracker.stopTracking();

        assertEquals(attractions.size(), userRewards.size());
    }
}
