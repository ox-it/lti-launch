package edu.ksu.lti.launch.oauth;

import org.springframework.security.core.GrantedAuthority;

/**
 * A custom granted authority for a user who is a root admin.
 * @see <a href="https://canvas.instructure.com/doc/api/file.tools_variable_substitutions.html">https://canvas.instructure.com/doc/api/file.tools_variable_substitutions.html</a>
 */
public class RootAccountAdminAuthority  implements GrantedAuthority {

    public RootAccountAdminAuthority(){
    }

    @Override
    public String getAuthority() {
        return "ROLE_ROOT_ADMIN";
    }
}
