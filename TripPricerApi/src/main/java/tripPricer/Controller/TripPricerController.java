package tripPricer.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tripPricer.Provider;
import tripPricer.service.TripPricerService;

import java.util.List;
import java.util.UUID;

@RestController
public class TripPricerController {

    @Autowired
    TripPricerService tripPricerService;

    private Logger logger = LoggerFactory.getLogger(TripPricerController.class);

    @GetMapping("/")
    public String tripPricerHome() {
        logger.debug("tripPricerHome");
        return  "P8 tripPricerHome";
    }

    @GetMapping("/tripprice")
    public List<Provider> tripPrice(@RequestParam String apiKey, @RequestParam UUID attractionId, @RequestParam int adults
            , @RequestParam int children, @RequestParam int nightsStay, @RequestParam int rewardsPoints) {
        return tripPricerService.getPrice(apiKey, attractionId, adults, children, nightsStay, rewardsPoints);
    }

}
