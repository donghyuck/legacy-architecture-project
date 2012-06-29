package architecture.security.user.spring.authentication;

import org.springframework.security.core.Authentication;

import architecture.common.user.User;
import architecture.common.user.authentication.AuthToken;

public interface ExtendedAuthentication extends Authentication {

	public abstract AuthToken getAuthToken();
	
	public abstract User getUser();
	
	public abstract long getUserId();
	
	public abstract boolean isAnonymous();
	
}
