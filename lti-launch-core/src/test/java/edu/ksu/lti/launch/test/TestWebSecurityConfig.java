package edu.ksu.lti.launch.test;

import edu.ksu.lti.launch.service.LtiLoginService;
import edu.ksu.lti.launch.service.SimpleLtiLoginService;
import edu.ksu.lti.launch.service.SingleToolConsumerService;
import edu.ksu.lti.launch.service.ToolConsumerService;
import edu.ksu.lti.launch.spring.config.LtiConfigurer;
import edu.ksu.lti.launch.spring.config.LtiLaunchCsrfMatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth.provider.nonce.NullNonceServices;
import org.springframework.security.oauth.provider.nonce.OAuthNonceServices;

@Configuration
@EnableWebSecurity(debug = false)
public class TestWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public ToolConsumerService toolConsumerService() {
        return new SingleToolConsumerService("test", "Test", "http://tool.consumer/", "secret");
    }

    @Bean
    public LtiLoginService ltiLoginService() {
        return new SimpleLtiLoginService();
    }

    @Bean
    public OAuthNonceServices oAuthNonceServices() {
        return new NullNonceServices();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .apply(new LtiConfigurer().checkInstance())
            .and().authorizeRequests().anyRequest().hasRole("LTI_USER");
        // Disable csrf for LTI launches
        http.csrf().requireCsrfProtectionMatcher(new LtiLaunchCsrfMatcher("/launch"));
    }
}
