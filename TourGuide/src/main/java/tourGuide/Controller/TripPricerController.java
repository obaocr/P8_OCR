package tourGuide.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tripPricer.Provider;
import tripPricer.TripPricer;

import java.util.List;
import java.util.UUID;

@RestController
public class TripPricerController {

    private Logger logger = LoggerFactory.getLogger(GpsController.class);
    private TripPricer tripPricer;

    // TODO  à voir pour un bean ??? /
    public TripPricerController() {
        this.tripPricer = new TripPricer();
    }

    @GetMapping("/tripGetPrice")
    public List<Provider> tripGetPrice(@RequestParam String apiKey, @RequestParam UUID attractionId, @RequestParam int adults
            , @RequestParam int children, @RequestParam int nightsStay, @RequestParam int rewardsPoints) {
        return tripPricer.getPrice(apiKey, attractionId, adults, children, nightsStay, rewardsPoints);
    }
}
