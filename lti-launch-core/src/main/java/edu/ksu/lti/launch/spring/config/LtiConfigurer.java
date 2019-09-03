package edu.ksu.lti.launch.spring.config;

import edu.ksu.lti.launch.oauth.LtiAuthenticationFilterEntryPoint;
import edu.ksu.lti.launch.oauth.LtiConsumerDetailsService;
import edu.ksu.lti.launch.oauth.LtiOAuthAuthenticationHandler;
import edu.ksu.lti.launch.oauth.LtiUserAuthorityFactory;
import edu.ksu.lti.launch.service.LtiLoginService;
import edu.ksu.lti.launch.service.ToolConsumerService;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.oauth.provider.OAuthProcessingFilterEntryPoint;
import org.springframework.security.oauth.provider.nonce.InMemoryNonceServices;
import org.springframework.security.oauth.provider.token.InMemoryProviderTokenServices;
import org.springframework.security.oauth.provider.token.OAuthProviderTokenServices;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * A configurer for enabling LTI login for a Spring application.
 */
public class LtiConfigurer extends AbstractHttpConfigurer<LtiConfigurer, HttpSecurity> {

    private LtiConsumerDetailsService oauthConsumerDetailsService;
    private LtiOAuthAuthenticationHandler oauthAuthenticationHandler;
    private OAuthProviderTokenServices oauthProviderTokenServices = new InMemoryProviderTokenServices();
    private LtiUserAuthorityFactory ltiUserAuthorityFactory;

    private LtiAuthenticationFilterEntryPoint authenticationEntryPoint;

    private String launchPath = "/launch";
    private String errorPath;
    private boolean checkInstance = false;
    private boolean validateLti = true;

    public LtiConfigurer() {
    }

    public void setLtiUserAuthorityFactory(LtiUserAuthorityFactory ltiUserAuthorityFactory) {
        this.ltiUserAuthorityFactory = ltiUserAuthorityFactory;
    }

    /**
     * Check that the LTI launch is from the correct instance.
     * @return The current instance for chaining.
     */
    public LtiConfigurer checkInstance() {
        this.checkInstance = true;
        return this;
    }

    /**
     * Don't perform extra LTI validation checked.
     * @return The current instance for chaining.
     */
    public LtiConfigurer disableValidation() {
        this.validateLti = false;
        return this;
    }

    /**
     * @param errorPath The path to send users to when an error occurs.
     * @return The current instance for chaining.
     */
    public LtiConfigurer errorPath(String errorPath) {
        this.errorPath = errorPath;
        return this;
    }

    /**
     * @param launchPath The path to accept LTI launches on.
     * @return The current instance for chaining.
     */
    public LtiConfigurer launchPath(String launchPath) {
        this.launchPath = launchPath;
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void init(HttpSecurity http) {
        // Allow LTI launches to bypass CSRF protection
        CsrfConfigurer configurer = http.getConfigurer(CsrfConfigurer.class);
        if (configurer != null) {
            configurer.requireCsrfProtectionMatcher(new LtiLaunchCsrfMatcher(launchPath));
        }
        // In the future we should use CSP to limit the domains that can embed this tool
        HeadersConfigurer headersConfigurer = http.getConfigurer(HeadersConfigurer.class);
        if (headersConfigurer != null) {
            headersConfigurer.frameOptions().disable();
        }
    }


    @Override
    public void configure(HttpSecurity http) {
        ToolConsumerService toolConsumerService = LtiConfigurerUtils.getToolConsumerService(http);
        oauthAuthenticationHandler = new LtiOAuthAuthenticationHandler(toolConsumerService);

        oauthConsumerDetailsService = new LtiConsumerDetailsService(toolConsumerService);

        oauthAuthenticationHandler.setCheckInstance(checkInstance);
        oauthAuthenticationHandler.setValidateLti(validateLti);
        if (ltiUserAuthorityFactory != null) {
            this.oauthAuthenticationHandler.setUserAuthorityFactory(ltiUserAuthorityFactory);
        }
        authenticationEntryPoint = new LtiAuthenticationFilterEntryPoint();
        if (errorPath != null) {
            authenticationEntryPoint.setErrorPage(errorPath);
        }

        LtiLoginService ltiLoginService = LtiConfigurerUtils.getLtiLoginService(http);
        http
            .addFilterBefore(configureProcessingFilter(), FilterSecurityInterceptor.class)
            .addFilterAfter(configureLoginFilter(ltiLoginService), SwitchUserFilter.class);
    }

    private LtiLoginFilter configureLoginFilter(LtiLoginService loginService) {
        LtiLoginFilter filter = new LtiLoginFilter(new AntPathRequestMatcher(launchPath));
        if (loginService != null) {
            filter.setLtiLoginService(loginService);
        }
        return filter;
    }

    private LtiAuthenticationFilter configureProcessingFilter() {
        //Set up nonce service to prevent replay attacks.
        InMemoryNonceServices nonceService = new InMemoryNonceServices();
        nonceService.setValidityWindowSeconds(600);

        LtiAuthenticationFilter ltiAuthenticationFilter = new LtiAuthenticationFilter(new AntPathRequestMatcher(launchPath));
        ltiAuthenticationFilter.setAuthHandler(oauthAuthenticationHandler);
        ltiAuthenticationFilter.setConsumerDetailsService(oauthConsumerDetailsService);
        ltiAuthenticationFilter.setNonceServices(nonceService);
        ltiAuthenticationFilter.setTokenServices(oauthProviderTokenServices);
        ltiAuthenticationFilter.setIgnoreMissingCredentials(false);
        ltiAuthenticationFilter.setAuthenticationEntryPoint(authenticationEntryPoint);
        return ltiAuthenticationFilter;
    }
}
