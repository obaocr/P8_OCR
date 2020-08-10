package tourGuide;

import com.jsoniter.output.JsonStream;
import gpsUtil.location.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rewardCentral.RewardCentral;
import tourGuide.Model.UserPreferencesDTO;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;
import tourGuide.user.UserPreferences;
import tripPricer.Provider;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
public class TourGuideController {

    private Logger logger = LoggerFactory.getLogger(TourGuideController.class);

    @Autowired
    TourGuideService tourGuideService;

    @RequestMapping("/")
    public String index() {
        return "Greetings from TourGuide! OCR P8 by OBA";
    }

    @RequestMapping("/getLocation")
    public String getLocation(@RequestParam String userName) {
        logger.debug("getLocation");
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
        logger.debug("getNearbyAttractions");
        return JsonStream.serialize(tourGuideService.getNearByAttractions(userName));
    }


    @RequestMapping("/getRewards")
    // TODO A voir pour éventuellement retourner l'objet
    public String getRewards(@RequestParam String userName) {
        logger.debug("getRewards");
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
        logger.debug("getAllCurrentLocations");
        return JsonStream.serialize(tourGuideService.getAllUsersCurrentLocation());
    }

    @RequestMapping("/getTripDeals")
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

    // TODO OBA
    @RequestMapping("/getUserPreferences")
    public UserPreferences getUserPreferences(@RequestParam String userName) {
        logger.debug("getUserPreferences");
        return tourGuideService.getUserPreferences(userName);
    }

    // TODO OBA ========> à enlever ...
    @RequestMapping("/TestAsynchrone")
    public boolean TestAsynchrone(@RequestParam String userName) {
        logger.debug("getUserPreferences");
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        RewardCentral rewardCentral = new RewardCentral();
        System.out.println("==========> synchrone resultat appel reward... : " + rewardCentral.getAttractionRewardPoints(uuid1, uuid2));

        // Tests 1 asycnhrone
        int number = 20;
        Thread newThread = new Thread(() -> {
            try {
                //Thread.sleep(5000);
                System.out.println("==========> test asycnc Reward ... : " + userName);
                logger.debug("avant appel asynchrone svc");
                System.out.println("==========> resultat appel reward... : " + rewardCentral.getAttractionRewardPoints(uuid1, uuid2));
            } catch (Exception e) {
                System.out.println("==========> Exception = " + e.toString());;
            }
        });
        // Appel en asycnhrone ...
        newThread.start();
        // C'est affiché avant la fin...
        System.out.println("==========> Test 1 / Après newThread.start() mais doit s'afficher avant...");

        // Test 2
        /*CompletableFuture<Long> completableFuture = CompletableFuture.supplyAsync(() -> factorial(number));
        while (!completableFuture.isDone()) {
            System.out.println("CompletableFuture is not finished yet...");
        }
        long result = completableFuture.get();*/
        return true;
    }

    // TODO OBA
    @RequestMapping("/getUserPreferencesSummary")
    public UserPreferencesDTO getUserPreferencesSummary(@RequestParam String userName) {
        logger.debug("getUserPreferences");
        return tourGuideService.getUserPreferencesSummary(userName);
    }

    // TODO faire le set des UserPref ...
    // TODO faire un put avec ID du user et les données en body, utiliser @Valid
    // TODO  à voir pour faire le checkInput ????
    @PutMapping(value = "/setUserPreferences/{userName}")
    public UserPreferences setUserPreferences(@PathVariable("userName") String userName, @RequestBody UserPreferencesDTO userPreferencesDTO) {
        logger.debug("Update UserPreferences for a user");
        //checkInput(userPreferencesDTO);
        return tourGuideService.setUserPreferences(userName, userPreferencesDTO);
    }


    private User getUser(String userName) {
        logger.debug("getUser");
        return tourGuideService.getUser(userName);
    }


}