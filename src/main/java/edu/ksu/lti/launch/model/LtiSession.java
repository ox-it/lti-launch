package edu.ksu.lti.launch.model;

import java.util.Locale;
import java.util.Objects;

/**
 * Class to hold LTI session data. It is created and populated when the LTI application is first
 * launched and then stored in the session for future reference. Some commonly accessed information
 * is stored in their own variables (like eID) but all data we get as part of the LTI launch request
 * is stored in the {@link LtiLaunchData} object if you need it.
 */
public class LtiSession {

    // The tenant that this session is for.
    private String applicationName;
    private String initialViewPath;
    private String eid;
    private String canvasCourseId;
    private String canvasDomain;
    private Locale locale;
    private LtiLaunchData ltiLaunchData;

    public LtiSession(LtiLaunchData launchData) {
        Objects.requireNonNull(launchData, "launchData cannot be null");
        this.ltiLaunchData = launchData;
    }

    public LtiSession() {
        this.ltiLaunchData = new LtiLaunchData();
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setInitialViewPath(String initialViewPath) {
        this.initialViewPath = initialViewPath;
    }

    public String getInitialViewPath() {
        return initialViewPath;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getEid() {
        return eid;
    }

    public void setCanvasCourseId(String canvasCourseId) {
        this.canvasCourseId = canvasCourseId;
    }

    public String getCanvasCourseId() {
        return canvasCourseId;
    }

    public void setCanvasDomain(String canvasDomain) {
        this.canvasDomain = canvasDomain;
    }

    public String getCanvasDomain() {
        return canvasDomain;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public void setLtiLaunchData(LtiLaunchData ltiLaunchData) {
        this.ltiLaunchData = ltiLaunchData;
    }

    public LtiLaunchData getLtiLaunchData() {
        return ltiLaunchData;
    }
    
}
