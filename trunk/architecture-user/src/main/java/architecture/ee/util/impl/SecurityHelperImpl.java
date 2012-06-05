package architecture.ee.util.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import architecture.ee.security.authentication.AnonymousUser;
import architecture.ee.security.authentication.AuthToken;
import architecture.ee.spring.security.authentication.ExtendedAuthentication;
import architecture.ee.spring.security.authentication.ExtendedUserDetails;
import architecture.ee.user.User;
import architecture.ee.util.SecurityHelper.Implementation;

public class SecurityHelperImpl implements Implementation {

	private Log log = LogFactory.getLog(SecurityHelperImpl.class);
	
	public boolean isAnonymous(){
		if( getAuthToke().isAnonymous() )
			return true;
		return false;
	}
	
	public boolean isAnonymous(Authentication authen) {
		
		if( authen instanceof AnonymousAuthenticationToken )
		    return true;
		if( authen instanceof ExtendedAuthentication ){
			return ((ExtendedAuthentication)authen).isAnonymous();
		}		
		return false;
	}

	public boolean isApplicaitonUser() {
		Authentication authen = getAuthentication();		
		if( authen instanceof ExtendedAuthentication )
			return true;		
		return false;
	}

	public AuthToken getAuthToke() {		
		Authentication authen = getAuthentication();		
		if( authen instanceof ExtendedAuthentication )
			return ((ExtendedAuthentication)authen).getAuthToken();		
		Object obj = authen.getPrincipal();
		if ( obj instanceof AuthToken )
			return (AuthToken)obj;				
		return new AnonymousUser();
	}

	public User getUser() {
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authen = context.getAuthentication();
		
		if( authen instanceof ExtendedAuthentication )
			return ((ExtendedAuthentication)authen).getUser();		
		
		Object obj = authen.getPrincipal();
		if ( obj instanceof ExtendedUserDetails )
			return ((ExtendedUserDetails)obj).getUser();				
		log.debug(authen);		
		return new AnonymousUser();
	}
	
	public Authentication getAuthentication(){
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authen = context.getAuthentication();		
		return authen;
	}

}
