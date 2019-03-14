package edu.ksu.lti.launch.spring.config;

import edu.ksu.lti.launch.beans.LtiLaunchPropertyValues;
import edu.ksu.lti.launch.beans.SnakeCasePropertyValues;
import edu.ksu.lti.launch.model.LtiLaunchData;
import edu.ksu.lti.launch.model.LtiSession;
import edu.ksu.lti.launch.oauth.LtiPrincipal;
import edu.ksu.lti.launch.service.LtiLoginService;
import edu.ksu.lti.launch.service.SimpleLtiLoginService;
import org.springframework.beans.PropertyValues;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.validation.DataBinder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This filter is used to redirect the user to the home page after successful authentication of the LTI request.
 * By default it just uses {@link SimpleLtiLoginService} to redirect to the root of the webapp as long as the user
 * is authenticated.
 */
public class LtiLoginFilter implements Filter {

    private RequestMatcher requestMatcher;
    private LtiLoginService ltiLoginService = new SimpleLtiLoginService();

    public LtiLoginFilter (RequestMatcher requestMatcher) {
        this.requestMatcher = requestMatcher;
    }

    @Override
    public void init(FilterConfig ignored) throws ServletException {
    }

    public void setLtiLoginService(LtiLoginService ltiLoginService) {
        this.ltiLoginService = ltiLoginService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (shouldFilter(request, response, chain)) {
            SecurityContext context = SecurityContextHolder.getContext();
            Object principal = context.getAuthentication().getPrincipal();

            if (principal instanceof LtiPrincipal) {
                // TODO validate that we have the required fields
                PropertyValues propertyValues = new LtiLaunchPropertyValues(request.getParameterMap());
                LtiLaunchData launchData = new LtiLaunchData();
                DataBinder dataBinder = new DataBinder(launchData);
                dataBinder.bind(propertyValues);
                LtiSession ltiSession = new LtiSession();
                ltiSession.setApplicationName(((LtiPrincipal) principal).getTenant());
                ltiSession.setCanvasCourseId(launchData.getCustom().get("canvas_course_id"));
                ltiSession.setCanvasDomain(launchData.getCustom().get("canvas_api_domain"));
                ltiSession.setEid(launchData.getCustom().get("canvas_user_login_id"));
                ltiSession.setLtiLaunchData(launchData);
                ltiLoginService.setLtiSession(ltiSession);
                String view = ltiLoginService.getInitialView((LtiPrincipal) principal);

                // Set the view afterwards as getting the initial view may need the LtiSession
                ltiSession.setInitialViewPath(view);
                ltiLoginService.onLogin((LtiPrincipal)principal, launchData);
                response.sendRedirect(view);
            } else {
                if (principal == null) {
                    throw new AuthenticationCredentialsNotFoundException("No principal found");
                } else {
                    throw new AuthenticationCredentialsNotFoundException("Principal is not an LtiPrincipal");
                }
            }
        } else {
            chain.doFilter(servletRequest, servletResponse);
        }
    }

    protected boolean shouldFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
        return requestMatcher.matches(request);
    }

    @Override
    public void destroy() {
    }
}
