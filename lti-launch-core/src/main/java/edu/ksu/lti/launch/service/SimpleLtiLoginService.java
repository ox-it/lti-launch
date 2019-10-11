package edu.ksu.lti.launch.service;

import edu.ksu.lti.launch.exception.NoLtiSessionException;
import edu.ksu.lti.launch.model.LtiSession;
import edu.ksu.lti.launch.oauth.LtiPrincipal;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Simple LtiLoginService that just redirects to the root of the webapp.
 */
public class SimpleLtiLoginService implements LtiLoginService {

    @Override
    public String getInitialView(LtiPrincipal principal) {
        return "/";
    }

    @Override
    public LtiSession getLtiSession() throws NoLtiSessionException {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest req = sra.getRequest();
        HttpSession session = req.getSession();
        LtiSession ltiSession = (LtiSession) session.getAttribute(SimpleLtiLoginService.class.getName());
        if (ltiSession == null) {
            throw new NoLtiSessionException();
        }
        return ltiSession;
    }

    @Override
    public void setLtiSession(LtiPrincipal principal, LtiSession ltiSession) {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest req = sra.getRequest();
        HttpSession session = req.getSession();
        session.setAttribute(SimpleLtiLoginService.class.getName(), ltiSession);
    }
}
