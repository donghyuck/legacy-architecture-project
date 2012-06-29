package architecture.security.user.spring.authentication;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import architecture.common.user.User;
import architecture.common.user.authentication.AnonymousUser;
import architecture.common.user.authentication.AuthToken;



public class ExtendedAnonymousAuthenticationToken implements ExtendedAuthentication {

	private static final long serialVersionUID = -2329724007952323111L;
	private final AnonymousUser user = new AnonymousUser();
	private Authentication token;
	private ExtendedUserDetails details ;
	
	public ExtendedAnonymousAuthenticationToken(Authentication token) {
		this.token = token;
		this.details = new ExtendedUserDetails(user, token);
	}
	
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return token.getAuthorities();
	}
	public Object getCredentials() {
		return token.getCredentials();
	}
	public Object getDetails() {		
		return details;
	}
	public Object getPrincipal() {
		return token.getPrincipal();
	}
	public boolean isAuthenticated() {
		return token.isAuthenticated();
	}
	public void setAuthenticated(boolean isAuthenticated)throws IllegalArgumentException {
		token.setAuthenticated(isAuthenticated);		
	}
	public String getName() {
		return token.getName();
	}
	
	public AuthToken getAuthToken() {
		return user;
	}
	
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
