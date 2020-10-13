package Controller;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tourGuide.Controller.TourGuideController;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ContextConfiguration(classes = ControllerConfig.class)
@AutoConfigureMockMvc
public class TourGuideControllerTest {

    @MockBean
    private GpsProxy gpsProxy;

    @MockBean
    private RewardProxy rewardProxy;

    @MockBean
    private TourGuideService tourGuideService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Test
    public void testControllerGetLocation() throws Exception {
        /*AttractionMapper attraction1 = new AttractionMapper("Musee", "Paris", "France", UUID.randomUUID(), 1.0, 2.0);
        List<AttractionMapper> attractions = new ArrayList<>();
        attractions.add(attraction1);
        Mockito.when(gpsProxy.gpsGetAttractions()).thenReturn(attractions);

        UUID userId = UUID.randomUUID();
        LocationMapper locationMapper = new LocationMapper(1.0, 1.0);
        Date dt = new Date();
        VisitedLocationMapper visitedLocationMapper = new VisitedLocationMapper(userId, locationMapper, dt);
        Mockito.when(gpsProxy.gpsGetUserLocation(userId.toString())).thenReturn(visitedLocationMapper);

        RewardPointsMapper rewardPointsMapper = new RewardPointsMapper(765);
        Mockito.when(rewardProxy.getAttractionRewardPoints(anyString(), anyString())).thenReturn(rewardPointsMapper);

        //rewardsService.setProximityBuffer(Integer.MAX_VALUE);
        //InternalTestHelper.setInternalUserNumber(0);
        //TourGuideService tourGuideService = new TourGuideService(gpsProxyService, rewardsService);

         */

        VisitedLocation visitedLocation = new VisitedLocation(UUID.randomUUID(), new Location(10.0, 10.0), new Date());
        Mockito.when(tourGuideService.getUserLocationByName(anyString())).thenReturn(visitedLocation);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        this.mockMvc.perform(get("/location")
                .param("userName", "user1")
                .characterEncoding("utf-8"))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        /*
        User user = new User(userId, "jon", "000", "jon@tourGuide.com");
        Attraction attraction = new Attraction("Musee", "Paris", "France", 1.0, 1.0);
        user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
        tourGuideService.trackUserLocation(user);

        List<UserReward> userRewards = user.getUserRewards();

         */
        //tourGuideService.tracker.stopTracking();
        //assertTrue(userRewards.size() == 1);
    }

}
