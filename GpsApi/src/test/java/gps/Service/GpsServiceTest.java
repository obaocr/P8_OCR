package gps.Service;

import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class GpsServiceTest {

    @Test
    void getAttractions() {
        GpsService gpsService = new GpsServiceImpl();
        List<Attraction> attractions = gpsService.getAttractions();
        assertTrue(attractions.size() > 0);
    }


    @Test
    void getUserLocation() {
        GpsService gpsService = new GpsServiceImpl();
        VisitedLocation visitedLocation = gpsService.getUserLocation(UUID.randomUUID());
        assertTrue(!visitedLocation.location.equals(""));
    }

}
