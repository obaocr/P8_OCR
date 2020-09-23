package gps.Service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GpsServiceImpl implements GpsService {

    private Logger logger = LoggerFactory.getLogger(GpsServiceImpl.class);
    private final GpsUtil gpsUtil;

    public GpsServiceImpl() {
        logger.debug("constructor GpsServiceImpl");
        this.gpsUtil = new GpsUtil();
    }

    @Override
    public List<Attraction> getAttractions() {
        logger.debug("getAttractions size : " + gpsUtil.getAttractions().size());
        return gpsUtil.getAttractions();
    }

    @Override
    public VisitedLocation getUserLocation(UUID userID) {
        logger.debug("getUserLocation for user :" + userID);
        return gpsUtil.getUserLocation(userID);
    }
}
