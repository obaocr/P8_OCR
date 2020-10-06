package gps.Controller;

import gps.Model.AttractionMapper;
import gps.Model.LocationMapper;
import gps.Model.VisitedLocationMapper;
import gps.Service.GpsService;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = GpsController.class)
public class GpsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GpsService gpsService;

    @Test
    void gpsGetAttractions() throws Exception {
        List<AttractionMapper> attractionMappers = new ArrayList<>();
        Mockito.when(gpsService.getAttractions()).thenReturn(attractionMappers);
        this.mockMvc.perform(get("/gpsattractions").characterEncoding("utf-8"))
                .andDo(print()).andExpect(status().isOk()).andReturn();
    }

    @Test
    void gpsGetUserLocation() throws Exception {
        LocationMapper locationMapper = new LocationMapper(10.0,10.0);
        VisitedLocationMapper visitedLocationMapper = new VisitedLocationMapper(UUID.randomUUID(), locationMapper, new Date());
        Mockito.when(gpsService.getUserLocation(UUID.fromString("79ad4e7d-49b9-4c82-bd88-0e5dbc41fe75"))).thenReturn(visitedLocationMapper);
        this.mockMvc.perform(get("/gpsuserlocation").param("userId", "79ad4e7d-49b9-4c82-bd88-0e5dbc41fe75").characterEncoding("utf-8"))
                .andDo(print()).andExpect(status().isOk()).andReturn();
    }

}
