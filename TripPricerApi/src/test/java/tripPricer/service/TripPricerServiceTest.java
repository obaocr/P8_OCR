package tripPricer.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tripPricer.Provider;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
public class TripPricerServiceTest {

    @TestConfiguration
    static class TripPricerTestsContextConfiguration {

        @Bean
        public TripPricerService tripPricerService() {
            return new TripPricerServiceImpl();
        }

    }

    @Autowired
    TripPricerService tripPricerService;

    @Test
    void getTripPrice() {
        UUID attractionId = UUID.randomUUID();
        List<Provider> providers = tripPricerService.getPrice("key",attractionId,2,2,2,99);
        assertTrue(providers.size() > 0);
        assertTrue(!providers.get(0).name.equals(""));
    }

}
