package architecture.user.util;

import org.springframework.security.core.Authentication;

import architecture.common.user.User;
import architecture.common.user.authentication.AuthToken;
import architecture.common.util.ImplFactory;

/**
 * 
 * 
 * 
 * @author  <a href="mailto:donghyuck.son@gmail.com">Donghyuck Son </a>
 *
 */
public class SecurityHelper {
	
	public static interface Implementation {
		
		public void checkUserStatus(String username, User.Status status);
		
		public Authentication getAuthentication();
		
		public AuthToken getAuthToken();
		
		public User getUser();
		
	}

	private static Implementation impl = null;
    
    static 
    {
        impl = (Implementation)ImplFactory.loadImplFromKey(SecurityHelper.Implementation.class);
    }

    public static void checkUserStatus(String username, User.Status status){
    	impl.checkUserStatus(username, status);
    }
    
    public static AuthToken getAuthToken(){
    	return impl.getAuthToken();
    }
    
    public static User getUser(){
    	return impl.getUser();
    }    
    
	public static Authentication getAuthentication(){
		return impl.getAuthentication();
	}	
		
}
