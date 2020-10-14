package tourGuide.Controller;

import com.jsoniter.output.JsonStream;
import gpsUtil.location.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tourGuide.Model.AttractionResponseDTO;
import tourGuide.service.TourGuideService;
import tripPricer.Provider;

import java.util.List;

/**
 * TourGuideController
 * Main controller
 */
@RestController
public class TourGuideController {

    private Logger logger = LoggerFactory.getLogger(TourGuideController.class);

    @Autowired
    TourGuideService tourGuideService;

    @GetMapping("/")
    public String index() {
        return "Greetings from TourGuide! OCR P8 by OBA";
    }

    @GetMapping("/location")
    public String getLocation1(@RequestParam String userName) {
        logger.debug("getLocation");
        VisitedLocation visitedLocation = tourGuideService.getUserLocation(tourGuideService.getUser(userName));
        return JsonStream.serialize(visitedLocation.location);
    }

    // TODO: Change this method to no longer return a List of Attractions.
    //  Instead: Get the closest five tourist attractions to the user - no matter how far away they are.
    //  Return a new JSON object that contains:
    // Name of Tourist attraction,
    // Tourist attractions lat/long,
    // The user's location lat/long,
    // The distance in miles between the user's location and each of the attractions.
    // The reward points for visiting each Attraction.
    //    Note: Attraction reward points can be gathered from RewardsCentral

    @GetMapping("/nearbyattractions")
    public String  getNearbyAttractions(@RequestParam String userName) {
        logger.debug("getNearbyAttractions");
        List<AttractionResponseDTO> attractionResponseDTOS = tourGuideService.getNearByAttractions(userName);
        return JsonStream.serialize(attractionResponseDTOS);
    }

    @GetMapping("/rewards")
    public String getRewards(@RequestParam String userName) {
        logger.debug("getRewards");
        return JsonStream.serialize(tourGuideService.getUserRewards(tourGuideService.getUser(userName)));
    }

    @GetMapping("/allcurrentlocations")
    public String getAllCurrentLocations() {
        // TODO: Get a list of every user's most recent location as JSON
        //- Note: does not use gpsUtil to query for their current location,
        //        but rather gathers the user's current location from their stored location history.
        //
        // Return object should be the just a JSON mapping of userId to Locations similar to:
        //     {
        //        "019b04a9-067a-4c76-8817-ee75088c3822": {"longitude":-48.188821,"latitude":74.84371}
        //        ...
        //     }
        logger.debug("getAllCurrentLocations");
        return JsonStream.serialize(tourGuideService.getAllUsersCurrentLocation());
    }

    @GetMapping("/tripdeals")
    public String getTripDeals(@RequestParam String userName) {
        logger.debug("getTripDeals");
        List<Provider> providers = tourGuideService.getTripDeals(tourGuideService.getUser(userName));
        return JsonStream.serialize(providers);
    }

}