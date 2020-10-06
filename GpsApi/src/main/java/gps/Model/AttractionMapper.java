package gps.Model;

import java.util.UUID;

public class AttractionMapper {

    private String attractionName;
    private String city;
    private String state;
    private UUID attractionId;
    private double longitude;
    private double latitude;

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

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public UUID getAttractionId() {
        return attractionId;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
