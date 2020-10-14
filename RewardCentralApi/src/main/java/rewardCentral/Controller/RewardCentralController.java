package rewardCentral.Controller;

import com.jsoniter.output.JsonStream;
import rewardCentral.Model.RewardPointsMapper;
import rewardCentral.Service.RewardCentralService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Controller for RewardService
 */
@RestController
public class RewardCentralController {

    @Autowired
    RewardCentralService rewardCentralService;

    private Logger logger = LoggerFactory.getLogger(RewardCentralController.class);

    @GetMapping("/")
    public String rewardCentral() {
        logger.debug("gpsHome");
        return  "P8 RewardCentral Home";
    }

    @GetMapping("/attractionrewardpoints")
    public RewardPointsMapper getAttractionRewardPoints(@RequestParam String attractionId, @RequestParam String userId) {
        logger.info("getAttractionRewardPoints Api");
        Integer points = rewardCentralService.getAttractionRewardPoints(UUID.fromString(attractionId), UUID.fromString(userId));
        RewardPointsMapper rewardPointsMapper = new RewardPointsMapper(points);
        return rewardPointsMapper;
    }

}
