package architecture.ee.security.authentication;

import architecture.ee.security.AuthToken;
import architecture.ee.user.User;

public interface AuthenticationProvider {
	
   // public abstract ExtendedAuthentication getAuthentication();
    
   // public abstract ExtendedSecurityContext getSecurityContext();
    
    public abstract User getUser();
    
    public abstract AuthToken getAuthToken();
    
    public abstract boolean isSystemAdmin();
    
   // public abstract Permissions getPermissions();
    
    
}
