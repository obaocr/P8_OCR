package gps.Controller;

import com.jsoniter.output.JsonStream;
import gps.Model.AttractionMapper;
import gps.Model.VisitedLocationMapper;
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

/**
 * Controller for GpsService
 */
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

    @GetMapping("/gpsattractions")
    public List<AttractionMapper> gpsGetAttractions() {
        logger.debug("GpsApi gpsGetAttractions");
        //List<Attraction> attractions = gpsService.getAttractions();
        //return JsonStream.serialize(gpsService.getAttractions());
        return gpsService.getAttractions();
    }

    @GetMapping("/gpsuserlocation")
    public VisitedLocationMapper gpsGetUserLocation(@RequestParam String userId) {
        logger.debug("gpsGetUserLocation");
        // Exemple de UUID f07621a4-6365-4074-b040-ac655217f82f
        VisitedLocationMapper visitedLocationMapper = gpsService.getUserLocation(UUID.fromString(userId));
        return visitedLocationMapper;
    }

}
