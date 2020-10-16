package tourGuide.service;


import tourGuide.Model.Attraction;
import tourGuide.Model.VisitedLocation;

import java.util.List;
import java.util.UUID;

public interface GpsProxyService {

    List<Attraction> gpsAttractions();

    VisitedLocation gpsUserLocation(UUID userId);

}

