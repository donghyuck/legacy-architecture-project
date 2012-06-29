package architecture.security.authentication;

import architecture.common.user.User;
import architecture.common.user.authentication.AuthToken;


public interface AuthenticationProvider {
	   
    public abstract User getUser();
    
    public abstract AuthToken getAuthToken();
    
    public abstract boolean isSystemAdmin(); 
    
}
