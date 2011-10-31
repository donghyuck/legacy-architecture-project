package architecture.ee.security.authentication.impl;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

public class ExtendedAnonymousAuthenticationFilter extends AnonymousAuthenticationFilter {

	private Log log = LogFactory.getLog(getClass());
	
	@Override
	protected Authentication createAuthentication(HttpServletRequest request) {

		log.debug( "getKey()=" + getKey() );
		
		log.debug( "getUserAttribute().getPassword()=" + getUserAttribute().getPassword() );
		log.debug( "getUserAttribute().getAuthorities()=" + getUserAttribute().getAuthorities() );
		
		
		ExtendedAnonymousAuthenticationToken auth = 
			new ExtendedAnonymousAuthenticationToken( getKey(), getUserAttribute().getPassword(), getUserAttribute().getAuthorities());
		
		log.debug( "auth=" + auth );
		log.debug( "user=" + auth.getUser() );

        auth.setDetails(new ExtendedUserDetails(auth));

        return auth;
	}

}
