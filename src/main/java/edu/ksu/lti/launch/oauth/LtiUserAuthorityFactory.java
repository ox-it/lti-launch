package edu.ksu.lti.launch.oauth;

import edu.ksu.lti.launch.model.InstitutionRole;

/**
 * Factory for creating LtiUserAuthorities.
 *
 * @see CustomLtiUserAuthority
 * @see StandardLtiUserAuthority
 */
public class LtiUserAuthorityFactory {

    public LtiUserAuthority getLtiUserAuthority(String role) {
        InstitutionRole institutionRole = InstitutionRole.fromString(role);
        if (institutionRole == null) {
            return new CustomLtiUserAuthority(role);
        } else {
            return new StandardLtiUserAuthority(institutionRole);
        }
    }

}
