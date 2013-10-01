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
package architecture.user.security.authentication.impl;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import architecture.common.exception.ComponentNotFoundException;
import architecture.common.user.Company;
import architecture.common.user.User;
import architecture.common.user.authentication.AnonymousUser;
import architecture.common.user.authentication.AuthToken;
import architecture.ee.util.ApplicationHelper;
import architecture.user.CompanyManager;
import architecture.user.CompanyNotFoundException;
import architecture.user.security.authentication.AuthenticationProvider;
import architecture.user.security.authentication.AuthenticationProviderFactory;
import architecture.user.security.spring.userdetails.ExtendedUserDetails;

public class AuthenticationProviderFactoryImpl implements AuthenticationProviderFactory.Implementation {

	private static final Log log = LogFactory.getLog(AuthenticationProviderFactoryImpl.class);
	
	private SecurityContextAuthenticationProvider instance = new SecurityContextAuthenticationProvider();
	
	
	
	public AuthenticationProvider getSecurityContextAuthenticationProvider() {
		return instance;
	}

	static class SecurityContextAuthenticationProvider implements AuthenticationProvider {

		private Company defaultCompany = null;
		
		public Authentication getAuthentication(){
			SecurityContext context = SecurityContextHolder.getContext();
			Authentication authen = context.getAuthentication();		
			return authen;
		}

		public User getUser() {
			try {
				Authentication authen = getAuthentication();	
				Object obj = authen.getPrincipal();
				if ( obj instanceof ExtendedUserDetails )
					return ((ExtendedUserDetails)obj).getUser();
			} catch (Exception ignore) {
			}			
			return  createAnonymousUser();
		}

		public AuthToken getAuthToken() {		
			try {
				Authentication authen = getAuthentication();	
				Object obj = authen.getPrincipal();
				if ( obj instanceof AuthToken )
					return (AuthToken)obj;
			} catch (Exception ignore) {
			}		
			return createAnonymousUser();
		}
		

		public boolean isSystemAdmin() {
				return false;
		}
		
		protected AnonymousUser createAnonymousUser(){
			try {
				HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
				String localAddr = request.getLocalAddr();
				String localName = request.getLocalName();
				
				log.debug("DOMAIN:" + localName);
			} catch (Exception ignore) {
				log.warn(ignore);
			}			
			try {
				return new AnonymousUser( getDefaultCompany() );
			} catch (Exception e) {
				log.warn(e);
				return new AnonymousUser();
			}			
		}

		protected Company getDefaultCompany() throws Exception {
			if( defaultCompany == null ){
				String companyIdStr = ApplicationHelper.getApplicationProperty("components.user.anonymous.company.id", "1");
				Company company = ApplicationHelper.getComponent(CompanyManager.class).getCompany(Long.parseLong(companyIdStr));
				defaultCompany = company;
			}
			return defaultCompany;
		}
		
	}
}