package Proxy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tourGuide.Model.AttractionMapper;
import tourGuide.Proxies.GpsProxy;

import java.util.List;

import static org.junit.Assert.assertTrue;

@SpringBootTest(classes = FeignConfig.class)
public class GpsApiProxyTest {

    @Autowired
    private GpsProxy gpsProxy;

    // Test intégration / OK ça passe

    @Test
    public void testProxyGetAttractions() {
        List<AttractionMapper> attractionMappers = this.gpsProxy.gpsGetAttractions();
        assertTrue(attractionMappers.size() > 0);
        System.out.println("attractionMappers : " + attractionMappers.size());


    }

}
