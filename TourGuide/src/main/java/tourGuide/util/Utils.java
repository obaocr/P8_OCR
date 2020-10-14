package tourGuide.util;

import gpsUtil.location.Location;
import tourGuide.Model.LocationMapper;

public class Utils {

    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

    /**
     * calculateDistance Utils
     * @param loc1 : location 1
     * @param loc2 : location 2 2
     * @return distance between two locations
     */
    public static double calculateDistance(Location loc1, Location loc2) {
        return(calculate(loc1.latitude,loc1.longitude,loc2.latitude,loc2.longitude));
    }

    public static double calculateDistance(LocationMapper loc1, LocationMapper loc2) {
        return(calculate(loc1.getLatitude(),loc1.getLongitude(),loc2.getLatitude(),loc2.getLongitude()));
    }

    private static double calculate(double lat1 , double long1, double lat2, double long2) {
        double latRad1 = Math.toRadians(lat1);
        double longRad1 = Math.toRadians(long1);
        double latRad2 = Math.toRadians(lat2);
        double longRad2 = Math.toRadians(long2);

        double angle = Math.acos(Math.sin(latRad1) * Math.sin(latRad2)
                + Math.cos(latRad1) * Math.cos(latRad2) * Math.cos(longRad1 - longRad2));

        double nauticalMiles = 60 * Math.toDegrees(angle);
        double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
        return statuteMiles;
    }

}
