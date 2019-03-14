package edu.ksu.lti.launch.oauth;

import edu.ksu.lti.launch.model.InstitutionRole;

/**
 * A Standard LTI Authority.
 */
public class StandardLtiUserAuthority implements LtiUserAuthority {

    private final InstitutionRole role;

    public StandardLtiUserAuthority(InstitutionRole role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role.toString();
    }

    @Override
    public boolean isCustom() {
        return false;
    }
}
