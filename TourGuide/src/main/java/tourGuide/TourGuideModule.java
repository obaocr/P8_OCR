package tourGuide;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import gpsUtil.GpsUtil;
import rewardCentral.RewardCentral;
import tourGuide.service.GpsService;
import tourGuide.service.RewardsService;

// TODO éventuellement retravailler pour n'avoir que Rewardservice en injection, et que des SVC en injection ...
// TODO Gérer le TripProcer en Bean et non pas en New ..

@Configuration
public class TourGuideModule {
	
	@Bean
	public GpsUtil getGpsUtil() {
		return new GpsUtil();
	}

	@Bean
	public GpsService getGpsService() {
		return new GpsService(getGpsUtil());
	}
	
	@Bean
	public RewardsService getRewardsService() {
		return new RewardsService(getGpsService(), getRewardCentral());
	}
	
	@Bean
	public RewardCentral getRewardCentral() {
		return new RewardCentral();
	}
	
}
