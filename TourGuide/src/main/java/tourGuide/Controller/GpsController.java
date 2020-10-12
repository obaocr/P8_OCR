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
import tourGuide.Model.AttractionMapper;
import tourGuide.Model.VisitedLocationMapper;
import tourGuide.Proxies.GpsProxy;

import java.util.List;
import java.util.UUID;

@RestController
public class GpsController {

    private Logger logger = LoggerFactory.getLogger(GpsController.class);

    @Autowired
    private GpsProxy gpsProxy;

    /**
     * Call GpsApi micro service
     * @return
     */

    @GetMapping("/gpsattractions")
    public List<AttractionMapper> gpsGetAttractions() {
        logger.debug("gpsGetAttractions / appel microservice");
        List<AttractionMapper> attractionMappers = gpsProxy.gpsGetAttractions();
        return  attractionMappers;
    }

    @GetMapping("/gpsuserlocation")
    public VisitedLocationMapper gpsGetUserLocation(@RequestParam String userId) {
        logger.debug("gpsGetUserLocation / appel microservice");
        VisitedLocationMapper visitedLocationMapper = gpsProxy.gpsGetUserLocation(userId);
        return visitedLocationMapper;
    }

}
