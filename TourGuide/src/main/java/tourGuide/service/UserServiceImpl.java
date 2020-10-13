package tourGuide.service;

import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tourGuide.Model.UserPreferencesDTO;
import tourGuide.user.User;
import tourGuide.user.UserPreferences;

import javax.money.Monetary;

/**
 * Service Layer for user
 */
@Service
public class UserServiceImpl implements UserService{

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    // TODO Gérer not found 201 ?
    @Override
    public UserPreferences getUserPreferences(User user) {
        logger.info("getUserPreferences");
        if (user != null) {
            return user.getUserPreferences();
        }
        logger.debug("getUserPreferences : userName null");
        return null;
    }

    // TODO Gérer not found 201 ?
    // TODO : pas de pb de performance, on peut laisser en l'état
    @Override
    public UserPreferencesDTO getUserPreferencesSummary(User user) {
        logger.info("getUserPreferencesSummary");
        UserPreferencesDTO userPreferencesDTO = new UserPreferencesDTO();
        if (user != null) {
            UserPreferences userPreferences = user.getUserPreferences();
            userPreferencesDTO.setAttractionProximity(userPreferences.getAttractionProximity());
            userPreferencesDTO.setTripDuration(userPreferences.getTripDuration());
            userPreferencesDTO.setNumberOfAdults(userPreferences.getNumberOfAdults());
            userPreferencesDTO.setNumberOfChildren(userPreferences.getNumberOfChildren());
            userPreferencesDTO.setTicketQuantity(userPreferences.getTicketQuantity());
            userPreferencesDTO.setCurrency(userPreferences.getCurrency().getCurrencyCode());
            userPreferencesDTO.setHighPricePoint(userPreferences.getHighPricePoint().getNumber().intValue());
            userPreferencesDTO.setLowerPricePoint(userPreferences.getLowerPricePoint().getNumber().intValue());
            return userPreferencesDTO;
        }
        logger.debug("getUserPreferencesSummary : userName null");
        return null;
    }

    // TODO Gérer Username not found 201 ?
    // TODO : pas de pb de performance, on peut laisser en l'état
    @Override
    public UserPreferences setUserPreferences(User user, UserPreferencesDTO userPreferencesDTO) {
        logger.info("settUserPreferences : " + user.getUserName());
        if (user != null && userPreferencesDTO != null) {
            UserPreferences userPreferences = user.getUserPreferences();
            userPreferences.setAttractionProximity(userPreferencesDTO.getAttractionProximity());
            userPreferences.setTripDuration(userPreferencesDTO.getTripDuration());
            userPreferences.setTicketQuantity(userPreferencesDTO.getTicketQuantity());
            userPreferences.setNumberOfAdults(userPreferencesDTO.getNumberOfChildren());
            userPreferences.setNumberOfChildren(userPreferencesDTO.getNumberOfChildren());
            userPreferences.setCurrency(Monetary.getCurrency(userPreferencesDTO.getCurrency()));
            userPreferences.setLowerPricePoint(Money.of(userPreferencesDTO.getLowerPricePoint(), userPreferences.getCurrency()));
            userPreferences.setHighPricePoint(Money.of(userPreferencesDTO.getHighPricePoint(), userPreferences.getCurrency()));
            user.setUserPreferences(userPreferences);
            return user.getUserPreferences();
        }
        logger.debug("settUserPreferences : Input param null ");
        return null;
    }
}
