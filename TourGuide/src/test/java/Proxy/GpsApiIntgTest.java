package Proxy;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tourGuide.Model.AttractionMapper;
import tourGuide.Proxies.GpsProxy;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Integration tests for GpsProxy
 * The Gps service must be UP
 */
@SpringBootTest(classes = FeignConfig.class)
public class GpsApiIntgTest {

    @Autowired
    private GpsProxy gpsProxy;

    @Test
    public void testProxyGetAttractions() {
        List<AttractionMapper> attractionMappers = this.gpsProxy.gpsGetAttractions();
        assertTrue(attractionMappers.size() > 0);
    }

}
