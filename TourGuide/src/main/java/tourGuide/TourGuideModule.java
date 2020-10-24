package tourGuide;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tourGuide.Proxies.TripPricerProxy;
import tourGuide.service.*;

@Configuration
public class TourGuideModule {

    @Bean
    public GpsProxyService getGpsService() {
        return new GpsProxyServiceImpl();
    }

    @Bean
    public RewardsService getRewardsService() {
        return new RewardsService();
    }

    @Bean
    public TripPricerService getTripPricerService() { return new TripPricerServiceImpl();
    }

}
