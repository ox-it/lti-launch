package edu.ksu.lti.launch.spring.config;

import edu.ksu.lti.launch.service.SingleToolConsumerService;
import edu.ksu.lti.launch.service.ToolConsumerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity(debug = false)
public class TestWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public ToolConsumerService toolConsumerService() {
        return new SingleToolConsumerService("test", "Test", "http://localhost", "secret");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .apply(new LtiConfigurer<>(toolConsumerService(), "/launch", true, null))
            .and().authorizeRequests().anyRequest().hasRole("LTI_USER");
        // Disable csrf for LTI launches
        http.csrf().requireCsrfProtectionMatcher(new LtiLaunchCsrfMatcher("/launch"));
    }
}
