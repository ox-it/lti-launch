package edu.ksu.lti.launch.security;

import edu.ksu.lti.launch.exception.InvalidInstanceException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

public class CanvasInstanceCheckerTest {

    private CanvasInstanceChecker checker;
    private MockHttpServletRequest request;

    @Before
    public void setUp() {
        request = new MockHttpServletRequest();
    }

    @Test
    public void testEmptyRequest() {
        checker = new CanvasInstanceChecker("https://example.com", null);
        checker.validateInstance(request);
    }

    @Test
    public void testMatchingApi() {
        checker = new CanvasInstanceChecker("https://example.com", null);
        request.addParameter("custom_canvas_api_domain", "example.com");
        checker.validateInstance(request);
    }

    @Test(expected = InvalidInstanceException.class)
    public void testNonMatchingApi() {
        checker = new CanvasInstanceChecker("https://example.com", null);
        request.addParameter("custom_canvas_api_domain", "other.com");
        checker.validateInstance(request);
    }

    @Test
    public void testMatchingReturn() {
        checker = new CanvasInstanceChecker("https://example.com", null);
        request.addParameter("launch_presentation_return_url", "http://example.com/url");
        checker.validateInstance(request);
    }

    @Test(expected = InvalidInstanceException.class)
    public void testNonMatchingReturn() {
        checker = new CanvasInstanceChecker("https://example.com", null);
        request.addParameter("launch_presentation_return_url", "http://other.com/url");
        checker.validateInstance(request);
    }

    @Test(expected = InvalidInstanceException.class)
    public void testMatchingReturnWithPort() {
        checker = new CanvasInstanceChecker("https://example.com", null);
        request.addParameter("launch_presentation_return_url", "http://example.com:80/url");
        checker.validateInstance(request);
    }

    @Test(expected = InvalidInstanceException.class)
    public void testNonMatchingReturnWithPort() {
        checker = new CanvasInstanceChecker("https://example.com", null);
        request.addParameter("launch_presentation_return_url", "http://other.com:80/url");
        checker.validateInstance(request);
    }
}
