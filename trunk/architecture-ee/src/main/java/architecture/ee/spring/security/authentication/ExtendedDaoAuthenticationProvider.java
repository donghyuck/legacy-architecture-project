package architecture.ee.spring.security.authentication;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import architecture.ee.user.UserManager;
import architecture.ee.user.UserTemplate;

/**
 * @author  donghyuck
 */
public class ExtendedDaoAuthenticationProvider extends DaoAuthenticationProvider {
	
	private Log log = LogFactory.getLog(getClass());
    
	/**
	 * @uml.property  name="userManager"
	 * @uml.associationEnd  
	 */
	protected UserManager userManager;
    
    protected AuthenticationProvider authProvider;

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		if(authentication.getCredentials() == null)
            throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));

		String enc = this.getPasswordEncoder().encodePassword(userDetails.getPassword(), this.getSaltSource().getSalt(userDetails));
		
		//log.debug( userDetails.getPassword() + ">>" + enc);
		
		super.additionalAuthenticationChecks(userDetails, authentication);	


		
		if(!supports(userDetails)){
            log.error("Unable to coerce user detail to ExtendedUserDetailsAdapter.");
            throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
		}
		
		ExtendedUserDetails user = (ExtendedUserDetails) userDetails;
		
		if( user.getUser() != null){
			 UserTemplate template = new UserTemplate(user.getUser());
			 template.setLastLoggedIn(new Date());
			 try {
				userManager.updateUser(template);
			} catch (Exception e) {
				log.warn((new StringBuilder()).append("Couldn't update LastLoggedIn date for user ").append(user).toString());
			}
		}
		
	}
	

	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		 try
	        {
	            return super.authenticate(authentication);
	        }
	        catch(AuthenticationException e)
	        {
	            log.info((new StringBuilder()).append("Unable to authenticate user ").append(authentication != null ? authentication.getName() : "<unknown>").append(" ").append(e.getMessage()).toString());
	            log.trace("Unable to authenticat detail", e);
	            throw e;
	        }
	        catch(RuntimeException e)
	        {
	            log.warn((new StringBuilder()).append("Unexpected exception authenticating user ").append(authentication != null ? authentication.getName() : "<unknown>").toString(), e);
	            throw e;
	        }
	}

	public void setAuthenticationProvider(AuthenticationProvider authProvider) {
		this.authProvider = authProvider;
	}
	
    /**
	 * @param userManager
	 * @uml.property  name="userManager"
	 */
    public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
    

	public boolean supports(UserDetails userDetails) {
        return userDetails instanceof ExtendedUserDetails ;
    }
    
    
}
