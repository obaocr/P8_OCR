package tourGuide;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tourGuide.Proxies.GpsProxy;
import tourGuide.service.GpsProxyService;
import tourGuide.service.GpsProxyServiceImpl;
import tourGuide.service.RewardsService;

// TODO éventuellement retravailler pour n'avoir que Rewardservice en injection, et que des SVC en injection ...
// TODO Gérer le TripProcer en Bean et non pas en New ..

@Configuration
public class TourGuideModule {

    @Bean
    public GpsProxyService getGpsService() {
        return new GpsProxyServiceImpl();
    }

    @Bean
    public RewardsService getRewardsService() {

        return new RewardsService(getGpsService());
    }

}
