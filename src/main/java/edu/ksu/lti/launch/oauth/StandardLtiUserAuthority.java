package edu.ksu.lti.launch.oauth;

import edu.ksu.lti.launch.model.InstitutionRole;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StandardLtiUserAuthority that = (StandardLtiUserAuthority) o;
        return role == that.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(role);
    }

    @Override
    public String toString() {
        return "StandardLtiUserAuthority{" +
            "role=" + role +
            '}';
    }
}
