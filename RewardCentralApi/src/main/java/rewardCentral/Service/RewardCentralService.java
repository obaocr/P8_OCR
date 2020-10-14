package rewardCentral.Service;

import java.util.UUID;

public interface RewardCentralService {

    int getAttractionRewardPoints(UUID attractionId, UUID userId);

}
