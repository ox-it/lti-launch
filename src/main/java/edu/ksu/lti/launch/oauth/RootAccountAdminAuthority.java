package edu.ksu.lti.launch.oauth;

import org.springframework.security.core.GrantedAuthority;

/**
 * A custom granted authority for a user who is a root admin.
 * @link https://canvas.instructure.com/doc/api/file.tools_variable_substitutions.html
 */
public class RootAccountAdminAuthority  implements GrantedAuthority {

    public RootAccountAdminAuthority(){
    }

    @Override
    public String getAuthority() {
        return "ROLE_ROOT_ADMIN";
    }
}
