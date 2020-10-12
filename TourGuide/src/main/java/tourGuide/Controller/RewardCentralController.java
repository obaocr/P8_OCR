package tourGuide.Controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tourGuide.Model.RewardPointsMapper;
import tourGuide.Proxies.RewardProxy;

import java.util.UUID;

@RestController
public class RewardCentralController {

    @Autowired
    private RewardProxy rewardProxy;

    private Logger logger = LoggerFactory.getLogger(RewardCentralController.class);

    @GetMapping("/attractionrewardpoints")
    @JsonProperty("RewardPointsMapper")
    public RewardPointsMapper getAttractionRewardPoints(@RequestParam String attractionId, @RequestParam String userId) {
        logger.debug("attractionrewardpoints");
        RewardPointsMapper rewardPointsMapper = rewardProxy.getAttractionRewardPoints(attractionId, userId);
        return rewardPointsMapper;
    }

}
