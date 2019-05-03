package edu.ksu.lti.launch.oauth;

import edu.ksu.lti.launch.service.ToolConsumer;

import java.security.Principal;

/**
 * Holds the LTI principal. This is designed to support multitenancy.
 * While we could put all the data returned in the LTI launch in here it would make the principal rather heavy. Instead
 * we just give it enough. In the future we may want to encode this as a JWT.
 */
public class LtiPrincipal implements Principal {

    private final ToolConsumer consumer;
    private final String user;
    private final String context;

    public LtiPrincipal(ToolConsumer consumer, String user) {
        this.consumer = consumer;
        this.user = user;
        context = null;
    }

    /**
     * Create a new LTI Principal.
     * @param consumer The LTI tool consumer that this principal was authenticated through.
     * @param user The user who was authenticated.
     * @param context The context in the tool consumer that the user was sent from.
     */
    public LtiPrincipal(ToolConsumer consumer, String user, String context) {
        this.consumer = consumer;
        this.user = user;
        this.context = context;
    }

    @Override
    public String getName() {
        return user;
    }

    public String getUser() {
        return user;
    }

    /**
     * @return This is the context that the LTI was launched from. This isn't the course ID.
     */
    public String getContext() {
        return context;
    }

    public ToolConsumer getConsumer() {
        return consumer;
    }

    public String getTenant() {
        return consumer.getInstance();
    }

    public String toString() {
        return consumer.getInstance()+ ":"+ user + ((context != null)?":"+ context:"");
    }
}
