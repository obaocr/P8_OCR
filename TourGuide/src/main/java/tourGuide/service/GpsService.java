package tourGuide.service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

public class GpsService {

    private Logger logger = LoggerFactory.getLogger(GpsService.class);
    private final GpsUtil gpsUtil;

    public GpsService(GpsUtil gpsUtil) {
        this.gpsUtil = gpsUtil;
    }

    public List<Attraction> getAttractions() {
        logger.debug("getAttractions");
        return gpsUtil.getAttractions();
    }

    public VisitedLocation getUserLocation(UUID userID) {
        logger.debug("getUserLocation :" + userID);
        return gpsUtil.getUserLocation(userID);
    }

}
