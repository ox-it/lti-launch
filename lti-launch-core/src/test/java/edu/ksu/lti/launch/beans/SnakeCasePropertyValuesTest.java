package edu.ksu.lti.launch.beans;

import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class SnakeCasePropertyValuesTest {

    @Test
    public void testNull() {
        SnakeCasePropertyValues values = new SnakeCasePropertyValues(null);
        assertEquals(0, values.size());
    }

    @Test
    public void testUnchanged() {
        SnakeCasePropertyValues values = new SnakeCasePropertyValues(Collections.singletonMap("key", "value"));
        assertEquals(1, values.size());
        assertEquals("value", values.get("key"));
    }

    @Test
    public void testCamelCase() {
        SnakeCasePropertyValues values = new SnakeCasePropertyValues(Collections.singletonMap("newKey", "value"));
        assertEquals(1, values.size());
        assertEquals("value", values.get("newKey"));
    }

    @Test
    public void testSnakeCase() {
        SnakeCasePropertyValues values = new SnakeCasePropertyValues(Collections.singletonMap("new_key", "value"));
        assertEquals(1, values.size());
        assertEquals("value", values.get("newKey"));
    }

    @Test
    public void testSnakeCaseLong() {
        SnakeCasePropertyValues values = new SnakeCasePropertyValues(Collections.singletonMap("new_key_for_test", "value"));
        assertEquals(1, values.size());
        assertEquals("value", values.get("newKeyForTest"));
    }

    @Test
    public void testSnakeCaseMultipleSeparators() {
        SnakeCasePropertyValues values = new SnakeCasePropertyValues(Collections.singletonMap("new____key", "value"));
        assertEquals(1, values.size());
        assertEquals("value", values.get("newKey"));
    }

    @Test
    public void testSnakeCaseLeadingTrailing() {
        SnakeCasePropertyValues values = new SnakeCasePropertyValues(Collections.singletonMap("_new_key_", "value"));
        assertEquals(1, values.size());
        assertEquals("value", values.get("newKey"));
    }

    @Test(expected = IllegalStateException.class)
    public void testOverlappingKeys() {
        Map<String, String> map = new HashMap<>();
        map.put("new_key", "new_key");
        map.put("new__key", "new__key");
        map.put("newKey", "newKey");

        new SnakeCasePropertyValues(map);
    }

}
