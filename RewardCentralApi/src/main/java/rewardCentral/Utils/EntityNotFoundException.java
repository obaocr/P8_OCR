package rewardCentral.Utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom EntityNotFoundException
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 3926498183341893451L;

    public EntityNotFoundException(String exception) {
        super(exception);
    }

}
