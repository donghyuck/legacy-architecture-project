/*
 * Copyright 2012 Donghyuck, Son
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package architecture.user.security.spring.userdetails;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;

import architecture.common.event.api.EventPublisher;
import architecture.common.event.api.EventSource;
import architecture.common.lifecycle.ApplicationProperties;
import architecture.common.user.User;
import architecture.common.user.UserManager;
import architecture.common.user.UserTemplate;
import architecture.common.util.L10NUtils;
import architecture.ee.component.admin.AdminHelper;
import architecture.user.Role;
import architecture.user.RoleManager;

public class ExtendedUserDetailsService implements UserDetailsService, EventSource {

	private Log log = LogFactory.getLog( getClass() );
	
	private UserManager userManager;
	private RoleManager roleManager;
	
	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

	private EventPublisher eventPublisher;
	
	private boolean caseInsensitive;
	
	public ExtendedUserDetailsService() {
		caseInsensitive = true;
	}
	
	public ExtendedUserDetailsService(boolean caseInsensitive) {
		this.caseInsensitive = caseInsensitive;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

    public void setEventPublisher(EventPublisher eventPublisher)
    {
        this.eventPublisher = eventPublisher;
    }
    
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userManager.getUser(new UserTemplate(username), caseInsensitive);
		if( user == null){
			String msg = L10NUtils.format( "005011", username );			
			if( log.isInfoEnabled())
				log.info(msg);			
			throw new UsernameNotFoundException(msg);
		}else{
			return toUserDetails(user);
		}
	}

	protected UserDetails toUserDetails(User user){
		if(eventPublisher != null){
			
		}		
		
		ExtendedUserDetails userDetails = new ExtendedUserDetails(user, getFinalUserAuthority(user) );
		return userDetails;
	}
	
	protected List<GrantedAuthority> getFinalUserAuthority(User user){		
		
		ApplicationProperties setupProperties = AdminHelper.getRepository().getSetupApplicationProperties();		
		
		String authority = setupProperties.get(architecture.ee.util.ApplicationConstants.SECURITY_AUTHENTICATION_AUTHORITY_PROP_NAME);
		
		long userId = user.getUserId();		
		List<Role> userRoles = roleManager.getFinalUserRoles(userId) ;
		List<String> roles = new ArrayList<String>();
		
		for( Role role : userRoles ){
			roles.add( role.getName() );
		}		
		log.debug( "roles:" + roles  + ", authority:" + authority );
		
		if( !architecture.common.util.StringUtils.isEmpty(authority) ){
			authority = authority.trim();			
			if (!roles.contains(authority)){
				roles.add(authority);
			}
		}				
		return AuthorityUtils.createAuthorityList(StringUtils.toStringArray(roles));
	}
	
}