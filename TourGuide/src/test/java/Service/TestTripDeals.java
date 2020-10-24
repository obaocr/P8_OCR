package Service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tourGuide.Model.Provider;
import tourGuide.PerfConfig;
import tourGuide.Proxies.GpsProxy;
import tourGuide.Proxies.RewardProxy;
import tourGuide.Proxies.TripPricerProxy;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.*;
import tourGuide.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(SpringExtension.class)
public class TestTripDeals {


    @TestConfiguration
    static class GpsTestsContextConfiguration {

        @Bean
        public GpsProxyService gpsService() {
            return new GpsProxyServiceImpl();
        }

        @Bean
        public RewardsService gpsRewardsService() {
            return new RewardsService();
        }

        @Bean
        public TripPricerService getTripPricerService() { return new TripPricerServiceImpl(); }

    }

    @Autowired
    private GpsProxyService gpsProxyService;

    @Autowired
    private RewardsService rewardsService;

    @Autowired
    private TripPricerService tripPricerService;

    @MockBean
    private GpsProxy gpsProxy;

    @MockBean
    private RewardProxy rewardProxy;

    @MockBean
    private TripPricerProxy tripPricerProxy;

    @Test
    public void getTripDeals() {
        InternalTestHelper.setInternalUserNumber(0);
        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

        List<Provider> providersMock = new ArrayList<>();
        for (int i=0; i< 5; i++) {
            Provider provider = new Provider(UUID.randomUUID(), "test"+i, 10.0);
            providersMock.add(provider);
        }

        Mockito.when(tripPricerProxy.getTripPrice(anyString(), anyString(), anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(providersMock);

        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(gpsProxyService, rewardsService, tripPricerService);
        tourGuideService.addUser(user);

        List<Provider> providers = tourGuideService.getTripDeals(user);
        tourGuideService.tracker.stopTracking();
        assertEquals(5, providers.size());
    }
}
