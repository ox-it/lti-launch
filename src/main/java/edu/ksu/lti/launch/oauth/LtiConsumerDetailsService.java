package edu.ksu.lti.launch.oauth;

import edu.ksu.lti.launch.service.ToolConsumer;
import edu.ksu.lti.launch.service.ToolConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth.common.OAuthException;
import org.springframework.security.oauth.common.signature.SharedConsumerSecretImpl;
import org.springframework.security.oauth.provider.BaseConsumerDetails;
import org.springframework.security.oauth.provider.ConsumerDetails;
import org.springframework.security.oauth.provider.ConsumerDetailsService;

/**
 * Consumer Details implementation. Given a consumer key, it looks up the secret
 * from a supplied LTI key lookup service and constructs a ConsumerDetails object
 * for Spring to work with to verify the authenticity of the LTI launch request.
 */
public class LtiConsumerDetailsService implements ConsumerDetailsService {

    private ToolConsumerService toolConsumerService;

    @Autowired
    public LtiConsumerDetailsService(ToolConsumerService toolConsumerService) {
        this.toolConsumerService = toolConsumerService;
    }

    @Override
    public ConsumerDetails loadConsumerByConsumerKey(String consumerKey) {
        if(consumerKey == null || consumerKey.isEmpty()) {
            throw new OAuthException("Supplied LTI key can not be blank");
        }
        ToolConsumer toolConsumer = toolConsumerService.getConsumer(consumerKey);
        if (toolConsumer == null) {
            throw new OAuthException("No ToolConsumer found for LTI key "+ consumerKey);
        }
        String secret = toolConsumerService.getSecret(consumerKey);
        if(secret == null || secret.isEmpty()) {
            throw new OAuthException("No secret set for "+ consumerKey);
        }

        BaseConsumerDetails consumerDetails = new BaseConsumerDetails();
        consumerDetails.setConsumerKey(consumerKey);
        consumerDetails.setSignatureSecret(new SharedConsumerSecretImpl(secret));
        consumerDetails.setConsumerName(toolConsumer.getName());
        consumerDetails.setRequiredToObtainAuthenticatedToken(false);
        return consumerDetails;
    }
}
