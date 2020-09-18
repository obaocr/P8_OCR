package tourGuide.Controller;

import com.jsoniter.output.JsonStream;
import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class GpsController {

    private Logger logger = LoggerFactory.getLogger(GpsController.class);

    // TODO  à voir pour un bean ??? /
    GpsUtil gpsUtil = new GpsUtil();

    @GetMapping("/gpsGetAttractions")
    public List<Attraction> gpsGetAttractions() {
        logger.debug("gpsGetAttractions");
        List<Attraction> attractions = gpsUtil.getAttractions();
        return  attractions;
    }

    @GetMapping("/gpsGetUserLocation")
    public VisitedLocation gpsGetUserLocation(@RequestParam UUID userId) {
        logger.debug("gpsGetUserLocation");
        VisitedLocation visitedLocation = gpsUtil.getUserLocation(userId);
        return visitedLocation;
    }

}
