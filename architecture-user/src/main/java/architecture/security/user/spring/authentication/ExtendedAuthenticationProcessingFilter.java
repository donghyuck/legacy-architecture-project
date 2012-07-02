package architecture.security.user.spring.authentication;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import architecture.common.user.UserTemplate;
import architecture.security.user.UserManager;

/**
 * @author  donghyuck
 */
public class ExtendedAuthenticationProcessingFilter extends UsernamePasswordAuthenticationFilter {

	private Log log = LogFactory.getLog(getClass());
	/**
	 */
	private UserManager userManager;

	public void setDefaultTargetUrl(String defaultTargetUrl) {		
		getAuthenticationSuccessHandler().setDefaultTargetUrl(defaultTargetUrl);
	}

	public void setUseReferer(boolean useReferer) {		
		getAuthenticationSuccessHandler().setUseReferer(useReferer);
	}
	
	public void setAlwaysUseDefaultTargetUrl(boolean alwaysUseDefaultTargetUrl) {		
		getAuthenticationSuccessHandler().setAlwaysUseDefaultTargetUrl(alwaysUseDefaultTargetUrl);
	}
	
	public void setTargetUrlParameter(String targetUrlParameter) {		
		getAuthenticationSuccessHandler().setTargetUrlParameter(targetUrlParameter);
	}

	protected ExtendedSavedRequestAwareAuthenticationSuccessHandler getAuthenticationSuccessHandler(){
		Object handler = getSuccessHandler();
		if( handler instanceof ExtendedSavedRequestAwareAuthenticationSuccessHandler ){
			return (ExtendedSavedRequestAwareAuthenticationSuccessHandler)handler;
		}else{
			ExtendedSavedRequestAwareAuthenticationSuccessHandler successHandler = new ExtendedSavedRequestAwareAuthenticationSuccessHandler();
			setAuthenticationSuccessHandler(successHandler);
			return successHandler;
		}
	}
	

	public void setDefaultFailureUrl(String defaultFailureUrl) {
		getAuthenticationFailureHandler().setDefaultFailureUrl(defaultFailureUrl);
	}

	protected ExtendedUrlAuthenticationFailureHandler getAuthenticationFailureHandler(){
		Object handler = getFailureHandler();
		if( handler instanceof ExtendedUrlAuthenticationFailureHandler ){
			return (ExtendedUrlAuthenticationFailureHandler)handler;
		}else{
			ExtendedUrlAuthenticationFailureHandler failerHandler = new ExtendedUrlAuthenticationFailureHandler();
			setAuthenticationFailureHandler(failerHandler);
			return failerHandler;
		}
	}	
	
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		
		if(log.isDebugEnabled())
			log.debug("Beginning form-based authentication attempt.");
		
        Authentication auth = super.attemptAuthentication(request, response);
        
        if(log.isDebugEnabled())
        	log.debug("Form-based authentication attempt complete.");        
        
        UserTemplate user = null;
        try{
	        if(auth != null && auth.isAuthenticated() && !(isAnonymous(auth)) && userManager != null)
	        {	
	        	if(auth instanceof UsernamePasswordAuthenticationToken)
	                user = new UserTemplate( userManager.getUser(((UsernamePasswordAuthenticationToken)auth).getName()));
	            else
	            if(auth instanceof ExtendedAuthentication)
	                user = new UserTemplate(((ExtendedAuthentication)auth).getUser());
	            if(user != null)
	            {
	                user.setLastLoggedIn(new Date());
	                userManager.updateUser(user);
	            }
	        }
        }catch(Exception ex){
            log.warn("Failed to update user last logged in date.", ex);
        }
        
        if(user != null && user.isExternal())
        {
            log.warn((new StringBuilder()).append("Rejecting authentication attempt by external user ").append(user.toString()).append(".").toString());
            throw new UsernameNotFoundException((new StringBuilder()).append(user.getUsername()).append(" not found.").toString());
        } else
        {
            return auth;
        }
        
	}

	private boolean isAnonymous(Authentication auth){
		return auth instanceof ExtendedAnonymousAuthenticationToken || auth instanceof ExtendedAnonymousAuthenticationToken;
	}
	
	/**
	 * @param userManager
	 */
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

}
