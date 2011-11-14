package architecture.ee.spring.security.authentication;

import architecture.ee.security.authentication.AuthenticationProvider;


public interface ExtendedAuthenticationProvider extends AuthenticationProvider {
	
	public abstract ExtendedAuthentication getAuthentication();

	public abstract ExtendedSecurityContext getSecurityContext();
}
