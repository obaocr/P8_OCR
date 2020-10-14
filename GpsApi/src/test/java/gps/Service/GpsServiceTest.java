package gps.Service;

import gps.Model.AttractionMapper;
import gps.Model.VisitedLocationMapper;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
public class GpsServiceTest {

    @TestConfiguration
    static class GpsTestsContextConfiguration {

        @Bean
        public GpsService gpsService() {
            return new GpsServiceImpl();
        }

    }

    @Autowired
    GpsService gpsService;

    @Test
    void getAttractions() {
        List<AttractionMapper> attractionMappers = gpsService.getAttractions();
        assertTrue(attractionMappers.size() > 0);
    }


    @Test
    void getUserLocation() {
        VisitedLocationMapper visitedLocationMapper = gpsService.getUserLocation(UUID.randomUUID());
        assertTrue(!(visitedLocationMapper.getLocation()==null));
    }

}
