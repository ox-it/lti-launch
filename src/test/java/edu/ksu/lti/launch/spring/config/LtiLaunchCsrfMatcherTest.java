package edu.ksu.lti.launch.spring.config;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LtiLaunchCsrfMatcherTest {

    private LtiLaunchCsrfMatcher matcher;
    private MockHttpServletRequest request;

    @Before
    public void setup() {
        matcher = new LtiLaunchCsrfMatcher("/launch");
        request = new MockHttpServletRequest();
    }

    @Test
    public void testLaunch() {
        // This is a LTI launch and shouldn't have CSRF
        request.setMethod("POST");
        request.setPathInfo("/launch");
        assertFalse(matcher.matches(request));
    }

    @Test
    public void testNonLaunchURL() {
        // We still want CSRF inside the application
        request.setMethod("POST");
        request.setPathInfo("/other-url");
        assertTrue(matcher.matches(request));
    }

    @Test
    public void testGETLaunch() {
        // Normal LTI shouldn't be a GET.
        request.setMethod("GET");
        request.setPathInfo("/launch");
        assertFalse(matcher.matches(request));
    }

    @Test
    public void testGETNonLaunchURL() {
        // Check we haven't broken normal GET.
        request.setMethod("GET");
        request.setPathInfo("/other-url");
        assertFalse(matcher.matches(request));
    }

}
