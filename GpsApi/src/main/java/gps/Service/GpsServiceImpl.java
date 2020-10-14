package gps.Service;

import gps.Model.AttractionMapper;
import gps.Model.LocationMapper;
import gps.Model.VisitedLocationMapper;
import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Service layer for GpsUtils
 */
@Service
public class GpsServiceImpl implements GpsService {

    private Logger logger = LoggerFactory.getLogger(GpsServiceImpl.class);
    private final GpsUtil gpsUtil;

    public GpsServiceImpl() {
        logger.debug("constructor GpsServiceImpl");
        Locale.setDefault(Locale.US);
        this.gpsUtil = new GpsUtil();
    }

    @Override
    public List<AttractionMapper> getAttractions() {
        logger.debug("getAttractions size : " + gpsUtil.getAttractions().size());
        List<Attraction> attractions =  gpsUtil.getAttractions();
        List<AttractionMapper> attractionMappers = new ArrayList<>();
        for (Attraction a : attractions) {
            AttractionMapper attractionMapper = new AttractionMapper(a.attractionName, a.city, a.state, a.attractionId, a.latitude,a.longitude);
            attractionMappers.add(attractionMapper);
        }
        return attractionMappers;
    }

    @Override
    public VisitedLocationMapper getUserLocation(UUID userID) {
        logger.debug("getUserLocation for user :" + userID);
        VisitedLocation vl = gpsUtil.getUserLocation(userID);
        LocationMapper locationMapper = new LocationMapper(vl.location.longitude, vl.location.latitude);
        VisitedLocationMapper visitedLocationMapper = new VisitedLocationMapper(vl.userId, locationMapper, vl.timeVisited);
        return visitedLocationMapper;
    }
}
