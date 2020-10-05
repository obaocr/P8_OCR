package tourGuide.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tourGuide.Model.UserPreferencesDTO;
import tourGuide.service.TourGuideService;
import tourGuide.service.UserService;
import tourGuide.user.User;
import tourGuide.user.UserPreferences;

import javax.validation.Valid;

@RestController
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    TourGuideService tourGuideService;

    @Autowired
    UserService userService;

    @GetMapping(value = "/user")
    private User getUser(String userName) {
        logger.debug("getUser");
        return tourGuideService.getUser(userName);
    }

    @GetMapping("/userpreferencessummary")
    public UserPreferencesDTO getUserPreferencesSummary(@RequestParam String userName) {
        logger.debug("getUserPreferences");
        return userService.getUserPreferencesSummary(tourGuideService.getUser(userName));
    }

    // TODO  à voir pour faire le checkInput ????
    // TODO faire comme le P7 on peut ajouter un biding result en input et l'utiliser pour gérer .. sinon voir P5 Exceptions...
    @PutMapping(value = "/userpreferences/{userName}")
    public UserPreferences setUserPreferences(@PathVariable("userName") String userName, @RequestBody @Valid UserPreferencesDTO userPreferencesDTO) {
        logger.debug("Update UserPreferences for a user");
        return userService.setUserPreferences(tourGuideService.getUser(userName), userPreferencesDTO);
    }

}
