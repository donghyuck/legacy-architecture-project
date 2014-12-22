package architecture.user.util.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;

import architecture.common.user.SecurityHelper.Implementation;
import architecture.common.user.User;
import architecture.common.user.User.Status;
import architecture.common.user.authentication.AnonymousUser;
import architecture.common.user.authentication.AuthToken;
import architecture.user.security.authentication.AuthenticationProviderFactory;


public class SecurityHelperImpl implements Implementation {

	private Log log = LogFactory.getLog(SecurityHelperImpl.class);
	
	public Authentication getAuthentication(){
		//SecurityContext context = SecurityContextHolder.getContext();
		//Authentication authen = context.getAuthentication();		
		return AuthenticationProviderFactory.getSecurityContextAuthenticationProvider().getAuthentication();
		//return authen;
	}

	public AuthToken getAuthToken() {		
		/*try {
			Authentication authen = getAuthentication();	
			Object obj = authen.getPrincipal();
			if ( obj instanceof AuthToken )
				return (AuthToken)obj;
		} catch (Exception ignore) {
		}		
		return new AnonymousUser();*/
		return AuthenticationProviderFactory.getSecurityContextAuthenticationProvider().getAuthToken();
	}

	public User getUser() {
		try {
			/*Authentication authen = getAuthentication();
			Object obj = authen.getPrincipal();
			if ( obj instanceof ExtendedUserDetails )
				return ((ExtendedUserDetails)obj).getUser();*/
			return AuthenticationProviderFactory.getSecurityContextAuthenticationProvider().getUser();
		} catch (Exception ignore) {
		}			
		return new AnonymousUser();
	}
	
	
	public boolean isUserInRole(String role){
		return AuthenticationProviderFactory.getSecurityContextAuthenticationProvider().isUserInRole(role);
	}

	public void checkUserStatus(String username, Status status) {
		
		/*if(status == User.Status.awaiting_validation)
        {
            log.info(String.format("Authentication attempt by unregistered user '%s'.", new Object[] {
                username
            }));
            throw new ActivationRequiredException("Account not yet activated.");
        }
        if(status == User.Status.awaiting_moderation || status == User.Status.rejected)
        {
            log.info(String.format("Authentication attempt by unapproved user '%s'.", new Object[] {
                username
            }));
            throw new ApprovalRequiredException("Account not yet approved.");
        }
        if(status != com.jivesoftware.base.User.Status.registered)
        {
            log.info(String.format("Authentication attempt by user, '%s', not in registered state, their current state is '%s'.", new Object[] {
                username, status
            }));
            throw new UserNotRegisteredException("User not properly registered");
        } else
        {
            return;
        }*/
	}
}
