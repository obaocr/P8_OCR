package tourGuide.Model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;

/**
 * Model for AttractionResponse / SVC nearByAttraction
 */
public class AttractionResponseDTO {

    private String attractionId;
    private String attractionName;
    private String city;
    private String state;
    private double longitude;
    private double latitude;
    private double distanceWithCurrLoc;
    private int rewardsPoints;


    public AttractionResponseDTO() {
    }

    public void setAttractionId(String attractionId) {
        this.attractionId = attractionId;
    }

    public String getAttractionName() {
        return attractionName;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public int getRewardsPoints() {
        return rewardsPoints;
    }

    public String getAttractionId() {

        return attractionId;
    }

    public void setAttractionName(String attractionName) {

        this.attractionName = attractionName;
    }

    public void setCity(String city) {

        this.city = city;
    }

    public void setState(String state) {

        this.state = state;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {

        this.latitude = latitude;
    }

    public double getDistanceWithCurrLoc() {

        return distanceWithCurrLoc;
    }

    public void setDistanceWithCurrLoc(double distanceWithCurrLoc) {

        this.distanceWithCurrLoc = distanceWithCurrLoc;
    }

    public void setRewardsPoints(int rewardsPoints) {

        this.rewardsPoints = rewardsPoints;
    }

    @Override
    public String toString() {
        return "AttractionResponse{" +
                "attractionId=" + attractionId +
                ", attractionName='" + attractionName + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", distanceWithCurrLoc=" + distanceWithCurrLoc +
                ", rewardsPoints=" + rewardsPoints +
                '}';
    }
}
