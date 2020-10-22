package model;

import org.javamoney.moneta.Money;
import org.junit.Test;
import tourGuide.user.User;
import tourGuide.user.UserPreferences;

import javax.money.Monetary;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

public class TestUser {

    @Test
    public void testUserPreference () {
        UserPreferences userPreferences = new UserPreferences();
        userPreferences.setNumberOfChildren(1);
        userPreferences.setNumberOfAdults(1);
        userPreferences.setTicketQuantity(5);
        userPreferences.setTripDuration(10);
        userPreferences.setAttractionProximity(50);
        userPreferences.setCurrency(Monetary.getCurrency("USD"));
        userPreferences.setLowerPricePoint(Money.of(50, Monetary.getCurrency("USD")));
        userPreferences.setHighPricePoint(Money.of(1000, Monetary.getCurrency("USD")));
        assertTrue(userPreferences.getAttractionProximity() == 50);
        assertTrue(userPreferences.getNumberOfAdults() == 1);
        assertTrue(userPreferences.getNumberOfChildren() == 1);
        assertTrue(userPreferences.getTicketQuantity() == 5);
        assertTrue(userPreferences.getTripDuration() == 10);
    }

    @Test
    public void testUser () {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "jon", "000", "jon@tourGuide.com");
        user.setPhoneNumber("0102030405");
        user.setEmailAddress("test@test.fr");

        UserPreferences userPreferences = new UserPreferences();
        userPreferences.setNumberOfChildren(1);
        userPreferences.setNumberOfAdults(1);
        userPreferences.setTicketQuantity(5);
        userPreferences.setTripDuration(10);
        userPreferences.setAttractionProximity(50);
        userPreferences.setCurrency(Monetary.getCurrency("USD"));
        userPreferences.setLowerPricePoint(Money.of(50, Monetary.getCurrency("USD")));
        userPreferences.setHighPricePoint(Money.of(1000, Monetary.getCurrency("USD")));
        user.setUserPreferences(userPreferences);

        assertTrue(user.getEmailAddress() == "test@test.fr");
        assertTrue(user.getPhoneNumber() == "0102030405");
        assertTrue(user.getUserName() == "jon");
        assertTrue(user.getUserId() == userId);
    }


}
