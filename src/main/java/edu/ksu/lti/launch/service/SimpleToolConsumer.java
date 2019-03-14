package edu.ksu.lti.launch.service;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * A simple implementation of the ToolConsumer.
 */
public class SimpleToolConsumer implements ToolConsumer {

    private final String instance;
    private final String name;
    private final String url;

    public SimpleToolConsumer(String instance, String name, String url) {
        requireNonNull(instance, "The instance cannot be null.");
        requireNonNull(name, "The name cannot be null.");
        requireNonNull(url, "The url cannot be null.");
        this.instance = instance;
        this.name = name;
        this.url = url;
    }

    @Override
    public String getInstance() {
        return instance;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUrl() {
        return url;
    }

    public boolean matches(String instance) {
        return this.instance.equals(instance);
    }
}
