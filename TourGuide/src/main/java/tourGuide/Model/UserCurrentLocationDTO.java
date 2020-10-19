package tourGuide.Model;

/**
 * Model response for User location
 */
public class UserCurrentLocationDTO {

    private String userName;
    private Location location;

    /**
     * Constructor for UserCurrentLocation
     * @param userName
     * @param location
     */
    public UserCurrentLocationDTO(String userName, Location location) {
        this.userName = userName;
        this.location = location;
    }

    public String getUserName() {
        return userName;
    }
}


