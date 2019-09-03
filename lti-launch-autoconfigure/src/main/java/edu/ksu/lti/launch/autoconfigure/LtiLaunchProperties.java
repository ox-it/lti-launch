package edu.ksu.lti.launch.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("lti")
public class LtiLaunchProperties {

    /**
     * Unique key for the consumer of this LTI. This is normally a string which helps administrators identify
     * the consumer.
     */
    private String consumer = "consumer";

    /**
     * Secret for the consumer. This is used to sign the LTI launch and to verify it was signed by the trusted
     * tool consumer.
     */
    private String secret = null;

    /**
     * Publicly displayed name of the tool consumer. This may be shown to end users and should be the common name
     * that the service is know as.
     */
    private String name = "Tool Consumer";

    /**
     * URL of the consuming service. Needs to be set if the application should verify the URL.
     */
    private String url = null;

    public String getConsumer() {
        return consumer;
    }

    public void setConsumer(String consumer) {
        this.consumer = consumer;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}

