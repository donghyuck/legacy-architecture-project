package architecture.user.spring.security.authentication;

import org.springframework.security.core.Authentication;

import architecture.user.User;
import architecture.user.security.authentication.AuthToken;

public interface ExtendedAuthentication extends Authentication {

	public abstract AuthToken getAuthToken();
	
	public abstract User getUser();
	
	public abstract long getUserId();
	
	public abstract boolean isAnonymous();
	
}
