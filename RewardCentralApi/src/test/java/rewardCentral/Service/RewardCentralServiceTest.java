package rewardCentral.Service;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RewardCentralServiceTest {

    @Test
    void getAttractionRewardPoints() {
        RewardCentralService rewardCentralService = new RewardCentralServiceImpl();
        Integer  points = rewardCentralService.getAttractionRewardPoints(UUID.randomUUID(), UUID.randomUUID());
        assertTrue(points >= 0);
    }

}
