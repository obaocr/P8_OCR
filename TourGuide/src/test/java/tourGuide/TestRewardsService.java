package tourGuide;

import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tourGuide.Model.AttractionMapper;
import tourGuide.Model.RewardPointsMapper;
import tourGuide.Proxies.GpsProxy;
import tourGuide.Proxies.RewardProxy;
import tourGuide.helper.InternalTestHelper;
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

@ExtendWith(SpringExtension.class)
public class TestRewardsService {

    @MockBean
    private GpsProxy gpsProxy;

    @MockBean
    private RewardProxy rewardProxy;

    // TODO => plante
    @Test
    public void userGetRewards() {
        RewardsService rewardsService = new RewardsService();
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(rewardsService);

        UUID userId = UUID.randomUUID();
        UUID attractionId = UUID.randomUUID();
        RewardPointsMapper rewardPointsMapper = new RewardPointsMapper(765);
        Mockito.when(rewardProxy.getAttractionRewardPoints(attractionId, userId)).thenReturn(rewardPointsMapper);

        AttractionMapper attractionMapper1 = new AttractionMapper("Musee", "Paris", "France", UUID.randomUUID(), 1.0, 2.0);
        AttractionMapper attractionMapper2 = new AttractionMapper("Musee", "Paris", "France", UUID.randomUUID(), 1.0, 2.0);
        List<AttractionMapper> attractionMappers = new ArrayList<>();
        attractionMappers.add(attractionMapper1);
        attractionMappers.add(attractionMapper2);
        Mockito.when(gpsProxy.gpsGetAttractions()).thenReturn(attractionMappers);

        User user = new User(userId, "jon", "000", "jon@tourGuide.com");
        Attraction attraction = new Attraction("Musee", "Paris", "France", 1.0, 2.0);
        user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
        tourGuideService.trackUserLocation(user);
        List<UserReward> userRewards = user.getUserRewards();
        tourGuideService.tracker.stopTracking();
        assertTrue(userRewards.size() == 1);
    }

    @Test
    public void isWithinAttractionProximity() {
        RewardsService rewardsService = new RewardsService();
        Attraction attraction = new Attraction("Musee", "Paris", "France", 1.0, 2.0);
        assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
    }

    // TODO / Needs fixed - can throw ConcurrentModificationException
    // TODO / stopTracking mis après new TourGuideService pour éviter l'ajout de rewards par le tracker ...
    // TODO => plante
    @Test
    public void nearAllAttractions() {
        AttractionMapper attractionMapper1 = new AttractionMapper("Musee", "Paris", "France", UUID.randomUUID(), 1.0, 2.0);
        AttractionMapper attractionMapper2 = new AttractionMapper("Musee", "Paris", "France", UUID.randomUUID(), 1.0, 2.0);
        List<AttractionMapper> attractionMappers = new ArrayList<>();
        attractionMappers.add(attractionMapper1);
        attractionMappers.add(attractionMapper2);
        Mockito.when(gpsProxy.gpsGetAttractions()).thenReturn(attractionMappers);

        RewardsService rewardsService = new RewardsService();
        rewardsService.setProximityBuffer(Integer.MAX_VALUE);

        InternalTestHelper.setInternalUserNumber(1);
        TourGuideService tourGuideService = new TourGuideService(rewardsService);

        User user = tourGuideService.getAllUsers().get(0);
        rewardsService.calculateRewards(user);
        List<UserReward> userRewards = tourGuideService.getUserRewards(user);

        tourGuideService.tracker.stopTracking();

        assertEquals(attractionMappers.size(), userRewards.size());
    }
}
