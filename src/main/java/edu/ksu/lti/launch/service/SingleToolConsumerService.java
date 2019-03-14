package edu.ksu.lti.launch.service;

/**
 * This just handles one tool consumer.
 */
public class SingleToolConsumerService implements ToolConsumerService {

    private String secret;
    private SimpleToolConsumer toolConsumer;

    public SingleToolConsumerService(String instance, String name, String url, String secret) {
        this.toolConsumer = new SimpleToolConsumer(instance, name, url);
        this.secret = secret;
    }

    @Override
    public ToolConsumer getConsumer(String instance) {
        return (toolConsumer.matches(instance))?toolConsumer:null;
    }

    @Override
    public String getSecret(String instance) {
        return (toolConsumer.matches(instance))?secret:null;
    }
}
