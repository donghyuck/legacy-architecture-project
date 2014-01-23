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
package architecture.ee.web.community.struts2.action.support;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;

import architecture.common.model.factory.ModelTypeFactory;
import architecture.common.user.User;
import architecture.common.user.UserManager;
import architecture.ee.web.community.social.SocialNetwork;
import architecture.ee.web.community.social.SocialNetwork.Media;
import architecture.ee.web.community.social.SocialNetworkManager;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;
import architecture.user.security.spring.userdetails.ExtendedUserDetailsService;

public abstract class SocialCallbackSupport extends FrameworkActionSupport implements SocialNetworkAware  {

	private SocialNetworkManager socialNetworkManager;
	private SocialNetwork socialNetwork;
	private String accessSecret;	
	private String accessToken;
	private UserManager userManager;
	
	
	/**
	 * @return userManager
	 */
	public UserManager getUserManager() {
		return userManager;
	}

	/**
	 * @param userManager 설정할 userManager
	 */
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	/**
	 * @return socialNetworkManager
	 */
	public SocialNetworkManager getSocialNetworkManager() {
		return socialNetworkManager;
	}

	/**
	 * @param socialNetworkManager 설정할 socialNetworkManager
	 */
	public void setSocialNetworkManager(SocialNetworkManager socialNetworkManager) {
		this.socialNetworkManager = socialNetworkManager;
	}

	/**
	 * @return socialNetwork
	 */
	public SocialNetwork getSocialNetwork() {
		return socialNetwork;
	}

	/**
	 * @param socialNetwork 설정할 socialNetwork
	 */
	public void setSocialNetwork(SocialNetwork socialNetwork) {
		
		this.socialNetwork = socialNetwork;
		this.accessSecret = socialNetwork.getAccessSecret();
		this.accessToken = socialNetwork.getAccessToken();
		
		socialNetwork.getSocialServiceProvider().setAccessToken(socialNetwork.getAccessToken());
		socialNetwork.getSocialServiceProvider().setAccessSecret(socialNetwork.getAccessSecret());

	}

	/**
	 * @return accessSecret
	 */
	public String getAccessSecret() {
		return accessSecret;
	}

	/**
	 * @param accessSecret 설정할 accessSecret
	 */
	public void setAccessSecret(String accessSecret) {
		this.accessSecret = accessSecret;
	}

	/**
	 * @return accessToken
	 */
	public String getAccessToken() {
		return accessToken;
	}

	/**
	 * @param accessToken 설정할 accessToken
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}	

	
	protected SocialNetwork newSocialNetwork(Media media){	
		SocialNetwork network =  socialNetworkManager.createSocialNetwork(getUser(), media );		
		return network;
	}
	
	abstract public Object getUserProfile();
	
	abstract public User findUser();
			
	protected  List<SocialNetwork> findSocialNetworksByUsername(String username){				
		int objectType = ModelTypeFactory.getTypeIdFromCode("USER") ;
		return socialNetworkManager.getSocialNetworks(objectType, username);		
	}
	
	protected  SocialNetwork findSocialNetworkByUsername(Media media, String username ){		
		List<SocialNetwork> list = findSocialNetworksByUsername(username);
		SocialNetwork found = null;
		for( SocialNetwork network : list ){
			if(	network.getSocialServiceProvider().getMedia() == media )
			{
				found = network;
				break;
			}
		}
		return found;
	}
	
	protected void createSecurityContext(User userToUse) {				
		if( userToUse.getUserId() > 0 ){
			ExtendedUserDetailsService detailsService = getComponent(ExtendedUserDetailsService.class);
			UserDetails details = detailsService.loadUserByUsername(userToUse.getUsername());
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
			SecurityContextImpl context = new SecurityContextImpl ();
			context.setAuthentication(authentication);				
			SecurityContextHolder.setContext( context );		
			
			HttpSession httpsession = request.getSession(true);
			httpsession.setAttribute("SPRING_SECURITY_CONTEXT", context);		
		}
	} 
	
	public boolean signIn() {
		
		User userToUse = findUser();
		log.debug("try ... single sign on ............" + userToUse.getUserId());
		if( userToUse != null ){			
			createSecurityContext(userToUse);
			return true;
		}
		return false;
		
	}
}
