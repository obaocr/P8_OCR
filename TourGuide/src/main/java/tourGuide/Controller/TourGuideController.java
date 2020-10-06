package tourGuide.Controller;

import com.jsoniter.output.JsonStream;
import gpsUtil.location.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tourGuide.Model.AttractionResponseDTO;
import tourGuide.Model.UserPreferencesDTO;
import tourGuide.service.TourGuideService;
import tourGuide.service.UserService;
import tourGuide.user.User;
import tourGuide.user.UserPreferences;
import tripPricer.Provider;

import javax.validation.Valid;
import java.util.List;

@RestController
public class TourGuideController {

    private Logger logger = LoggerFactory.getLogger(TourGuideController.class);

    @Autowired
    TourGuideService tourGuideService;

    @Autowired
    UserService userService;

    @GetMapping("/")
    public String index() {
        return "Greetings from TourGuide! OCR P8 by OBA";
    }

    @GetMapping("/location")
    public String getLocation(@RequestParam String userName) {
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
    public List<AttractionResponseDTO> getNearbyAttractions(@RequestParam String userName) {
        logger.debug("getNearbyAttractions");
        return tourGuideService.getNearByAttractions(userName);
    }

    @GetMapping("/rewards")
    // TODO A voir pour éventuellement retourner l'objet
    public String getRewards(@RequestParam String userName) {
        logger.debug("getRewards");
        return JsonStream.serialize(tourGuideService.getUserRewards(tourGuideService.getUser(userName)));
    }

    @GetMapping("/allcurrentlocations")
    public String getAllCurrentLocations() {
        // TODO OBA (Finished, to be validated)  - getAllUsersCurrentLocation
        // TODO: Get a list of every user's most recent location as JSON
        // TODO OBA - faire les tests
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