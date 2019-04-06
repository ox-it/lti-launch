package edu.ksu.lti.launch.oauth;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth.provider.OAuthProcessingFilterEntryPoint;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This is a copy of org.springframework.security.web.access.AccessDeniedHandlerImpl but adapted for a failed
 * OAuth launch. This is because in the LTI world we are doing zero legged OAuth and just
 * used to display a nice error to the user.
 *
 * @author Matthew Buckett
 */
public class LtiAuthenticationFilterEntryPoint extends OAuthProcessingFilterEntryPoint {

    // ~ Instance fields
    // ================================================================================================

    private String errorPage;

    // ~ Methods
    // ========================================================================================================

    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        if (!response.isCommitted()) {
            if (errorPage != null) {
                // Put exception into request scope (perhaps of use to a view)
                request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.FORBIDDEN.value());
                request.setAttribute(RequestDispatcher.ERROR_EXCEPTION, authException);
                request.setAttribute(RequestDispatcher.ERROR_REQUEST_URI, request.getRequestURI());
                // forward to error page.
                RequestDispatcher dispatcher = request.getRequestDispatcher(errorPage);
                dispatcher.forward(request, response);
            }
            else {
                response.sendError(HttpStatus.FORBIDDEN.value(),
                    HttpStatus.FORBIDDEN.getReasonPhrase());
            }
        }
    }

    /**
     * The error page to use. Must begin with a "/" and is interpreted relative to the
     * current context root.
     *
     * @param errorPage the dispatcher path to display
     *
     * @throws IllegalArgumentException if the argument doesn't comply with the above
     * limitations
     */
    public void setErrorPage(String errorPage) {
        if ((errorPage != null) && !errorPage.startsWith("/")) {
            throw new IllegalArgumentException("errorPage must begin with '/'");
        }

        this.errorPage = errorPage;
    }
}
