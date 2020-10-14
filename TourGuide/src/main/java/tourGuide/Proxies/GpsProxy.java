package tourGuide.Proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tourGuide.Model.AttractionMapper;
import tourGuide.Model.VisitedLocationMapper;

import java.util.List;

/**
 * Proxy for Gps / micro service
 */
@FeignClient(name = "microservice-gps", url = "localhost:8046")
public interface GpsProxy {

    @GetMapping(value = "/gpsattractions")
    List<AttractionMapper> gpsGetAttractions();

    @GetMapping(value = "/gpsuserlocation")
    VisitedLocationMapper gpsGetUserLocation(@RequestParam String userId);

}