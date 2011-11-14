package architecture.ee.spring.security.authentication;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import architecture.ee.security.authentication.AnonymousUser;
import architecture.ee.security.authentication.AuthToken;
import architecture.ee.user.User;

public class ExtendedAuthenticationProviderImpl implements ExtendedAuthenticationProvider {
	
	private Log log = LogFactory.getLog(getClass());

	public ExtendedAuthentication getAuthentication() {		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();        
		if(null == auth)
        {
            log.debug("No authentication associated with current context. Defaulting to anonymous");
           // return new ExtendedAnonymousAuthenticationToken();
        } else
        {
            return (ExtendedAuthentication)auth;
        }
		return null;
	}

	public AuthToken getAuthToken() {
		
		ExtendedAuthentication authentication = getAuthentication();
        
		if(null == authentication || !authentication.isAuthenticated())
            return new AnonymousUser();
        else
            return authentication.getAuthToken();
	
	}

	public ExtendedSecurityContext getSecurityContext() {
	
		return (ExtendedSecurityContext)SecurityContextHolder.getContext();
	
	}

	public User getUser() {
    
		User user = getAuthentication().getUser();
        return ((User) (user != null ? user : new AnonymousUser()));
	
	}

	public boolean isSystemAdmin() {
		
		return false;
	
	}
	
}
