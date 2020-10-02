package rewardCentral.Service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
public class RewardCentralServiceTest {

    @TestConfiguration
    static class RewardCentralTestsContextConfiguration {

        @Bean
        public RewardCentralService rewardCentralService() {
            return new RewardCentralServiceImpl();
        }

    }

    @Autowired
    RewardCentralService rewardCentralService;

    @Test
    void getAttractionRewardPoints() {
        Integer  points = rewardCentralService.getAttractionRewardPoints(UUID.randomUUID(), UUID.randomUUID());
        assertTrue(points >= 0);
    }

}
