import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import tourGuide.service.GpsProxyService;
import tourGuide.service.GpsProxyServiceImpl;
import tourGuide.service.RewardsService;

@Profile("testperf")
@Configuration
public class PerfConfig {


    @Bean
    public GpsProxyService getGpsService() {
        return new GpsProxyServiceImpl();
    }

    @Bean
    public RewardsService getRewardsService() {

        return new RewardsService();
    }

}
