package tourGuide;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.*;

@Configuration
@EnableFeignClients
public class PerfConfig {

    /*static {
        InternalTestHelper.setInternalUserNumber(500);
    }
     */

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
        public TripPricerService getTripPricerService() {
            return new TripPricerServiceImpl();
        }

        @Bean
        public TourGuideService getTourGuideService() {
            return new TourGuideService(getGpsService(), getRewardsService(), getTripPricerService());
        }

    }

}
