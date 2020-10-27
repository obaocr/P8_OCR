package tourGuide.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import tourGuide.Model.*;
import tourGuide.Proxies.GpsProxy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Abstraction layer for GpsProxy
 */
public class GpsProxyServiceImpl implements GpsProxyService {

    private Logger logger = LoggerFactory.getLogger(GpsProxyServiceImpl.class);

    @Autowired
    private GpsProxy gpsProxy;

    @Override
    public List<Attraction> gpsAttractions() {
        List<Attraction> attractions = new ArrayList<>();
        List<AttractionMapper> attractionMappers = gpsProxy.gpsGetAttractions();
        for (AttractionMapper a : attractionMappers) {
            Attraction attraction = new Attraction(a.getAttractionName(), a.getCity(), a.getState(), a.getLatitude(), a.getLongitude());
            attractions.add(attraction);
        }
        return attractions;
    }

    @Override
    public VisitedLocation gpsUserLocation(UUID userId) {
        VisitedLocationMapper v = gpsProxy.gpsGetUserLocation(userId.toString());
        Location location = new Location(v.getLocation().getLongitude(), v.getLocation().getLatitude());
        VisitedLocation visitedLocation = new VisitedLocation(userId, location, v.getTimeVisited());
        return visitedLocation;
    }
}
