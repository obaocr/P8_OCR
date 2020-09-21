package tourGuide.service;

import gpsUtil.GpsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import rewardCentral.RewardCentral;

import java.util.UUID;

@Service
public class RewardCentralServiceImpl implements RewardCentralService {

    private Logger logger = LoggerFactory.getLogger(RewardCentralServiceImpl.class);
    private final RewardCentral rewardCentral;

    public RewardCentralServiceImpl(GpsUtil gpsUtil) {
        logger.debug("constructor RewardCentralServiceImpl");
        this.rewardCentral = new RewardCentral();
    }

    public int getAttractionRewardPoints(UUID attractionId, UUID userId) {
        return rewardCentral.getAttractionRewardPoints(attractionId, userId);
    }

}
