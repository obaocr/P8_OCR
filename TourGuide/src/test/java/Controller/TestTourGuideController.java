package Controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tourGuide.Model.AttractionResponseDTO;
import tourGuide.Model.Location;
import tourGuide.Model.VisitedLocation;
import tourGuide.Proxies.GpsProxy;
import tourGuide.Proxies.RewardProxy;
import tourGuide.Proxies.TripPricerProxy;
import tourGuide.service.TourGuideService;

import java.util.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ContextConfiguration(classes = ControllerConfig.class)
@AutoConfigureMockMvc
public class TestTourGuideController {

    @MockBean
    private GpsProxy gpsProxy;

    @MockBean
    private RewardProxy rewardProxy;

    @MockBean
    private TripPricerProxy tripPricerProxy;

    @MockBean
    private TourGuideService tourGuideService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Test
    public void testGetLocation() throws Exception {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        VisitedLocation visitedLocation = new VisitedLocation(UUID.randomUUID(), new Location(10.0, 10.0), new Date());
        Mockito.when(tourGuideService.getUserLocation(tourGuideService.getUser(anyString()))).thenReturn(visitedLocation);

        this.mockMvc.perform(get("/location")
                .param("userName", "user1")
                .characterEncoding("utf-8"))
                .andDo(print()).andExpect(status().isOk()).andReturn();
    }

    @Test
    public void testAllCurrentLocation() throws Exception {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        Map<String, Location> mapUserLocation = new HashMap<>();
        mapUserLocation.put("User1",new Location(10.0,10.0));
        Mockito.when(tourGuideService.getAllUsersCurrentLocation()).thenReturn(mapUserLocation);

        this.mockMvc.perform(get("/allcurrentlocations")
                .characterEncoding("utf-8"))
                .andDo(print()).andExpect(status().isOk()).andReturn();
    }

    @Test
    public void testNearbyattractions() throws Exception {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        List<AttractionResponseDTO> attractionResponseDTOS = new ArrayList<>();
        AttractionResponseDTO attractionResponseDTO = new AttractionResponseDTO();
        attractionResponseDTO.setAttractionId(UUID.randomUUID().toString());
        attractionResponseDTO.setRewardsPoints(100);
        attractionResponseDTO.setState("FR");
        attractionResponseDTO.setCity("Paris");
        attractionResponseDTO.setLongitude(1.0);
        attractionResponseDTO.setLatitude(1.0);
        attractionResponseDTO.setDistanceWithCurrLoc(100.0);
        attractionResponseDTO.setAttractionName("Gare");
        attractionResponseDTOS.add(attractionResponseDTO);
        Mockito.when(tourGuideService.getNearByAttractions(anyString())).thenReturn(attractionResponseDTOS);

        this.mockMvc.perform(get("/nearbyattractions")
                .param("userName", "user1")
                .characterEncoding("utf-8"))
                .andDo(print()).andExpect(status().isOk()).andReturn();
    }

}
