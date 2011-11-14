package architecture.ee.spring.security.authentication;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import architecture.common.event.api.EventPublisher;
import architecture.common.event.api.EventSource;
import architecture.ee.security.role.Role;
import architecture.ee.security.role.RoleManager;
import architecture.ee.user.User;
import architecture.ee.user.UserManager;
import architecture.ee.user.UserTemplate;

/**
 * @author  donghyuck
 */
public class ExtendedUserDetailsService implements UserDetailsService, EventSource {

	private Log log = LogFactory.getLog(getClass());
	/**
	 * @uml.property  name="eventPublisher"
	 * @uml.associationEnd  
	 */
	private EventPublisher eventPublisher;	
	private boolean caseInsensitive;
	/**
	 * @uml.property  name="userManager"
	 * @uml.associationEnd  
	 */
	private UserManager userManager;
	/**
	 * @uml.property  name="roleManager"
	 * @uml.associationEnd  
	 */
	private RoleManager roleManager;
	private String grantedAuthority;
	
    /**
	 * @param grantedAuthority
	 * @uml.property  name="grantedAuthority"
	 */
    public void setGrantedAuthority(String grantedAuthority) {
		this.grantedAuthority = grantedAuthority;
	}

	public ExtendedUserDetailsService(boolean caseInsensitive) {
		this.caseInsensitive = caseInsensitive;
		this.grantedAuthority = "";
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
		if(StringUtils.isNotEmpty(grantedAuthority)){			
			GrantedAuthorityImpl impl = new GrantedAuthorityImpl(grantedAuthority);
			if( ! authorities.contains(impl) )
				authorities.add(impl);
		}
		
		return new ExtendedUserDetails(user, authorities);
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

	/**
	 * @param caseInsensitive
	 * @uml.property  name="caseInsensitive"
	 */
	public void setCaseInsensitive(boolean caseInsensitive)
    {
        this.caseInsensitive = caseInsensitive;
    }

	/**
	 * @param eventPublisher
	 * @uml.property  name="eventPublisher"
	 */
	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	/**
	 * @param roleManager
	 * @uml.property  name="roleManager"
	 */
	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

	/**
	 * @param userManager
	 * @uml.property  name="userManager"
	 */
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

}