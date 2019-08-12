package edu.ksu.lti.launch.test;


import edu.ksu.lti.launch.service.SingleToolConsumerService;
import edu.ksu.lti.launch.service.ToolConsumerService;
import edu.ksu.lti.launch.spring.config.LtiConfigurer;
import edu.ksu.lti.launch.spring.config.LtiLaunchCsrfMatcher;
import edu.ksu.lti.launch.spring.config.TestWebSecurityConfig;
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
import org.springframework.security.oauth.common.signature.SharedConsumerSecretImpl;
import org.springframework.security.oauth.consumer.BaseProtectedResourceDetails;
import org.springframework.security.oauth.consumer.OAuthConsumerSupport;
import org.springframework.security.oauth.consumer.client.CoreOAuthConsumerSupport;
import org.springframework.security.oauth.provider.nonce.NullNonceServices;
import org.springframework.security.oauth.provider.nonce.OAuthNonceServices;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
 * This checks that you can use the library without supplying a URL for the SingleToolConsumerService.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class NoUrlITest {

    @Configuration
    @EnableWebSecurity
    public static class TestWebSecurityConfig extends WebSecurityConfigurerAdapter {

        @Bean
        public ToolConsumerService toolConsumerService() {
            // We don't have a URL for the service here.
            return new SingleToolConsumerService("test", "Test", null, "secret");
        }

        @Bean
        public OAuthNonceServices oAuthNonceServices() {
            return new NullNonceServices();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                // We don't enable instance checking.
                .apply(new LtiConfigurer<>(toolConsumerService(), "/launch", false, null))
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
    public void testSignedLogin() throws Exception {
        OAuthConsumerSupport support = new CoreOAuthConsumerSupport();
        BaseProtectedResourceDetails details = new BaseProtectedResourceDetails();
        details.setAcceptsAuthorizationHeader(false);
        details.setSharedSecret(new SharedConsumerSecretImpl("secret"));
        details.setConsumerKey("test");
        URL url = new URL("http://server/launch");
        // There isn't a nice way to get the signed values back from the library.
        String encodedQueryString = support.getOAuthQueryString(details, null, url, "POST", Collections.emptyMap());

        Map<String, List<String>> collect = toQueryParams(encodedQueryString);

        this.mockMvc.perform(post("http://server/launch")
            .params(new LinkedMultiValueMap<>(collect))
            .accept(MediaType.TEXT_HTML))
            .andExpect(status().is3xxRedirection());
    }


    private Map<String, List<String>> toQueryParams(String encodedQueryString) {
        MultiValueMap<String, String> queryParams = UriComponentsBuilder
            .fromUriString("?" + encodedQueryString)
            .build()
            .getQueryParams();
        return queryParams.entrySet()
            .stream()
            .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue()
                .stream()
                .map(s -> UriUtils.decode(s, "UTF-8"))
                .collect(Collectors.toList()))
            );
    }

}
