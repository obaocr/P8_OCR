package tourGuide.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tripPricer.Provider;
import tripPricer.TripPricer;

import java.util.List;
import java.util.UUID;

// TODO non utilisé pour le moment ...

@Service
public class TripPricerService {

    private Logger logger = LoggerFactory.getLogger(TripPricerService.class);
    private final TripPricer tripPricer;

    public TripPricerService()
    {
        this.tripPricer = new TripPricer();
    }

    public TripPricer getTripPricer() {
        return tripPricer;
    }

    public List<Provider> getPrice(String apiKey, UUID attractionId, int adults, int children, int nightsStay, int rewardsPoints) {
        logger.debug("getPrice");
        return tripPricer.getPrice(apiKey, attractionId, adults, children, nightsStay, rewardsPoints);
    }

}
