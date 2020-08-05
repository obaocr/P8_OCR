package tourGuide.Model;

import tourGuide.user.UserPreferences;

public class UserPrefResponse {
        private String userName;
        private UserPreferences userPreferences;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UserPreferences getUserPreferences() {
        return userPreferences;
    }

    public void setUserPreferences(UserPreferences userPreferences) {
        this.userPreferences = userPreferences;
    }
}
