package Proxy;

import gpsUtil.location.Attraction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tourGuide.Model.AttractionMapper;
import tourGuide.Proxies.GpsProxy;
import tourGuide.service.GpsProxyService;
import tourGuide.service.GpsProxyServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

@SpringBootTest(classes = FeignConfig.class)
//@ExtendWith(SpringExtension.class)
public class GpsApiTest {

    /*@TestConfiguration
    static class GpsTestsContextConfiguration {

        @Bean
        public GpsProxyService gpsService() {
            return new GpsProxyServiceImpl();
        }

    }

    private GpsProxyService gpsProxyService;

     */


    @Test
    public void testSvcGetAttractions() {
        GpsProxyService gpsProxyService = new GpsProxyServiceImpl();
        Attraction attraction1 = new Attraction("Musee", "Paris", "France", 1.0, 2.0);
        Attraction attraction2 = new Attraction("Musee", "Paris", "France", 1.0, 2.0);
        List<Attraction> attractions = new ArrayList<>();
        attractions.add(attraction1);
        attractions.add(attraction2);
        System.out.println("****************** OBA *****");
        //Mockito.when(gpsProxyService.gpsAttractions()).thenReturn(attractions);
        List<Attraction> response = gpsProxyService.gpsAttractions();
        assertTrue(response.size() > 0);
        System.out.println("response : " + response.size());
    }

}
