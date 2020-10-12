package tourGuide.Proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tourGuide.Model.RewardPointsMapper;

import java.util.UUID;

@FeignClient(name = "microservice-reward", url = "localhost:8047")
public interface RewardProxy {

    @GetMapping(value = "/attractionrewardpoints")
    RewardPointsMapper getAttractionRewardPoints(@RequestParam String attractionId, @RequestParam String userId);

}
