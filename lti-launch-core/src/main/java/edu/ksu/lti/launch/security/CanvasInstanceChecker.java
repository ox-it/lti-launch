package edu.ksu.lti.launch.security;

import edu.ksu.lti.launch.exception.InvalidInstanceException;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * Checks which Canvas instance this application is running in.
 * When the periodic overwrite of beta/test happens in hosted Canvas instances,
 * you can end up with Canvas test pointing at production LTI applications. This
 * can potentially result in test data being pushed into production SIS or other
 * systems that this LTI application may interact with.
 * <p>
 * This checker is invoked during launch and will throw an exception if the
 * Canvas URL coming in from the LTI launch request doesn't match the configured
 * Canvas URL or secondary URL if you have a vanity domain.
 *
 */
public class CanvasInstanceChecker {

    private final String canvasUrl;
    private final String secondCanvasUrl;

    public CanvasInstanceChecker(String canvasUrl, String secondCanvasUrl) {
        Objects.requireNonNull(canvasUrl, "canvasUrl cannot be null");
        this.canvasUrl = canvasUrl;
        this.secondCanvasUrl = secondCanvasUrl;
    }

    public void validateInstance(HttpServletRequest request) {
        String launchUrl = removeLocalPart(request.getParameter("custom_canvas_api_domain"));
        if (launchUrl == null || launchUrl.isEmpty()) {
            launchUrl = domainFromUrl(request.getParameter("launch_presentation_return_url"));
        }
        validateInstance(launchUrl);
    }

    public void validateInstance(String canvasHostname) {
        if (canvasHostname == null || canvasHostname.isEmpty()) {
            return;
        }
        String canvasDomain = domainFromUrl(canvasUrl);
        String secondCanvasDomain = domainFromUrl(secondCanvasUrl);
        validateDomain(canvasHostname, canvasDomain);
        validateDomain(canvasHostname, secondCanvasDomain);
    }

    private static void validateDomain(String requestDomain, String configDomain) {
        if (configDomain != null && !configDomain.isEmpty() && requestDomain != null && !requestDomain.equalsIgnoreCase(configDomain)) {
            throw new InvalidInstanceException(requestDomain, configDomain);
        }
    }

    private static String domainFromUrl(String url) {
        if (url == null || url.isEmpty()) {
            return "";
        }
        url = removeProtocol(url);
        return removeLocalPart(url);
    }

    private static String removeProtocol(String url) {
        int firstSlashIndex = url.indexOf("/");
        return (firstSlashIndex > 0)?url.substring(firstSlashIndex + 2):url;
    }

    private static String removeLocalPart(String url) {
        if (url != null) {
            int firstSlashIndex = url.indexOf('/');
            if (firstSlashIndex > 0) {
                return url.substring(0, firstSlashIndex);
            }
        }
        return url;
    }
}
