package edu.ksu.lti.launch.service;

/**
 * A consumer of the LTI tool.
 */
public interface ToolConsumer {

    String getInstance();

    String getName();

    String getUrl();

}
