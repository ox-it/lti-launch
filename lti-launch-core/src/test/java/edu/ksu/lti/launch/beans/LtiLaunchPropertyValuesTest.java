package edu.ksu.lti.launch.beans;

import edu.ksu.lti.launch.model.LtiLaunchData;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.DataBinder;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class LtiLaunchPropertyValuesTest {

    private Map<String, String> map;
    private LtiLaunchData data;
    private DataBinder dataBinder;

    @Before
    public void setUp() {
        map = new HashMap<>();
        data = new LtiLaunchData();
        dataBinder = new DataBinder(data);
    }

    @Test
    public void testSimpleBinder() {
        map.put("lti_version", "1.0");
        map.put("context_label", "context label");
        dataBinder.bind(new LtiLaunchPropertyValues(map));
        assertEquals("1.0", data.getLtiVersion());
        assertEquals("context label", data.getContextLabel());
    }

    @Test
    public void testExistingCustom() {
        // This one doesn't need changing
        map.put("custom[key_unmapped]", "unmapped value");
        dataBinder.bind(new LtiLaunchPropertyValues(map));
        assertEquals("unmapped value", data.getCustom().get("key_unmapped"));
    }

    @Test
    public void testCustomReMapping() {
        map.put("custom_key", "value");
        dataBinder.bind(new LtiLaunchPropertyValues(map));
        assertEquals("value", data.getCustom().get("key"));
    }

    @Test
    public void testIgnoreValues() {
        map.put("unused", "value");
        dataBinder.bind(new LtiLaunchPropertyValues(map));
    }

    @Test
    public void testMultipleCustomValues() {
        map.put("custom_key_1", "value 1");
        map.put("custom_key_2", "value 2");
        map.put("custom_key_3", "value 3");
        dataBinder.bind(new LtiLaunchPropertyValues(map));
        assertEquals("value 1", data.getCustom().get("key_1"));
        assertEquals("value 2", data.getCustom().get("key_2"));
        assertEquals("value 3", data.getCustom().get("key_3"));
    }

}
