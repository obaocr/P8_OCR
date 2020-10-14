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

/**
 * UserController for user object
 */
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

    @GetMapping("/userpreferences")
    public UserPreferences getUserPreferences(@RequestParam String userName) {
        logger.debug("getUserPreferences");
        return userService.getUserPreferences(tourGuideService.getUser(userName));
    }

    @GetMapping("/userpreferencessummary")
    public UserPreferencesDTO getUserPreferencesSummary(@RequestParam String userName) {
        logger.debug("getUserPreferences");
        return userService.getUserPreferencesSummary(tourGuideService.getUser(userName));
    }

    @PutMapping(value = "/userpreferences/{userName}")
    public UserPreferences setUserPreferences(@PathVariable("userName") String userName, @RequestBody @Valid UserPreferencesDTO userPreferencesDTO) {
        logger.debug("Update UserPreferences for a user");
        return userService.setUserPreferences(tourGuideService.getUser(userName), userPreferencesDTO);
    }

}
