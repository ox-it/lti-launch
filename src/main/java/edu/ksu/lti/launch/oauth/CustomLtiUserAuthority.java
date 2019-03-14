package edu.ksu.lti.launch.oauth;

/**
 * This is a role outside the standard LTI roles.
 */
public class CustomLtiUserAuthority implements LtiUserAuthority {

    private final String role;

    public CustomLtiUserAuthority(String role) {
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
}
