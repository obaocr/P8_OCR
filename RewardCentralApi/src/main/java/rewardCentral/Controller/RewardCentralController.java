package rewardCentral.Controller;

import rewardCentral.Service.RewardCentralService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

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

    @GetMapping("/getAttractionRewardPoints")
    public String getAttractionRewardPoints(@RequestParam UUID attractionId, @RequestParam UUID userId) {
        logger.debug("getAttractionRewardPoints");
        Integer points = rewardCentralService.getAttractionRewardPoints(attractionId, userId);
        String response = "{ \"points\" :" + points + " }";
        return (response);
    }

}
