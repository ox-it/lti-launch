package edu.ksu.lti.launch.test;


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
import org.springframework.security.oauth.common.signature.SharedConsumerSecretImpl;
import org.springframework.security.oauth.consumer.BaseProtectedResourceDetails;
import org.springframework.security.oauth.consumer.OAuthConsumerSupport;
import org.springframework.security.oauth.consumer.client.CoreOAuthConsumerSupport;
import org.springframework.security.oauth.provider.nonce.NullNonceServices;
import org.springframework.security.oauth.provider.nonce.OAuthNonceServices;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import static edu.ksu.lti.launch.test.LtiSigning.getRequiredLtiParameters;
import static edu.ksu.lti.launch.test.LtiSigning.toQueryParams;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
 * This checks that you can use the library without supplying a URL for the SingleToolConsumerService.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class LtiStandardsITest {

    private URL url;
    private BaseProtectedResourceDetails details;
    private OAuthConsumerSupport support;
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setup() throws MalformedURLException {
        support = new CoreOAuthConsumerSupport();
        details = new BaseProtectedResourceDetails();
        details.setAcceptsAuthorizationHeader(false);
        details.setSharedSecret(new SharedConsumerSecretImpl("secret"));
        details.setConsumerKey("test");
        url = new URL("http://server/launch");

        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
            .apply(SecurityMockMvcConfigurers.springSecurity())
            .build();
    }

    @Test
    public void testNoResourceLinkId() throws Exception {
        Map<String, String> requiredLtiParameters = getRequiredLtiParameters();
        requiredLtiParameters.remove("resource_link_id");
        String encodedQueryString = support.getOAuthQueryString(details, null, url, "POST", requiredLtiParameters);

        Map<String, List<String>> collect = toQueryParams(encodedQueryString);

        this.mockMvc.perform(post("http://server/launch")
            .params(new LinkedMultiValueMap<>(collect))
            .accept(MediaType.TEXT_HTML))
            .andExpect(status().is3xxRedirection())
            .andExpect(result -> result.getResponse().getRedirectedUrl().startsWith("http://tool.consumer/"));
    }

    @Test
    public void testNoResourceLinkIdNoReturn() throws Exception {
        Map<String, String> requiredLtiParameters = getRequiredLtiParameters();
        requiredLtiParameters.remove("resource_link_id");
        requiredLtiParameters.remove("launch_presentation_return_url");
        String encodedQueryString = support.getOAuthQueryString(details, null, url, "POST", requiredLtiParameters);

        Map<String, List<String>> collect = toQueryParams(encodedQueryString);

        this.mockMvc.perform(post("http://server/launch")
            .params(new LinkedMultiValueMap<>(collect))
            .accept(MediaType.TEXT_HTML))
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void testInvalidLtiVersion() throws Exception {
        Map<String, String> requiredLtiParameters = getRequiredLtiParameters();
        requiredLtiParameters.put("lti_version", "LTI-1");
        String encodedQueryString = support.getOAuthQueryString(details, null, url, "POST", requiredLtiParameters);

        Map<String, List<String>> collect = toQueryParams(encodedQueryString);

        this.mockMvc.perform(post("http://server/launch")
            .params(new LinkedMultiValueMap<>(collect))
            .accept(MediaType.TEXT_HTML))
            .andExpect(result -> result.getResponse().getRedirectedUrl().startsWith("http://tool.consumer/"));
    }

    @Test
    public void testWrongLtiVersion() throws Exception {
        Map<String, String> requiredLtiParameters = getRequiredLtiParameters();
        requiredLtiParameters.put("lti_version", "LTI-2");
        String encodedQueryString = support.getOAuthQueryString(details, null, url, "POST", requiredLtiParameters);

        Map<String, List<String>> collect = toQueryParams(encodedQueryString);

        this.mockMvc.perform(post("http://server/launch")
            .params(new LinkedMultiValueMap<>(collect))
            .accept(MediaType.TEXT_HTML))
            .andExpect(result -> result.getResponse().getRedirectedUrl().startsWith("http://tool.consumer/"));
    }

    @Test
    public void testMissingLtiVersion() throws Exception {

        Map<String, String> requiredLtiParameters = getRequiredLtiParameters();
        requiredLtiParameters.remove("lti_version");
        String encodedQueryString = support.getOAuthQueryString(details, null, url, "POST", requiredLtiParameters);

        Map<String, List<String>> collect = toQueryParams(encodedQueryString);

        this.mockMvc.perform(post("http://server/launch")
            .params(new LinkedMultiValueMap<>(collect))
            .accept(MediaType.TEXT_HTML))
            .andExpect(result -> result.getResponse().getRedirectedUrl().startsWith("http://tool.consumer/"));
    }

    @Test
    public void testInvalidLtiMessageType() throws Exception {
        Map<String, String> requiredLtiParameters = getRequiredLtiParameters();
        requiredLtiParameters.put("lti_message_type", "not-a-valid-type");
        String encodedQueryString = support.getOAuthQueryString(details, null, url, "POST", requiredLtiParameters);

        Map<String, List<String>> collect = toQueryParams(encodedQueryString);

        this.mockMvc.perform(post("http://server/launch")
            .params(new LinkedMultiValueMap<>(collect))
            .accept(MediaType.TEXT_HTML))
            .andExpect(result -> result.getResponse().getRedirectedUrl().startsWith("http://tool.consumer/"));
    }

    @Test
    public void testMissingLtiMessageType() throws Exception {
        Map<String, String> requiredLtiParameters = getRequiredLtiParameters();
        requiredLtiParameters.remove("lti_message_type");
        String encodedQueryString = support.getOAuthQueryString(details, null, url, "POST", requiredLtiParameters);

        Map<String, List<String>> collect = toQueryParams(encodedQueryString);

        this.mockMvc.perform(post("http://server/launch")
            .params(new LinkedMultiValueMap<>(collect))
            .accept(MediaType.TEXT_HTML))
            .andExpect(result -> result.getResponse().getRedirectedUrl().startsWith("http://tool.consumer/"));
    }

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
                .apply(new LtiConfigurer<>(toolConsumerService(), "/launch", false, true, null))
                .and().authorizeRequests().anyRequest().hasRole("LTI_USER");
            // Disable csrf for LTI launches
            http.csrf().requireCsrfProtectionMatcher(new LtiLaunchCsrfMatcher("/launch"));
        }
    }

}
