package edu.ksu.lti.launch.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Thrown to indicate that this LTI application is running in an unexpected instance of Canvas.
 *
 * This happens most often after a Canvas test/beta overwrite from production. The
 * production settings are copied to the development instance and then you have a user
 * in Canvas test trying to hit the production LTI application.
 */
public class InvalidInstanceException extends AuthenticationException {
    private static final long serialVersionUID = 1L;

    private final String launchUrl;
    private final String requiredUrl;

    public InvalidInstanceException(String launchUrl, String requiredUrl) {
        super("LTI Launch for incorrect service.");
        this.launchUrl = launchUrl;
        this.requiredUrl = requiredUrl;
    }

    public String getLaunchUrl() {
        return this.launchUrl;
    }

    public String getRequiredUrl() {
        return requiredUrl;
    }

    @Override
    public String getMessage() {
        return "LTI Launch was made from " +
            launchUrl +
            " but we are only configured to accept launches from " +
            requiredUrl;
    }
}
