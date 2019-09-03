package edu.ksu.lti.launch.model;

import edu.ksu.lti.launch.beans.SnakeCasePropertyValues;
import org.junit.Test;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.validation.DataBinder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class LtiLaunchDataTest {

    @Test
    public void testBinder() {
        LtiLaunchData data = new LtiLaunchData();
        DataBinder dataBinder = new DataBinder(data);
        Map<String, String> map = Collections.singletonMap("lti_version", "1.0");
        MutablePropertyValues values = new SnakeCasePropertyValues(map);
        values.add("contextLabel", "context label");
        dataBinder.bind(values);
        assertEquals("1.0", data.getLtiVersion());
        assertEquals("context label", data.getContextLabel());
    }

}
