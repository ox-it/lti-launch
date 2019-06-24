package edu.ksu.lti.launch.oauth;

import java.util.Objects;

/**
 * This is a role outside the standard LTI roles.
 */
public class CustomLtiUserAuthority implements LtiUserAuthority {

    private final String role;

    public CustomLtiUserAuthority(String role) {
        Objects.requireNonNull(role, "role cannot be null.");
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role;
    }

    @Override
    public boolean isCustom() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomLtiUserAuthority that = (CustomLtiUserAuthority) o;
        return Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(role);
    }

    @Override
    public String toString() {
        return "CustomLtiUserAuthority{" +
            "role='" + role + '\'' +
            '}';
    }
}
