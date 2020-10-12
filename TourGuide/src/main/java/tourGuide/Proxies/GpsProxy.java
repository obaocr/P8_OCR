package tourGuide.Proxies;

import gpsUtil.location.Attraction;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tourGuide.Model.AttractionMapper;
import tourGuide.Model.VisitedLocationMapper;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "microservice-gps", url = "localhost:8046")
public interface GpsProxy {

    @GetMapping(value = "/gpsattractions")
    List<AttractionMapper> gpsGetAttractions();

    @GetMapping(value = "/gpsuserlocation")
    VisitedLocationMapper gpsGetUserLocation(@RequestParam String userId);

}