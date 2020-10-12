package Proxy;

import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tourGuide.Model.AttractionMapper;
import tourGuide.Model.LocationMapper;
import tourGuide.Model.VisitedLocationMapper;
import tourGuide.Proxies.GpsProxy;
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

/**
 * Units tests for GpsProxyService, mock for GpsProxy
 */
@ExtendWith(SpringExtension.class)
public class GpsApiTest {

    @TestConfiguration
    static class GpsTestsContextConfiguration {

        @Bean
        public GpsProxyService gpsService() {
            return new GpsProxyServiceImpl();
        }

    }

    @Autowired
    private GpsProxyService gpsProxyService;

    @MockBean
    private GpsProxy gpsProxy;

    @Test
    public void testSvcGetAttractions() {
        AttractionMapper attraction1 = new AttractionMapper("Musee", "Paris", "France", UUID.randomUUID(), 1.0, 2.0);
        AttractionMapper attraction2 = new AttractionMapper("Musee", "Paris", "France", UUID.randomUUID(), 1.0, 2.0);
        List<AttractionMapper> attractions = new ArrayList<>();
        attractions.add(attraction1);
        attractions.add(attraction2);
        Mockito.when(gpsProxy.gpsGetAttractions()).thenReturn(attractions);
        List<Attraction> response = gpsProxyService.gpsAttractions();
        assertTrue(response.size() == 2);
    }

    @Test
    public void testSvcGetUserLocation() {
        UUID userId = UUID.randomUUID();
        LocationMapper locationMapper = new LocationMapper(10.0,10.0);
        Date dt = new Date();
        VisitedLocationMapper visitedLocationMapper = new VisitedLocationMapper(userId, locationMapper, dt);
        Mockito.when(gpsProxy.gpsGetUserLocation(anyString())).thenReturn(visitedLocationMapper);
        VisitedLocation visitedLocation = gpsProxyService.gpsUserLocation(userId);
        assertTrue(visitedLocation.timeVisited == dt);
    }

}
