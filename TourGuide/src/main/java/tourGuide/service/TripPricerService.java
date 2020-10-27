package tourGuide.service;


import tourGuide.Model.Provider;

import java.util.List;

public interface TripPricerService {

    List<Provider> getPrice(String apiKey, String attractionId, Integer adults, Integer children, Integer nightsStay, Integer rewardsPoints);
}
