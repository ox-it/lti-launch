package edu.ksu.lti.launch.service;

import edu.ksu.lti.launch.exception.NoLtiSessionException;
import edu.ksu.lti.launch.model.LtiLaunchData;
import edu.ksu.lti.launch.model.LtiSession;
import edu.ksu.lti.launch.oauth.LtiPrincipal;
import org.springframework.context.ApplicationListener;

/**
 * Users of this library should implement this interface if they want custom behaviour.
 * @see SimpleLtiLoginService
 */
public interface LtiLoginService {

    /**
     * Gets the URL to redirect the current user to after successful login.
     * @param principal The authenticated principal.
     * @return The URL to redirect to.
     */
    String getInitialView(LtiPrincipal principal);

    LtiSession getLtiSession() throws NoLtiSessionException;

    void setLtiSession(LtiPrincipal principal, LtiSession ltiSession);

    /**
     * Callback that happens after a successful login. This is useful if some of the data from the LTI launch is needed
     * to be present in the handler. Otherwise using a handler for a authentication events.
     *
     * @param principal The principal that was successfully authenticated.
     * @param launchData The LTI launch data.
     * @see ApplicationListener
     */
    default void onLogin(LtiPrincipal principal, LtiLaunchData launchData) {
        // Nothing
    }
}
