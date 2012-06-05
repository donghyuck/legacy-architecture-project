package architecture.ee.spring.security.authentication;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import architecture.ee.security.authentication.AuthToken;
import architecture.ee.security.authentication.SystemUser;
import architecture.ee.user.User;

/**
 * @author  donghyuck
 */
public class SystemAuthentication implements ExtendedAuthentication {

	/**
	 */
	private static final SystemUser user = new SystemUser();

	public AuthToken getAuthenticationToken() {
		return user;
	}

	public Collection<GrantedAuthority> getAuthorities() {
		return AuthorityUtils.createAuthorityList("AUTHENTICATE");
	}
	
	public AuthToken getAuthToken() {
		return user;
	}

	public Object getCredentials() {
		return user.getPassword();
	}

	public Object getDetails() {
		return null;
	}

	public String getName() {
		return user.getName();
	}

	public Object getPrincipal() {
		return user;
	}

	/**
	 * @return
	 */
	public User getUser() {
		return user;
	}

	public long getUserId() {
		return user.getUserId();
	}

	public boolean isAnonymous() {
		return false;
	}

	public boolean isAuthenticated() {
		return true;
	}

	public void setAuthenticated(boolean arg0) throws IllegalArgumentException {

	}

}
