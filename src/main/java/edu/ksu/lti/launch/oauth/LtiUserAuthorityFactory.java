package edu.ksu.lti.launch.oauth;

import java.util.Collection;

public interface LtiUserAuthorityFactory {
    /**
     * Get LTIUserAuthorities from a command seperated string.
     * @param roles The roles to parse.
     * @return A collection of roles, never <code>null</code>.
     */
    Collection<LtiUserAuthority> getLtiUserAuthorities(String roles);
}
