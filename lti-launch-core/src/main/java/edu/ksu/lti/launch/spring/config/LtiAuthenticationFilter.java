package edu.ksu.lti.launch.spring.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth.provider.filter.ProtectedResourceProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This makes the LTI authentication persist in the session after it's been authenticated.
 */
public class LtiAuthenticationFilter extends ProtectedResourceProcessingFilter {

    private RequestMatcher requestMatcher;

    public LtiAuthenticationFilter(RequestMatcher requestMatcher) {
        this.requestMatcher = requestMatcher;
    }

    /**
     * For LTI we don't want to reset the previous authentication as we want it to persist in the session. A standard
     * OAuth1 filter would reset the previous security context here.
     *
     * @param previousAuthentication The previous authentication.
     */
    protected void resetPreviousAuthentication(Authentication previousAuthentication) {
    }

    /**
     * We only want to filter on the LTI launch URL.
     */
    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        return requestMatcher.matches(request);
    }


}
