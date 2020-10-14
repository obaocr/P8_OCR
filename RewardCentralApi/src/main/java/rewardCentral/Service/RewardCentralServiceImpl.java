package rewardCentral.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import rewardCentral.RewardCentral;

import java.util.Locale;
import java.util.UUID;

/**
 * Service layer for Rewards
 */
@Service
public class RewardCentralServiceImpl implements RewardCentralService {

    private Logger logger = LoggerFactory.getLogger(RewardCentralServiceImpl.class);
    private final RewardCentral rewardCentral;

    public RewardCentralServiceImpl() {
        logger.debug("constructor RewardCentralServiceImpl");
        Locale.setDefault(Locale.US);
        this.rewardCentral = new RewardCentral();
    }

    public int getAttractionRewardPoints(UUID attractionId, UUID userId) {
        logger.debug("getAttractionRewardPoints");
        return rewardCentral.getAttractionRewardPoints(attractionId, userId);
    }

}
