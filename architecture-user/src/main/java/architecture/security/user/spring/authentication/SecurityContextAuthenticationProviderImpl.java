package architecture.security.user.spring.authentication;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import architecture.common.user.User;
import architecture.common.user.authentication.AuthToken;
import architecture.user.util.SecurityHelper;


public class SecurityContextAuthenticationProviderImpl implements ExtendedAuthenticationProvider {

    public SecurityContextAuthenticationProviderImpl()
    {
    }

	public User getUser() {		
		return SecurityHelper.getUser();
	}

	public AuthToken getAuthToken() {
		return SecurityHelper.getAuthToke();
	}

	public boolean isSystemAdmin() {
        if(getAuthentication() instanceof SystemAuthentication)
            return true;
        else
        	return false;
	}

	public ExtendedAuthentication getAuthentication() {
		return (ExtendedAuthentication)SecurityHelper.getAuthentication();
	}

	public SecurityContext getSecurityContext() {
		return SecurityContextHolder.getContext();
	}
    
}
