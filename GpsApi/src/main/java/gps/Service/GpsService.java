package gps.Service;

import gps.Model.AttractionMapper;
import gps.Model.VisitedLocationMapper;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;

import java.util.List;
import java.util.UUID;

public interface GpsService {

    List<AttractionMapper> getAttractions();
    VisitedLocationMapper getUserLocation(UUID userID);
}
