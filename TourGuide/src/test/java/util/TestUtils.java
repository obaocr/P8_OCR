package util;

import gpsUtil.location.Location;
import org.junit.Test;
import tourGuide.Model.LocationMapper;
import tourGuide.util.Utils;

import static org.junit.Assert.assertTrue;

public class TestUtils {

    @Test
    public void testCalculDistanceShouldReturnTrue() {
        LocationMapper loc1 = new LocationMapper(33.817595D, -112.817595D);
        LocationMapper loc2 = new LocationMapper(40.817595D, -133.817595D);
        int distance = (int)  Utils.calculateDistance(loc1, loc2);
        System.out.println("Utils.calculateDistance(loc1, loc2) : " + distance);
        assertTrue(distance == 1471);
    }

}
