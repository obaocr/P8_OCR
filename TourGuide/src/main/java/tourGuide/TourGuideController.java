package tourGuide;

import com.jsoniter.output.JsonStream;
import gpsUtil.location.VisitedLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;
import tourGuide.user.UserPreferences;
import tripPricer.Provider;

import java.util.List;

@RestController
public class TourGuideController {

    @Autowired
    TourGuideService tourGuideService;

    @RequestMapping("/")
    public String index() {
        return "Greetings from TourGuide! OCR P8 by OBA";
    }

    @RequestMapping("/getLocation")
    public String getLocation(@RequestParam String userName) {
        VisitedLocation visitedLocation = tourGuideService.getUserLocation(getUser(userName));
        return JsonStream.serialize(visitedLocation.location);
    }

    // TODO: Change this method to no longer return a List of Attractions.
    // TODO : Rewards  à calculer...
    //  Instead: Get the closest five tourist attractions to the user - no matter how far away they are.
    //  Return a new JSON object that contains:
    // Name of Tourist attraction,
    // Tourist attractions lat/long,
    // The user's location lat/long,
    // The distance in miles between the user's location and each of the attractions.
    // The reward points for visiting each Attraction.
    //    Note: Attraction reward points can be gathered from RewardsCentral
    @RequestMapping("/getNearbyAttractions")
    public String getNearbyAttractions(@RequestParam String userName) {
        return JsonStream.serialize(tourGuideService.getNearByAttractions(userName));
    }


    @RequestMapping("/getRewards")
    // TODO A voir pour éventuellement retourner l'objet
    public String getRewards(@RequestParam String userName) {
        return JsonStream.serialize(tourGuideService.getUserRewards(getUser(userName)));
    }

    @RequestMapping("/getAllCurrentLocations")
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
        return JsonStream.serialize(tourGuideService.getAllUsersCurrentLocation());
    }

    @RequestMapping("/getTripDeals")
    public String getTripDeals(@RequestParam String userName) {
        List<Provider> providers = tourGuideService.getTripDeals(getUser(userName));
        return JsonStream.serialize(providers);
    }

    // TODO nouveeau endpoint get et put pour mise à jour des preferences
    // TODO avoir tous les champs sinon lever une exception ,annotation @valid et mettre @notnull dans le modele (passer en Integer)...
    // TODO pour la réponse filter que les champs voulus des objets comme Money... à voir
    // TODO à voir pour mettre dans une classe dédié UserController ?
    // TODO Gérer les codes HTTP si KO ...

    // TODO ca rend beaucoup d'informations ...
    @RequestMapping("/getUserPreferences")
    public UserPreferences getUserPreferences(@RequestParam String userName) {
        return tourGuideService.getUserPreferences(userName);
    }

    // TODO faire le set des UserPref ...
    // TODO faire un put avec ID du user et les données en body, utiliser @Valid

    private User getUser(String userName) {
        return tourGuideService.getUser(userName);
    }


}