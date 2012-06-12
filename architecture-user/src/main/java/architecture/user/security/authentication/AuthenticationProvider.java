package architecture.user.security.authentication;

import architecture.user.User;

public interface AuthenticationProvider {
	   
    public abstract User getUser();
    
    public abstract AuthToken getAuthToken();
    
    public abstract boolean isSystemAdmin(); 
    
}
