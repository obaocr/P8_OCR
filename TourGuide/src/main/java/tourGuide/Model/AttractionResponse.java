package tourGuide.Model;

import java.util.UUID;

public class AttractionResponse {
    private String attractionName;
    private String city;
    private String state;
    private UUID attractionId;
    private double longitude;
    private double latitude;
    private double distanceWithCurrLoc;
    private int rewardsPoints;


    public AttractionResponse(String attractionName, String city, String state, UUID attractionId, double longitude, double latitude, double distanceWithCurrLoc, int rewardsPoints) {
        this.attractionName = attractionName;
        this.city = city;
        this.state = state;
        this.attractionId = attractionId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.distanceWithCurrLoc = distanceWithCurrLoc;
        this.rewardsPoints = rewardsPoints;
    }
}
