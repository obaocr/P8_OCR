package gps.Service;

import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;

import java.util.List;
import java.util.UUID;

public interface GpsService {

    List<Attraction> getAttractions();

    public VisitedLocation getUserLocation(UUID userID);
}
