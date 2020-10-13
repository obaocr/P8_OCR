package tourGuide.Model;

import java.util.UUID;

/**
 * Model for Attraction  : response of GpsService
 */
public class AttractionMapper {

    private String attractionName;
    private String city;
    private String state;
    private UUID attractionId;
    private double longitude;
    private double latitude;

    public AttractionMapper() {
    }

    public AttractionMapper(String attractionName, String city, String state, UUID attractionId, double longitude, double latitude) {
        this.attractionName = attractionName;
        this.city = city;
        this.state = state;
        this.attractionId = attractionId;
        this.longitude = longitude;
        this.latitude = latitude;
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

    public UUID getAttractionId() {
        return attractionId;
    }

    public void setAttractionId(UUID attractionId) {
        this.attractionId = attractionId;
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
}
