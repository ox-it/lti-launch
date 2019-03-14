package edu.ksu.lti.launch.oauth;

import edu.ksu.lti.launch.service.ToolConsumer;

import java.security.Principal;

/**
 * Holds the LTI principal. This is designed to support multitenancy.
 * While we could put all the data returned in the LTI launch in here it would make the principal rather heavy. Instead
 * we just give it enough.
 */
public class LtiPrincipal implements Principal {

    private final ToolConsumer consumer;
    private final String user;

    public LtiPrincipal(ToolConsumer consumer, String user) {
        this.consumer = consumer;
        this.user = user;
    }

    @Override
    public String getName() {
        return user;
    }

    public String getUser() {
        return user;
    }

    public ToolConsumer getConsumer() {
        return this.getConsumer();
    }

    public String getTenant() {
        return consumer.getInstance();
    }

    public String toString() {
        return consumer.getInstance()+ ":"+ user;
    }
}
