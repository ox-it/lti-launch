package edu.ksu.lti.launch.service;

/**
 * A service to lookup ToolConsumers this is used to find the ToolConsumer for a LTI launch.
 */
public interface ToolConsumerService {

    /**
     * Looks up a ToolConsumer
     * @param instance The name/key of the ToolConsumer.
     * @return The ToolConsumer or <code>null</code> if there isn't one.
     */
    ToolConsumer getConsumer(String instance);

    /**
     * This isn't part of the ToolConsumer so we don't keep the secret in the session
     * and other places after we've done the authentication.
     * @param instance The name/key of the ToolConsumer.
     * @return The secret or <code>null</code> if there isn't one.
     */
    String getSecret(String instance);

}
