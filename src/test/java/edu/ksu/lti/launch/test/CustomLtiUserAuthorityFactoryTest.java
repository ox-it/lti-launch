package edu.ksu.lti.launch.test;


import edu.ksu.lti.launch.oauth.CustomLtiUserAuthority;
import edu.ksu.lti.launch.oauth.LtiUserAuthority;
import edu.ksu.lti.launch.oauth.LtiUserAuthorityFactory;
import edu.ksu.lti.launch.service.SingleToolConsumerService;
import edu.ksu.lti.launch.service.ToolConsumerService;
import edu.ksu.lti.launch.spring.config.LtiConfigurer;
import edu.ksu.lti.launch.spring.config.LtiLaunchCsrfMatcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth.common.signature.SharedConsumerSecretImpl;
import org.springframework.security.oauth.consumer.BaseProtectedResourceDetails;
import org.springframework.security.oauth.consumer.OAuthConsumerSupport;
import org.springframework.security.oauth.consumer.client.CoreOAuthConsumerSupport;
import org.springframework.security.oauth.provider.nonce.NullNonceServices;
import org.springframework.security.oauth.provider.nonce.OAuthNonceServices;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.net.URL;
import java.util.*;

import static edu.ksu.lti.launch.test.LtiSigning.getRequiredLtiParameters;
import static edu.ksu.lti.launch.test.LtiSigning.toQueryParams;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
 * This checks that you can supply your own role factory.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class CustomLtiUserAuthorityFactoryTest {

    @Configuration
    @EnableWebSecurity
    public static class TestWebSecurityConfig extends WebSecurityConfigurerAdapter {

        @Bean
        public ToolConsumerService toolConsumerService() {
            // We don't have a URL for the service here.
            return new SingleToolConsumerService("test", "Test", "http://example.com", "secret");
        }

        @Bean
        public OAuthNonceServices oAuthNonceServices() {
            return new NullNonceServices();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            LtiConfigurer<HttpSecurity> configurer = new LtiConfigurer<>(toolConsumerService(), "/launch", false, true, null);
            // Override to ignore the passed value.
            configurer.setLtiUserAuthorityFactory(new LtiUserAuthorityFactory() {
                @Override
                public Collection<LtiUserAuthority> getLtiUserAuthorities(String roles) {
                    return Collections.singleton(new CustomLtiUserAuthority("TEST"));
                }
            });
            http
                // We don't enable instance checking.
                .apply(configurer)
                .and().authorizeRequests().anyRequest().hasRole("LTI_USER");
            // Disable csrf for LTI launches
            http.csrf().requireCsrfProtectionMatcher(new LtiLaunchCsrfMatcher("/launch"));
        }
    }

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
            .apply(SecurityMockMvcConfigurers.springSecurity())
            .build();
    }

    @Test
    public void testRoleExtraction() throws Exception {
        OAuthConsumerSupport support = new CoreOAuthConsumerSupport();
        BaseProtectedResourceDetails details = new BaseProtectedResourceDetails();
        details.setAcceptsAuthorizationHeader(false);
        details.setSharedSecret(new SharedConsumerSecretImpl("secret"));
        details.setConsumerKey("test");
        URL url = new URL("http://server/launch");
        // There isn't a nice way to get the signed values back from the library.
        Map<String, String> additional = new HashMap<>(getRequiredLtiParameters());
        additional.put("roles", "Instructor");
        String encodedQueryString = support.getOAuthQueryString(details, null, url, "POST", additional);

        Map<String, List<String>> collect = toQueryParams(encodedQueryString);

        this.mockMvc.perform(post("http://server/launch")
            .params(new LinkedMultiValueMap<>(collect))
            .accept(MediaType.TEXT_HTML))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/"))
            .andExpect(SecurityMockMvcResultMatchers.authenticated()
                .withAuthorities(Arrays.asList(
                    // Check that we ignore the passed roles and return our custom granted authority.
                    new CustomLtiUserAuthority("TEST"),
                    new SimpleGrantedAuthority("ROLE_LTI_USER")
                ))
            );
    }

}
