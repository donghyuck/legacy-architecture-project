package architecture.ee.security.authentication.impl;

import architecture.ee.security.authentication.AuthenticationProvider;


public interface ExtendedAuthenticationProvider extends AuthenticationProvider {
	
	public abstract ExtendedAuthentication getAuthentication();

	public abstract ExtendedSecurityContext getSecurityContext();
}
