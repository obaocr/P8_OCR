package tourGuide.Controller;

import com.jsoniter.output.JsonStream;
import gpsUtil.location.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tourGuide.Model.UserPreferencesDTO;
import tourGuide.service.TourGuideService;
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

    @GetMapping("/")
    public String index() {
        return "Greetings from TourGuide! OCR P8 by OBA";
    }

    @GetMapping("/getLocation")
    public String getLocation(@RequestParam String userName) {
        logger.debug("getLocation");
        VisitedLocation visitedLocation = tourGuideService.getUserLocation(getUser(userName));
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
    @GetMapping("/getNearbyAttractions")
    public String getNearbyAttractions(@RequestParam String userName) {
        logger.debug("getNearbyAttractions");
        String response = JsonStream.serialize(tourGuideService.getNearByAttractions(userName));
        return response;
    }

    @GetMapping("/getRewards")
    // TODO A voir pour éventuellement retourner l'objet
    public String getRewards(@RequestParam String userName) {
        logger.debug("getRewards");
        return JsonStream.serialize(tourGuideService.getUserRewards(getUser(userName)));
    }

    @GetMapping("/getAllCurrentLocations")
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

    @GetMapping("/getTripDeals")
    public String getTripDeals(@RequestParam String userName) {
        logger.debug("getTripDeals");
        List<Provider> providers = tourGuideService.getTripDeals(getUser(userName));
        return JsonStream.serialize(providers);
    }

    // TODO nouveeau endpoint get et put pour mise à jour des preferences
    // TODO avoir tous les champs sinon lever une exception ,annotation @valid et mettre @notnull dans le modele (passer en Integer)...
    // TODO pour la réponse filter que les champs voulus des objets comme Money... à voir
    // TODO à voir pour mettre dans une classe dédié UserController ?
    // TODO Gérer les codes HTTP si KO ...
    @GetMapping("/getUserPreferences")
    public UserPreferences getUserPreferences(@RequestParam String userName) {
        logger.debug("getUserPreferences");
        return tourGuideService.getUserPreferences(userName);
    }

    // TODO OBA
    @GetMapping("/getUserPreferencesSummary")
    public UserPreferencesDTO getUserPreferencesSummary(@RequestParam String userName) {
        logger.debug("getUserPreferences");
        return tourGuideService.getUserPreferencesSummary(userName);
    }

    // TODO  à voir pour faire le checkInput ????
    // TODO faire comme le P7 on peut ajouter un biding result en input et l'utiliser pour gérer .. sinon voir P5 Exceptions...
    @PutMapping(value = "/setUserPreferences/{userName}")
    public UserPreferences setUserPreferences(@PathVariable("userName") String userName, @RequestBody @Valid UserPreferencesDTO userPreferencesDTO) {
        logger.debug("Update UserPreferences for a user");
        //checkInput(userPreferencesDTO);
        return tourGuideService.setUserPreferences(userName, userPreferencesDTO);
    }

    private User getUser(String userName) {
        logger.debug("getUser");
        return tourGuideService.getUser(userName);
    }


}