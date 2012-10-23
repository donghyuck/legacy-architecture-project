package architecture.security.util.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import architecture.common.user.User;
import architecture.common.user.User.Status;
import architecture.common.user.authentication.AnonymousUser;
import architecture.common.user.authentication.AuthToken;
import architecture.security.spring.userdetails.ExtendedUserDetails;
import architecture.security.util.SecurityHelper.Implementation;


public class SecurityHelperImpl implements Implementation {

	private Log log = LogFactory.getLog(SecurityHelperImpl.class);
	
	public Authentication getAuthentication(){
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authen = context.getAuthentication();		
		return authen;
	}

	public AuthToken getAuthToken() {		
		try {
			Authentication authen = getAuthentication();	
			Object obj = authen.getPrincipal();
			if ( obj instanceof AuthToken )
				return (AuthToken)obj;
		} catch (Exception ignore) {
		}		
		return new AnonymousUser();
	}

	public User getUser() {
		try {
			SecurityContext context = SecurityContextHolder.getContext();
			Authentication authen = context.getAuthentication();
			Object obj = authen.getPrincipal();
			if ( obj instanceof ExtendedUserDetails )
				return ((ExtendedUserDetails)obj).getUser();
		} catch (Exception ignore) {
		}			
		return new AnonymousUser();
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
