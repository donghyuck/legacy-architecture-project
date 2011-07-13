package architecture.ee.spring.security.authentication;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import architecture.ee.security.AuthToken;
import architecture.ee.security.authentication.AnonymousUser;
import architecture.ee.user.User;

public class AnonymousAuthentication implements ExtendedAuthentication {
	
	private final AnonymousUser user = new AnonymousUser();

	public Collection<GrantedAuthority> getAuthorities() {
		return AuthorityUtils.createAuthorityList("AUTHENTICATE");
	}

	public Object getCredentials() {
		return "";
	}

	public Object getDetails() {
		return user;
	}

	public Object getPrincipal() {
		return user;
	}

	public boolean isAuthenticated() {
		return true;
	}

	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		
	}

	public String getName() {
		return user.getName();
	}

	public AuthToken getAuthToken() {
		return user;
	}

	public boolean isAnonymous() {
		return true;
	}

	public long getUserId() {
		return user.getUserId();
	}

	public User getUser() {
		return user;
	}
}
