package tourGuide.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;

public class AttractionResponse {

    @JsonIgnore
    private UUID attractionId;
    private String attractionName;
    private String city;
    private String state;
    private double longitude;
    private double latitude;
    private double distanceWithCurrLoc;
    private int rewardsPoints;


    public AttractionResponse() {
    }

    public UUID getAttractionId() {
        return attractionId;
    }

    public void setAttractionId(UUID attractionId) {
        this.attractionId = attractionId;
    }

    public String getAttractionName() {
        return attractionName;
    }

    public void setAttractionName(String attractionName) {
        this.attractionName = attractionName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
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

    public int getRewardsPoints() {
        return rewardsPoints;
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
