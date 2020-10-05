package tourGuide.Controller;

import com.jsoniter.JsonIterator;
import com.jsoniter.output.JsonStream;
import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tourGuide.Proxies.GpsProxy;

import java.util.List;
import java.util.UUID;

@RestController
public class GpsController {

    private Logger logger = LoggerFactory.getLogger(GpsController.class);

    // TODO  à voir pour un bean ??? /
    GpsUtil gpsUtil = new GpsUtil();

    @Autowired
    GpsProxy gpsProxy;

    @GetMapping("/gpsattractions")
    public String gpsGetAttractions() {
        logger.debug("gpsGetAttractions  / appel microservice");
        //List<Attraction> attractions = gpsUtil.getAttractions();
        String attractions = gpsProxy.gpsGetAttractions();
        return  attractions;
    }

    @GetMapping("/gpsuserlocation")
    public VisitedLocation gpsGetUserLocation(@RequestParam UUID userId) {
        logger.debug("gpsGetUserLocation");
        VisitedLocation visitedLocation = gpsUtil.getUserLocation(userId);
        return visitedLocation;
    }

}
