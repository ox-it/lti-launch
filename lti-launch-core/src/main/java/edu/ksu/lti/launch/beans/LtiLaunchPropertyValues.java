package edu.ksu.lti.launch.beans;

import java.util.Map;

/**
 * This extends the snake_case property values class making sure that the custom values from an LTI launch (that <code>custom_</code>) are put into the nested map.
 */
public class LtiLaunchPropertyValues extends SnakeCasePropertyValues {

    /**
     * The prefix to filter.
     */
    public static final String CUSTOM = "custom";

    /**
     * The prefix that we actually re-map.
     */
    public static final String PREFIX = CUSTOM + "_";

    /**
     * @param original The original map.
     * @throws IllegalStateException if values in the original map collapse to the same key.
     */
    public LtiLaunchPropertyValues(Map<?, ?> original) {
        super();
        if(original != null) {
            original.forEach((key, value) -> {
                if (key.toString().startsWith(CUSTOM)) {
                    // Leave the snake case for the custom parameter keys
                    addPropertyValue(convertKey(key), value);
                } else {
                    addPropertyValue(SnakeCasePropertyValues.convertKey(key), value);
                }
            });
        }
    }

    public static String convertKey(Object object) {
        String key = object.toString();
        if (key.startsWith(PREFIX)) {
            return CUSTOM+ "["+ key.substring(PREFIX.length())+ "]";
        }
        return key;
    }
}
