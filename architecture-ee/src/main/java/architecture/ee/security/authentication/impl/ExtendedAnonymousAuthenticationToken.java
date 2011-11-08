package architecture.ee.security.authentication.impl;

import java.util.List;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import architecture.ee.security.authentication.AnonymousUser;
import architecture.ee.security.authentication.AuthToken;
import architecture.ee.user.User;

/**
 * @author  donghyuck
 */
public class ExtendedAnonymousAuthenticationToken extends AnonymousAuthenticationToken implements ExtendedAuthentication {
	
	private static final long serialVersionUID = -6513817885158410340L;

	/**
	 * @uml.property  name="user"
	 * @uml.associationEnd  
	 */
	private final AnonymousUser user = new AnonymousUser();
	
	public ExtendedAnonymousAuthenticationToken(String key, Object principal, List<GrantedAuthority> authorities) {
		super(key, principal, authorities);
	}

	public AuthToken getAuthToken() {
		return user;
	}

	/**
	 * @return
	 * @uml.property  name="user"
	 */
	public User getUser() {
		return user;
	}

	public long getUserId() {
		return user.getUserId();
	}

	public boolean isAnonymous() {
		return true;
	}
	
}
