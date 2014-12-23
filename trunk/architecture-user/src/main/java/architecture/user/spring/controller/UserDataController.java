/*
 * Copyright 2012, 2013 Donghyuck, Son
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
package architecture.user.spring.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;

import architecture.common.user.User;
import architecture.common.user.UserManager;
import architecture.common.user.UserTemplate;
import architecture.user.security.spring.userdetails.ExtendedUserDetails;
import architecture.user.spring.annotation.ActiveUser;


@Controller ("security-data-controller")
@RequestMapping("/data")
public class UserDataController {

	private static final Log log = LogFactory.getLog(UserDataController.class);
	
	//private static final String DEFAULT_CONTENT_TYPE = "text/html;charset=UTF-8";
	
//	private static final String DEFAULT_LOGIN_TEMPLATE = "/html/accounts/login.ftl";		
	
	
	@Inject
	@Qualifier("userManager")
	private UserManager userManager ;
	
	
	public UserDataController() {
	}
	
/*	
	*/
	
	@RequestMapping(value="/accounts/verify_credentials.json", method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public ExtendedUserDetails verifyCredentials(@ActiveUser ExtendedUserDetails activeUser){	
		return activeUser;
	}

	@RequestMapping(value="/accounts/get.json", method={RequestMethod.POST, RequestMethod.GET } )
	@ResponseBody
	public UserDetails getUserDetails(@ActiveUser ExtendedUserDetails activeUser, NativeWebRequest request ){				
		return new UserDetails(activeUser.getUser(), getRoles(activeUser.getAuthorities()));		
	}
	
	protected List<String> getRoles (Collection<GrantedAuthority> authorities) {		
		List<String> list = new ArrayList<String>();
		for(GrantedAuthority auth : authorities ) {
			list.add( auth.getAuthority() );
		}
		return list;
	}
	
	public static class UserDetails {
		
		private User user;
		private List<String> roles;
		
		public UserDetails() {
		}		
		
		public UserDetails(User user, List<String> roles) {
			this.user = user;
			this.roles = roles;
		}
		/**
		 * @return user
		 */
		public User getUser() {
			return user;
		}
		/**
		 * @param user 설정할 user
		 */
		public void setUser(User user) {
			this.user = user;
		}
		/**
		 * @return roles
		 */
		public List<String> getRoles() {
			return roles;
		}
		/**
		 * @param roles 설정할 roles
		 */
		public void setRoles(List<String> roles) {
			this.roles = roles;
		}
	}
	

	@RequestMapping(value="/users/lookup.json", method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public User getUserProfile(@RequestParam(value="id", defaultValue="0", required=false ) Long userId, @RequestParam(value="username", required=false ) String username, 	@RequestParam(value="email", required=false ) String email, 	NativeWebRequest request ){		
		UserTemplate template = new UserTemplate(userId);		
		if(StringUtils.isNotEmpty(username))
			template.setUsername(username);
		if(StringUtils.isNotEmpty(email))
			template.setEmail(email);		
		return userManager.getUser(template);
	}
}
