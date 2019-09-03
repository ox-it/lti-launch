package edu.ksu.lti.launch.autoconfigure;

import edu.ksu.lti.launch.service.LtiLoginService;
import edu.ksu.lti.launch.service.SimpleLtiLoginService;
import edu.ksu.lti.launch.service.SingleToolConsumerService;
import edu.ksu.lti.launch.service.ToolConsumerService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.UUID;

/**
 * This auto configures the LTI Launch library.
 */
@Configuration
@ConditionalOnWebApplication
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
@Order(1001)
@EnableConfigurationProperties(LtiLaunchProperties.class)
public class LtiLaunchAutoConfigure {

    private static final Log logger = LogFactory.getLog(LtiLaunchAutoConfigure.class);

    private final LtiLaunchProperties properties;

    public LtiLaunchAutoConfigure(LtiLaunchProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public LtiLoginService ltiLoginService() {
        return new SimpleLtiLoginService();
    }

    @Bean
    @ConditionalOnMissingBean
    public ToolConsumerService toolConsumerService() {
        String secret = properties.getSecret();
        if (secret == null) {
            secret = UUID.randomUUID().toString();
            logger.info("Creating tool consumer with key: " + properties.getConsumer() +
                " and generated secret of: " + secret + " Set 'lti.secret' to prevent secret generation.");
        } else {
            logger.info("Creating tool consumer with key: " + properties.getConsumer());
        }
        return new SingleToolConsumerService(properties.getConsumer(), properties.getName(), properties.getUrl(), secret);
    }
}
