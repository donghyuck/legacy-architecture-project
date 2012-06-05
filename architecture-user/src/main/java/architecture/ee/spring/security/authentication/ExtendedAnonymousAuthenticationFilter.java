package architecture.ee.spring.security.authentication;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

public class ExtendedAnonymousAuthenticationFilter extends AnonymousAuthenticationFilter {

	private Log log = LogFactory.getLog(getClass());
	
	public ExtendedAnonymousAuthenticationFilter(String key, Object principal, String authorities) {		
		super(key, principal, AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
	}

	@Override
	protected Authentication createAuthentication(HttpServletRequest request) {		
		Authentication auth = super.createAuthentication(request);		
		ExtendedAnonymousAuthenticationToken authen = new ExtendedAnonymousAuthenticationToken(auth);
        return authen;
	}

}
