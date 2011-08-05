package architecture.ee.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import architecture.ee.security.AuthToken;
import architecture.ee.security.authentication.AnonymousUser;
import architecture.ee.spring.security.authentication.ExtendedAuthentication;
import architecture.ee.spring.security.authentication.ExtendedUserDetails;
import architecture.ee.user.User;

public class SecurityHelper {

	//private static final Log log = LogFactory.getLog(SecurityHelper.class);
	
	
	public static Authentication getAuthentication(){
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authen = context.getAuthentication();		
		return authen;
	}
	
	public static AuthToken getAuthToke(){
		Authentication authen = getAuthentication();		
		if( authen instanceof ExtendedAuthentication )
			return ((ExtendedAuthentication)authen).getAuthToken();		
		Object obj = authen.getPrincipal();
		if ( obj instanceof AuthToken )
			return (AuthToken)obj;				
		return new AnonymousUser();
	}
	
	public static User getUser(){		
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authen = context.getAuthentication();		
		if( authen instanceof ExtendedAuthentication )
			return ((ExtendedAuthentication)authen).getUser();		
		Object obj = authen.getPrincipal();
		if ( obj instanceof ExtendedUserDetails )
			return ((ExtendedUserDetails)obj).getUser();		
		return new AnonymousUser();
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
