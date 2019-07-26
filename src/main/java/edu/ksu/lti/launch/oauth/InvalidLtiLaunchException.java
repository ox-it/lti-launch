package edu.ksu.lti.launch.oauth;

import org.springframework.security.core.AuthenticationException;

/**
 * An exception with the LTI for which we should redirect to the Tool Consumer and include a nice error message.
 */
public class InvalidLtiLaunchException extends AuthenticationException {

    private final String returnUrl;

    /**
     * Create a new launch failure.
     * @param s The error message to display to the user.
     * @param returnUrl The tool consumer URL to send the response back to, may be {@code null}
     */
    public InvalidLtiLaunchException(String s, String returnUrl) {
        super(s);
        this.returnUrl = returnUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public boolean hasReturnUrl() {
        return returnUrl != null && !returnUrl.isEmpty();
    }

}
