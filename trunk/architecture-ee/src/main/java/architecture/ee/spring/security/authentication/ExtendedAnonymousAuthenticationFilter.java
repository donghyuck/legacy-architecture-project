package architecture.ee.spring.security.authentication;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

public class ExtendedAnonymousAuthenticationFilter extends AnonymousAuthenticationFilter {

	private Log log = LogFactory.getLog(getClass());
	private String key ;
	
	public ExtendedAnonymousAuthenticationFilter(String key, Object principal, List<GrantedAuthority> authorities) {
		super(key, principal, authorities);
		this.key = key;
	}

	@Override
	protected Authentication createAuthentication(HttpServletRequest request) {

		log.debug( "getPrincipal() -" + getPrincipal() );
		log.debug( "getAuthorities() -" + getAuthorities() );		
		
		ExtendedAnonymousAuthenticationToken auth =  new ExtendedAnonymousAuthenticationToken( key, getPrincipal(), getAuthorities());
        auth.setDetails(new ExtendedUserDetails(auth));

        return auth;
	}

}
