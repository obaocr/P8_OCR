package tourGuide;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tourGuide.Proxies.GpsProxy;
import tourGuide.service.GpsProxyService;
import tourGuide.service.GpsProxyServiceImpl;
import tourGuide.service.RewardsService;

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

}
