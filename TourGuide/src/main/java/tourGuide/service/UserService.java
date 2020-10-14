package tourGuide.service;

import tourGuide.Model.UserPreferencesDTO;
import tourGuide.user.User;
import tourGuide.user.UserPreferences;

public interface UserService {

    public UserPreferences getUserPreferences(User user);
    public UserPreferencesDTO getUserPreferencesSummary(User user);
    public UserPreferences setUserPreferences(User user, UserPreferencesDTO userPreferencesDTO);
}
