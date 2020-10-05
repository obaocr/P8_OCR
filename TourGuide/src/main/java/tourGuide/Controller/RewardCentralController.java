package tourGuide.Controller;

import com.jsoniter.output.JsonStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tourGuide.service.RewardCentralService;

import java.util.UUID;

@RestController
public class RewardCentralController {

    @Autowired
    RewardCentralService rewardCentralService;

    private Logger logger = LoggerFactory.getLogger(RewardCentralController.class);

    @GetMapping("/attractionrewardpoints")
    public String getAttractionRewardPoints(@RequestParam UUID attractionId, @RequestParam UUID userId) {
        Integer points = rewardCentralService.getAttractionRewardPoints(attractionId, userId);
        String response = "{ \"points\" :" + points + " }";
        logger.debug("response " + response);
        return (response);
    }

}
