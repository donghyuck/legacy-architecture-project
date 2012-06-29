package architecture.security.user.spring.authentication;

import org.springframework.security.core.context.SecurityContext;

import architecture.user.security.authentication.AuthenticationProvider;


public interface ExtendedAuthenticationProvider extends AuthenticationProvider {
	
	public abstract ExtendedAuthentication getAuthentication();

	public abstract SecurityContext getSecurityContext();
}
