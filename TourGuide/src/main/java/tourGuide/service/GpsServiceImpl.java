package tourGuide.service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;
// TODO enlever les lignes en commentaires en fin
public class GpsServiceImpl implements GpsService {

    private Logger logger = LoggerFactory.getLogger(GpsServiceImpl.class);
    private final GpsUtil gpsUtil;

    public GpsServiceImpl(GpsUtil gpsUtil) {
        //logger.debug("constructor GpsServiceImpl");
        this.gpsUtil = gpsUtil;
    }

    @Override
    public List<Attraction> getAttractions() {
        //logger.debug("getAttractions size : " + gpsUtil.getAttractions().size());
        return gpsUtil.getAttractions();
    }

    @Override
    public VisitedLocation getUserLocation(UUID userID) {
        //logger.debug("getUserLocation for user :" + userID);
        return gpsUtil.getUserLocation(userID);
    }
}
