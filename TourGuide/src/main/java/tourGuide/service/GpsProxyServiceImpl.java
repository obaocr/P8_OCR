package tourGuide.service;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourGuide.Model.AttractionMapper;
import tourGuide.Model.VisitedLocationMapper;
import tourGuide.Proxies.GpsProxy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class GpsProxyServiceImpl implements GpsProxyService {

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
        VisitedLocationMapper v = gpsProxy.gpsGetUserLocation(userId);
        Location location = new Location(v.getLocation().getLongitude(), v.getLocation().getLatitude());
        VisitedLocation visitedLocation = new VisitedLocation(userId, location, v.getTimeVisited());
        return visitedLocation;
    }

}
