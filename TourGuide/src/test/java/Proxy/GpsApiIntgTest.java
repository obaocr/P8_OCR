package Proxy;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import tourGuide.Model.AttractionMapper;
import tourGuide.Proxies.GpsProxy;
import tourGuide.service.TourGuideService;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Integration tests for GpsProxy
 * The Gps service must be UP
 */
@SpringBootTest
@ContextConfiguration(classes = FeignConfig.class)
@EnableAutoConfiguration
public class GpsApiIntgTest {

    @Autowired
    private TourGuideService tourGuideService;

    @Autowired
    private GpsProxy gpsProxy;

    @Test
    public void testProxyGetAttractions() {
        List<AttractionMapper> attractionMappers = gpsProxy.gpsGetAttractions();
        assertTrue(attractionMappers.size() > 0);
    }

}
