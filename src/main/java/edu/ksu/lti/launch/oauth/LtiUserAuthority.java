package edu.ksu.lti.launch.oauth;

import org.springframework.security.core.GrantedAuthority;

public interface LtiUserAuthority extends GrantedAuthority {

    /**
     * @return <code>true</code> if this role is outside the standard LTI roles.
     */
    boolean isCustom();
}
