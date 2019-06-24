package edu.ksu.lti.launch.oauth;

import edu.ksu.lti.launch.model.InstitutionRole;

import java.util.*;

/**
 * Factory for creating LtiUserAuthorities.
 *
 * @see CustomLtiUserAuthority
 * @see StandardLtiUserAuthority
 */
public class LtiUserAuthorityFactory {

    /**
     * Gets a single LTIUserAuthority
     * @param role The role to parse.
     * @return The parsed LTIUserAuthority or <code>null</code> if supplied role is empty of <code>null</code>.
     */
    public LtiUserAuthority getLtiUserAuthority(String role) {
        if (role == null || role.isEmpty()) {
            return null;
        }
        InstitutionRole institutionRole = InstitutionRole.fromString(role);
        if (institutionRole == null) {
            return new CustomLtiUserAuthority(role);
        } else {
            return new StandardLtiUserAuthority(institutionRole);
        }
    }

    /**
     * Get LTIUserAuthorities from a command seperated string.
     * @param roles The roles to parse.
     * @return A collection of roles, never <code>null</code>.
     */
    public Collection<LtiUserAuthority> getLtiUserAuthorities(String roles) {
        if (roles == null || roles.isEmpty()) {
            return Collections.emptySet();
        }
        StringTokenizer stringTokenizer = new StringTokenizer(roles, ",");
        Set<LtiUserAuthority> grantedAuthorities = new HashSet<>();
        while (stringTokenizer.hasMoreElements()) {
            grantedAuthorities.add(getLtiUserAuthority(stringTokenizer.nextToken()));
        }
        return grantedAuthorities;
    }
}
