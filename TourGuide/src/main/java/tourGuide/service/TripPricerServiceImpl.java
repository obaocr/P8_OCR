package tourGuide.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import tourGuide.Model.Provider;
import tourGuide.Proxies.TripPricerProxy;

import java.util.List;

/**
 * Service layer for TripPricer
 */
public class TripPricerServiceImpl implements TripPricerService {

    private Logger logger = LoggerFactory.getLogger(TripPricerServiceImpl.class);

    @Autowired
    private TripPricerProxy tripPricerProxy;

    @Override
    public List<Provider> getPrice(String apiKey, String attractionId, Integer adults, Integer children, Integer nightsStay, Integer rewardsPoints) {
        logger.debug("getPrice");
        return tripPricerProxy.getTripPrice(apiKey, attractionId.toString(), adults, children, nightsStay, rewardsPoints);
    }

}
