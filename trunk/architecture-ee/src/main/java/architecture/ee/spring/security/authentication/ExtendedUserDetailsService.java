package architecture.ee.spring.security.authentication;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import architecture.common.event.api.EventPublisher;
import architecture.common.event.api.EventSource;
import architecture.ee.security.RoleManager;
import architecture.ee.security.role.Role;
import architecture.ee.user.User;
import architecture.ee.user.UserManager;
import architecture.ee.user.UserTemplate;

public class ExtendedUserDetailsService implements UserDetailsService, EventSource {

	private Log log = LogFactory.getLog(getClass());
	private EventPublisher eventPublisher;	
	private boolean caseInsensitive;
	private UserManager userManager;
	private RoleManager roleManager;
	
    public ExtendedUserDetailsService(boolean caseInsensitive) {
		this.caseInsensitive = caseInsensitive;
	}

	public void setCaseInsensitive(boolean caseInsensitive)
    {
        this.caseInsensitive = caseInsensitive;
    }

	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		
		User user = userManager.getUser(new UserTemplate(username), caseInsensitive);
        if(null == user)
        {
            String message = (new StringBuilder()).append("No user found for username '").append(username).append("'.").toString();
            log.info(message);
            throw new UsernameNotFoundException(message);
        } else
        {
            return loadByUser(user);
        }
	}

	public ExtendedUserDetails loadByUser(User user) {
		
		if(eventPublisher != null){
			// fire event;
			
		}
		
		StringBuilder builder = new StringBuilder();
		List<Role> roles = roleManager.getFinalUserRoles(user.getUserId());
		
		for(Role r : roles){
			builder.append(r.getName());
			builder.append(",");			
		}
		
		List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(builder.toString());
		
		return new ExtendedUserDetails(user, authorities);
	}

	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

}