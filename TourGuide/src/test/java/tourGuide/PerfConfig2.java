package tourGuide;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tourGuide.service.GpsProxyService;
import tourGuide.service.GpsProxyServiceImpl;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;

@Configuration
@EnableFeignClients
public class PerfConfig2 {

    @TestConfiguration
    static class GpsTestsContextConfiguration {

        @Bean
        public GpsProxyService getGpsService() {
            return new GpsProxyServiceImpl();
        }

        @Bean
        public RewardsService getRewardsService() {
            return new RewardsService();
        }

        @Bean
        public TourGuideService getTourGuideService() {
            return new TourGuideService(getGpsService(), getRewardsService());
        }

    }


}
