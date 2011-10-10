package architecture.ee.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import architecture.common.lifecycle.ApplicationHelperFactory;
import architecture.common.util.ImplFactory;

import architecture.ee.security.authentication.AuthToken;
import architecture.ee.user.User;

public class SecurityHelper {

	public static interface Implementation {
		
		public boolean isAnonymous(Authentication authen);
		
		public  boolean isApplicaitonUser();
		
		public  AuthToken getAuthToke();
		
		public User getUser();
	}
	
	private static Implementation impl = null;
    
    static 
    {
        impl = (Implementation)ImplFactory.loadImplFromKey(ApplicationHelperFactory.Implementation.class);
    }
	
    
    public static boolean isAnonymous(Authentication authen){
    	return impl.isAnonymous(authen);
    }
    
    public static boolean isApplicaitonUser(){
    	return impl.isApplicaitonUser();
    }
    
    public static AuthToken getAuthToke(){
    	return impl.getAuthToke();
    }
    
    public static User getUser(){
    	return impl.getUser();
    }    
    
	public static Authentication getAuthentication(){
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authen = context.getAuthentication();		
		return authen;
	}
	
	public static List<String> getUserRoles(){		
		Collection<GrantedAuthority> authorities = getAuthentication().getAuthorities();		
		List<String> list = new ArrayList<String>(authorities.size());		
		for( GrantedAuthority authority : authorities ){
			list.add( authority.getAuthority() );
		}
		return list;
	}	
	
}
