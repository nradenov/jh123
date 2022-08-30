package abnamro.recipes.web.rest;

import java.net.URISyntaxException;

public class BadRequestAlertException extends URISyntaxException {
    public BadRequestAlertException(String message, String entityName, String key) {
        super(message + entityName, key);
    }
}
