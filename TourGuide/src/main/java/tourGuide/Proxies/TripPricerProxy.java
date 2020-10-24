package tourGuide.Proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tourGuide.Model.Provider;

import java.util.List;

/**
 * Proxy for TripPricer / micro service
 */
@FeignClient(name = "microservice-trippricer", url = "localhost:8048")
public interface TripPricerProxy {

    @GetMapping(value = "/tripprice")
    List<Provider> getTripPrice(@RequestParam String apiKey, @RequestParam String attractionId, @RequestParam Integer adults
            , @RequestParam Integer children, @RequestParam Integer nightsStay, @RequestParam Integer rewardsPoints);

}
