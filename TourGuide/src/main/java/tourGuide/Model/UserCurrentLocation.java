package tourGuide.Model;

import gpsUtil.location.Location;

/**
 * Model response for User location
 */
public class UserCurrentLocation {

    private String userName;
    private Location location;

    /**
     * Constructor for UserCurrentLocation
     * @param userName
     * @param location
     */
    public UserCurrentLocation(String userName, Location location) {
        this.userName = userName;
        this.location = location;
    }

}


