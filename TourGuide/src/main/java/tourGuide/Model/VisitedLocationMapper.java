package tourGuide.Model;

import java.util.Date;
import java.util.UUID;

public class VisitedLocationMapper {

    private UUID userId;
    private LocationMapper location;
    private Date timeVisited;

    public VisitedLocationMapper(UUID userId, LocationMapper location, Date timeVisited) {
        this.userId = userId;
        this.location = location;
        this.timeVisited = timeVisited;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public LocationMapper getLocation() {
        return location;
    }

    public void setLocation(LocationMapper location) {
        this.location = location;
    }

    public Date getTimeVisited() {
        return timeVisited;
    }

    public void setTimeVisited(Date timeVisited) {
        this.timeVisited = timeVisited;
    }
}
