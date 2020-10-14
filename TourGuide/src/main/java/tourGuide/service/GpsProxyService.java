package tourGuide.service;

import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;

import java.util.List;
import java.util.UUID;

public interface GpsProxyService {

    List<Attraction> gpsAttractions();

    VisitedLocation gpsUserLocation(UUID userId);

}

