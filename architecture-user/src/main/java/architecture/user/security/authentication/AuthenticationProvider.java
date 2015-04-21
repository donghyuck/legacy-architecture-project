package architecture.user.security.authentication;

import org.springframework.security.core.Authentication;

import architecture.common.user.User;
import architecture.common.user.authentication.AuthToken;

public interface AuthenticationProvider {
	   
	public abstract Authentication getAuthentication();
	
    public abstract User getUser();
    
    public abstract AuthToken getAuthToken();
    
    public abstract boolean isSystemAdmin(); 
    
    public abstract boolean isUserInRole(String role);
    
}
