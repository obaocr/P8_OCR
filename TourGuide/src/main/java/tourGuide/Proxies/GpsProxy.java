package tourGuide.Proxies;

import gpsUtil.location.Attraction;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "microservice-gps", url = "localhost:8046")
public interface GpsProxy {

    @GetMapping(value = "/gpsattractions")
    String gpsGetAttractions();

}
