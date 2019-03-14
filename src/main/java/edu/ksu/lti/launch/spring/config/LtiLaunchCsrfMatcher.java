package edu.ksu.lti.launch.spring.config;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

/**
 * Disables CSRF on the LTI launch URL but keeps it enabled everywhere else.
 */
public class LtiLaunchCsrfMatcher implements RequestMatcher {

    private Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");

    private RequestMatcher requestMatcher;

    /**
     * @param path The path that should be excluded from CSRF. Typically /launch.
     */
    public LtiLaunchCsrfMatcher(String path) {
        this.requestMatcher = new AntPathRequestMatcher(path);
    }

    /* (non-Javadoc)
     * @see org.springframework.security.web.util.matcher.RequestMatcher#matches(javax.servlet.http.HttpServletRequest)
     */
    public boolean matches(HttpServletRequest request) {
        return !requestMatcher.matches(request) && !allowedMethods.matcher(request.getMethod()).matches();
    }
}
