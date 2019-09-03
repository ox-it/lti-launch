package edu.ksu.lti.launch.oauth;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth.provider.ConsumerCredentials;

import java.util.Collection;

/**
 * An LTI specific authentication token for use with Spring Security.
 */
public class LtiAuthenticationToken extends AbstractAuthenticationToken {

    private ConsumerCredentials credentials;
    private LtiPrincipal principal;
    /**
     * Creates a token with the supplied array of authorities.
     *
     * @param credentials the supplied credentials.
     * @param principal the LTI principal used to authenticate.
     * @param authorities the collection of <code>GrantedAuthority</code>s for the
     *                    principal represented by this authentication object.
     */
    public LtiAuthenticationToken(ConsumerCredentials credentials, LtiPrincipal principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.credentials = credentials;
        this.principal = principal;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public LtiPrincipal getPrincipal() {
        return principal;
    }
}
