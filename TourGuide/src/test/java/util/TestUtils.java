package util;

import gpsUtil.location.Location;
import org.junit.Test;
import tourGuide.util.Utils;

import static org.junit.Assert.assertTrue;

public class TestUtils {

    @Test
    public void testCalculDistanceShouldReturnTrue() {
        Location loc1 = new Location(33.817595D, -112.817595D);
        Location loc2 = new Location(40.817595D, -133.817595D);
        int distance = (int)  Utils.calculateDistance(loc1, loc2);
        System.out.println("Utils.calculateDistance(loc1, loc2) : " + distance);
        assertTrue(distance == 1246);
    }

}
