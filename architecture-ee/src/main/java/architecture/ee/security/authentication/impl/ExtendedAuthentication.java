package architecture.ee.security.authentication.impl;

import org.springframework.security.core.Authentication;

import architecture.ee.security.authentication.AuthToken;

import architecture.ee.user.User;

public interface ExtendedAuthentication extends Authentication {

	public abstract AuthToken getAuthToken();
	
	public abstract User getUser();
	
	public abstract long getUserId();
	
	public abstract boolean isAnonymous();
	
}
