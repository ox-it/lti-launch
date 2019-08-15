package edu.ksu.lti.launch.oauth;

import edu.ksu.lti.launch.security.CanvasInstanceChecker;
import edu.ksu.lti.launch.service.ToolConsumer;
import edu.ksu.lti.launch.service.ToolConsumerService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth.provider.ConsumerAuthentication;
import org.springframework.security.oauth.provider.InvalidOAuthParametersException;
import org.springframework.security.oauth.provider.OAuthAuthenticationHandler;
import org.springframework.security.oauth.provider.token.OAuthAccessProviderToken;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;

/**
 * This switches out the OAuth principal for a LTI principal. The LTI launch has a principal on it, but that's the
 * service who's sending the user, we switch it out for a principal representing the actual user.
 */
public class LtiOAuthAuthenticationHandler implements OAuthAuthenticationHandler {

    private ToolConsumerService toolConsumerService;

    private boolean checkInstance;

    private boolean validateLti;

    private LtiUserAuthorityFactory userAuthorityFactory = new DefaultLtiUserAuthorityFactory();

    public LtiOAuthAuthenticationHandler(ToolConsumerService toolConsumerService) {
        this.toolConsumerService = toolConsumerService;
    }

    public void setUserAuthorityFactory(LtiUserAuthorityFactory userAuthorityFactory) {
        this.userAuthorityFactory = userAuthorityFactory;
    }

    public void setCheckInstance(boolean checkInstance) {
        this.checkInstance = checkInstance;
    }

    public void setValidateLti(boolean validateLti) {
        this.validateLti = validateLti;
    }

    @Override
    public Authentication createAuthentication(HttpServletRequest request,
                                               ConsumerAuthentication consumerAuthentication,
                                               OAuthAccessProviderToken authToken) {
        if (validateLti) {
            validateBasicLaunchRequest(request);
        }

        String name = request.getParameter("custom_canvas_user_login_id");
        if (name == null || name.isEmpty()) {
            name = request.getParameter("lis_person_sourcedid");
        }
        String key = consumerAuthentication.getConsumerCredentials().getConsumerKey();
        ToolConsumer consumer = toolConsumerService.getConsumer(key);
        if (consumer == null) {
            throw new InvalidOAuthParametersException("Failed to lookup tool consumer for: " + key);
        }

        String resourceId = request.getParameter("resource_id");
        // According to the spec context_id is optional.
        String context = request.getParameter("context_id");
        if (context == null || context.isEmpty()) {
            context = resourceId;
        }

        LtiPrincipal principal = new LtiPrincipal(consumer, name, context);

        HashSet<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_LTI_USER"));
        authorities.addAll(userAuthorityFactory.getLtiUserAuthorities(request.getParameter("roles")));

        String isRootAdmin = request.getParameter("custom_canvas_user_isrootaccountadmin");
        if (Boolean.parseBoolean(isRootAdmin)) {
            authorities.add(new RootAccountAdminAuthority());
        }

        Authentication authentication = new LtiAuthenticationToken(consumerAuthentication.getConsumerCredentials(), principal, authorities);

        // TODO This shouldn't be in the core code and should be listening for authentication events.
        // We do this after checking authentication as we will present the return URL and don't
        // want people to be able to fake this.
        if (checkInstance) {
            String consumerUrl = consumer.getUrl();
            if (consumerUrl == null || consumerUrl.isEmpty()) {
                throw new IllegalArgumentException("Not URL set on " + key + " but we are set to check instances.");
            }
            CanvasInstanceChecker checker = new CanvasInstanceChecker(consumerUrl, null);
            checker.validateInstance(request);
        }
        return authentication;
    }

    private void validateBasicLaunchRequest(HttpServletRequest request) {
        // We can cope with this being null
        String returnUrl = request.getParameter("launch_presentation_return_url");

        {
            String resourceLinkId = request.getParameter("resource_link_id");
            if (resourceLinkId == null || resourceLinkId.isEmpty()) {
                // This should be replaced with a factory call so that when we don't have a return URL we have a different exception which displays a message instead.
                throw new InvalidLtiLaunchException("Missing required parameter: resource_link_id", returnUrl);
            }
        }
        {
            String ltiVersion = request.getParameter("lti_version");
            if (ltiVersion == null) {
                throw new InvalidLtiLaunchException("Missing required parameter: lti_version", returnUrl);
            }

            if (!"LTI-1p0".equals(ltiVersion)) {
                throw new InvalidLtiLaunchException("Unsupported LTI version: " + ltiVersion, returnUrl);
            }
        }
        {
            String ltiMessageType = request.getParameter("lti_message_type");
            if (ltiMessageType == null) {
                throw new InvalidLtiLaunchException("Missing required parameter: lti_message_type", returnUrl);
            }

            if (!"basic-lti-launch-request".equals(ltiMessageType)) {
                throw new InvalidLtiLaunchException("Unsupported lti_message_type: " + ltiMessageType, returnUrl);
            }
        }
    }

}
