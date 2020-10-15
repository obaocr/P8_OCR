package rewardCentral.Utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom EntityIllegalArgumentException
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EntityIllegalArgumentException extends RuntimeException {

    private static final long serialVersionUID = 3392438702306004516L;

    public EntityIllegalArgumentException(String exception) {
        super(exception);
    }
}
