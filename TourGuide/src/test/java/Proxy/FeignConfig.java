package Proxy;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import tourGuide.Proxies.GpsProxy;

//@EnableFeignClients(clients = GpsProxy.class)
@EnableFeignClients("tourGuide")
@Configuration
//@EnableAutoConfiguration
public class FeignConfig {

}
