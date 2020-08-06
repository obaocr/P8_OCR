package tourGuide.Model;

import javax.validation.constraints.NotNull;

public class UserPreferencesDTO {

    @NotNull
    private Integer attractionProximity;
    @NotNull
    private String currency;
    @NotNull
    private Integer lowerPricePoint;
    @NotNull
    private Integer highPricePoint;
    @NotNull
    private Integer tripDuration;
    @NotNull
    private Integer ticketQuantity;
    @NotNull
    private Integer numberOfAdults;
    @NotNull
    private Integer numberOfChildren;

    public Integer getAttractionProximity() {
        return attractionProximity;
    }

    public void setAttractionProximity(Integer attractionProximity) {
        this.attractionProximity = attractionProximity;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getLowerPricePoint() {
        return lowerPricePoint;
    }

    public void setLowerPricePoint(Integer lowerPricePoint) {
        this.lowerPricePoint = lowerPricePoint;
    }

    public Integer getHighPricePoint() {
        return highPricePoint;
    }

    public void setHighPricePoint(Integer highPricePoint) {
        this.highPricePoint = highPricePoint;
    }

    public Integer getTripDuration() {
        return tripDuration;
    }

    public void setTripDuration(Integer tripDuration) {
        this.tripDuration = tripDuration;
    }

    public Integer getTicketQuantity() {
        return ticketQuantity;
    }

    public void setTicketQuantity(Integer ticketQuantity) {
        this.ticketQuantity = ticketQuantity;
    }

    public Integer getNumberOfAdults() {
        return numberOfAdults;
    }

    public void setNumberOfAdults(Integer numberOfAdults) {
        this.numberOfAdults = numberOfAdults;
    }

    public Integer getNumberOfChildren() {
        return numberOfChildren;
    }

    public void setNumberOfChildren(Integer numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }
}
