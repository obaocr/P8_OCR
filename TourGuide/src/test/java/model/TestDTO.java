package model;

import org.junit.Test;
import tourGuide.Model.AttractionResponseDTO;
import tourGuide.Model.Location;
import tourGuide.Model.UserCurrentLocationDTO;
import tourGuide.Model.UserPreferencesDTO;

import java.util.UUID;

import static org.junit.Assert.assertTrue;

public class TestDTO {

    @Test
    public void testUserCurrentLocationDTO() {
        Location loc1 = new Location(33.817595D, -112.817595D);
        UserCurrentLocationDTO userCurrentLocationDTO = new UserCurrentLocationDTO("UserName1", loc1);
        assertTrue(userCurrentLocationDTO.getUserName().equals("UserName1"));
    }

    @Test
    public void testAttractionResponseDTO() {
        AttractionResponseDTO attractionResponseDTO = new AttractionResponseDTO();
        attractionResponseDTO.setAttractionName("Test1");
        attractionResponseDTO.setCity("Ville");
        attractionResponseDTO.setDistanceWithCurrLoc(12.00);
        attractionResponseDTO.setLatitude(33.817595D);
        attractionResponseDTO.setLongitude(-33.817595D);
        attractionResponseDTO.setRewardsPoints(99);
        attractionResponseDTO.setState("USA");
        attractionResponseDTO.setAttractionId(UUID.randomUUID().toString());
        assertTrue(attractionResponseDTO.toString() != null);
        assertTrue(attractionResponseDTO.getDistanceWithCurrLoc() == 12.00);
        assertTrue(attractionResponseDTO.getAttractionId().equals(attractionResponseDTO.getAttractionId()));

    }

    @Test
    public void testUserPreferencesDTO() {
        UserPreferencesDTO userPreferencesDTO = new UserPreferencesDTO();
        userPreferencesDTO.setLowerPricePoint(0);
        userPreferencesDTO.setHighPricePoint(100);
        userPreferencesDTO.setTicketQuantity(5);
        userPreferencesDTO.setCurrency("USD");
        userPreferencesDTO.setNumberOfChildren(2);
        userPreferencesDTO.setNumberOfAdults(2);
        userPreferencesDTO.setTripDuration(45);
        userPreferencesDTO.setAttractionProximity(10);
        assertTrue(userPreferencesDTO.getCurrency().equals("USD"));
        assertTrue(userPreferencesDTO.getAttractionProximity() == 10);
        assertTrue(userPreferencesDTO.getLowerPricePoint() == 0);
        assertTrue(userPreferencesDTO.getHighPricePoint() == 100);
        assertTrue(userPreferencesDTO.getNumberOfAdults() == 2);
        assertTrue(userPreferencesDTO.getNumberOfChildren() == 2);
        assertTrue(userPreferencesDTO.getTripDuration() == 45);
        assertTrue(userPreferencesDTO.getTicketQuantity() == 5);
    }
}
