package tourGuide.service;

import tripPricer.Provider;
import tripPricer.TripPricer;

import java.util.List;
import java.util.UUID;

public interface TripPricerService {

    TripPricer getTripPricer();
    List<Provider> getPrice(String apiKey, UUID attractionId, int adults, int children, int nightsStay, int rewardsPoints);
}
