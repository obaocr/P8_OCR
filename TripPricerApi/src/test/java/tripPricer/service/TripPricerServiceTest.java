package tripPricer.service;

import org.junit.jupiter.api.Test;
import tripPricer.Provider;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TripPricerServiceTest {

    @Test
    void getTripPrice() {
        TripPricerService tripPricerService = new TripPricerServiceImpl();
        UUID attractionId = UUID.randomUUID();
        List<Provider> providers = tripPricerService.getPrice("key",attractionId,2,2,2,99);
        assertTrue(providers.size() > 0);
        assertTrue(!providers.get(0).name.equals(""));
    }

}
