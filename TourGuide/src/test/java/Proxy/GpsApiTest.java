package Proxy;

import gpsUtil.location.Attraction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tourGuide.Model.AttractionMapper;
import tourGuide.Proxies.GpsProxy;
import tourGuide.service.GpsProxyService;
import tourGuide.service.GpsProxyServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

@ExtendWith(SpringExtension.class)
public class GpsApiTest {

    @TestConfiguration
    static class GpsTestsContextConfiguration {

        @Bean
        public GpsProxyService gpsService() {
            return new GpsProxyServiceImpl();
        }

    }

    @MockBean
    private GpsProxy gpsProxy;

    @Autowired
    private GpsProxyService gpsProxyService;

    @Test
    public void testSvcGetAttractions() {
        AttractionMapper attraction1 = new AttractionMapper("Musee", "Paris", "France", UUID.randomUUID(), 1.0, 2.0);
        AttractionMapper attraction2 = new AttractionMapper("Musee", "Paris", "France", UUID.randomUUID(), 1.0, 2.0);
        List<AttractionMapper> attractions = new ArrayList<>();
        attractions.add(attraction1);
        attractions.add(attraction2);
        System.out.println("****************** OBA *****");
        Mockito.when(gpsProxy.gpsGetAttractions()).thenReturn(attractions);
        List<Attraction> response = gpsProxyService.gpsAttractions();
        assertTrue(response.size() == 2);
        System.out.println("response : " + response.size());
    }

}
