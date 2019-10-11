package edu.ksu.lti.launch.beans;

import org.springframework.beans.MutablePropertyValues;

import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

/**
 * Converts a Map from snake_case to CamelCase before creating the MutablePropertyValues. This has the edge case of
 * throwing an exception if values from the map collapse to the same value (one_part, one__part, onePart).
 */
public class SnakeCasePropertyValues extends MutablePropertyValues {

    /**
     * Create an empty property values.
     */
    public SnakeCasePropertyValues() {
        super();
    }

    /**
     * Populate a new property values.
     * @param original The original map.
     * @throws IllegalStateException if values in the original map collapse to the same key.
     */
    public SnakeCasePropertyValues(Map<?, ?> original) {
        super(convert(original));
    }

    public static Map<?, ?> convert(Map<?, ?> original) {
        Map<?, ?> collect = null;
        if (original != null) {
            collect = original.entrySet().stream()
                .collect(Collectors.toMap(e -> convertKey(e.getKey()), Map.Entry::getValue));
        }
        return collect;
    }

    public static String convertKey(Object object) {
        StringBuilder output = new StringBuilder();
        StringTokenizer tokens = new StringTokenizer(object.toString(), "_");
        if (tokens.hasMoreElements()) {
            output.append(tokens.nextToken());
            while (tokens.hasMoreElements()) {
                String part = tokens.nextToken();
                part = capitalise(part);
                output.append(part);
            }
        }
        return output.toString();
    }

    public static String capitalise(String part) {
        if (part.length() == 0) {
            return "";
        } else if (part.length() == 1) {
            return part.toUpperCase();
        } else {
            return part.substring(0, 1).toUpperCase() + part.substring(1);
        }
    }

}
