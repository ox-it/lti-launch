package edu.ksu.lti.launch.test;


import edu.ksu.lti.launch.spring.config.TestWebSecurityConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.oauth.common.signature.SharedConsumerSecretImpl;
import org.springframework.security.oauth.consumer.BaseProtectedResourceDetails;
import org.springframework.security.oauth.consumer.OAuthConsumerSupport;
import org.springframework.security.oauth.consumer.client.CoreOAuthConsumerSupport;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
 * This test simply tests that the spring context is able to be setup in an appropiate way.
 * It does this just by using a spring context similar to one that client code will need
 * to set up.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringJUnitWebConfig(classes = {TestWebSecurityConfig.class})
public class SpringContextITest {

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
    public void testNoParams() throws Exception {
        this.mockMvc.perform(get("/launch")
            .accept(MediaType.TEXT_HTML))
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void testNeedLogin() throws Exception {
        this.mockMvc.perform(get("/")
            .accept(MediaType.TEXT_HTML))
            .andExpect(status().isForbidden());
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

        MultiValueMap<String, String> queryParams = UriComponentsBuilder
            .fromUriString("?" + encodedQueryString)
            .build()
            .getQueryParams();
        Map<String, List<String>> collect = queryParams.entrySet()
            .stream()
            .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue()
                .stream()
                .map(s -> UriUtils.decode(s, "UTF-8"))
                .collect(Collectors.toList()))
            );

        this.mockMvc.perform(post("http://server/launch")
            .params(new LinkedMultiValueMap<>(collect))
            .content(encodedQueryString)
            .accept(MediaType.TEXT_HTML))
            .andExpect(status().is3xxRedirection());
    }

}
