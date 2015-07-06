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

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.NativeWebRequest;

import architecture.common.user.SecurityHelper;
import architecture.common.user.User;
import architecture.ee.exception.NotFoundException;
import architecture.ee.util.ApplicationHelper;
import architecture.ee.web.navigator.MenuComponent;
import architecture.ee.web.navigator.MenuNotFoundException;
import architecture.ee.web.site.WebSite;
import architecture.ee.web.site.WebSiteNotFoundException;
import architecture.ee.web.util.WebSiteUtils;

@Controller ("security-accounts-controller")
@RequestMapping("/accounts")
public class SecurityController {

	private static final Log log = LogFactory.getLog(UserDataController.class);
	
	private static final String DEFAULT_CONTENT_TYPE = "text/html;charset=UTF-8";
	
	private static final String DEFAULT_LOGIN_TEMPLATE_KEY = "security.authentication.template";		
	
	private static final String DEFAULT_LOGIN_TEMPLATE = "/html/accounts/login";		
	
	public SecurityController() {
	}

	@RequestMapping(value="/login", method={RequestMethod.POST, RequestMethod.GET } )
	public String login(NativeWebRequest request, Model model ) throws NotFoundException, IOException {	
		User user = SecurityHelper.getUser();		
		WebSite website = getCurrentWebSite(request.getNativeRequest(HttpServletRequest.class));
		model.addAttribute("action", ActionAdaptor.newBuilder().webSite(website).user(user).remoteAddr(request.getNativeRequest(HttpServletRequest.class).getRemoteAddr()).build());	
		setContentType(request.getNativeResponse(HttpServletResponse.class));		
		return ApplicationHelper.getApplicationProperty(DEFAULT_LOGIN_TEMPLATE_KEY, DEFAULT_LOGIN_TEMPLATE);
	}

	private void setContentType(HttpServletResponse response){
		response.setContentType(DEFAULT_CONTENT_TYPE);
	}
	
	private WebSite getCurrentWebSite(HttpServletRequest request) throws WebSiteNotFoundException{
		return WebSiteUtils.getWebSite(request);
	}
	
public static class ActionAdaptor {
		
		private String remoteAddr;		
		private User user;
		
		private WebSite webSite;				

		public MenuComponent getWebSiteMenu(String name) throws MenuNotFoundException { 
			if( webSite != null ){
				return WebSiteUtils.getMenuComponent( webSite.getMenu(), name);
			}else{
				throw new MenuNotFoundException();
			}
		}
		
		public boolean hasWebSiteMenu(String name ){
			if( webSite != null ){
				try {
					WebSiteUtils.getMenuComponent( webSite.getMenu(), name);
					return true;
				} catch (MenuNotFoundException e) {
					return false;
				}				
			}
			return false;
		}		
		
		public boolean isSetNavigator (){
			return false;
		}
				
		public MenuComponent getNavigator() throws MenuNotFoundException{
			throw new MenuNotFoundException();
		}
		
		
		/**
		 * @return remoteAddr
		 */
		public String getRemoteAddr() {
			return remoteAddr;
		}

		/**
		 * @param remoteAddr 설정할 remoteAddr
		 */
		public void setRemoteAddr(String remoteAddr) {
			this.remoteAddr = remoteAddr;
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
		 * @return webSite
		 */
		public WebSite getWebSite() {
			return webSite;
		}

		/**
		 * @param webSite 설정할 webSite
		 */
		public void setWebSite(WebSite webSite) {
			this.webSite = webSite;
		}

		public static class Builder{
			ActionAdaptor actionAdaptor = new ActionAdaptor();			
			public Builder webSite(WebSite webSite){
				this.actionAdaptor.webSite = webSite;
				return this;
			} 
			
			public Builder user(User user){
				this.actionAdaptor.user = user;
				return this;
			}
			
			public Builder remoteAddr(String remoteAddr){
				this.actionAdaptor.remoteAddr = remoteAddr;
				return this;
			}
			public ActionAdaptor build(){
				return actionAdaptor;
			}
			
		}
		
		public static Builder newBuilder(){
			return new Builder();
		}
	}
	
}
