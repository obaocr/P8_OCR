package gps.Controller;

import gps.Service.GpsService;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class GpsController {

    private Logger logger = LoggerFactory.getLogger(GpsController.class);

    @Autowired
    GpsService gpsService;

    @GetMapping("/")
    public String gpsHome() {
        logger.debug("gpsHome");
        return  "P8 gpsHome";
    }

    @GetMapping("/gpsGetAttractions")
    public List<Attraction> gpsGetAttractions() {
        logger.debug("gpsGetAttractions");
        List<Attraction> attractions = gpsService.getAttractions();
        return  attractions;
    }

    @GetMapping("/gpsGetUserLocation")
    public VisitedLocation gpsGetUserLocation(@RequestParam UUID userId) {
        logger.debug("gpsGetUserLocation");
        // Exemple de UUID f07621a4-6365-4074-b040-ac655217f82f
        VisitedLocation visitedLocation = gpsService.getUserLocation(userId);
        return visitedLocation;
    }

}
