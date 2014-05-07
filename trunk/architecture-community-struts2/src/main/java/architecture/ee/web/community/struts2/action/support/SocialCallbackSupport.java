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

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;

import architecture.common.model.factory.ModelTypeFactory;
import architecture.common.user.User;
import architecture.common.user.UserManager;
import architecture.common.user.UserNotFoundException;
import architecture.common.user.UserTemplate;
import architecture.ee.web.community.social.SocialNetwork;
import architecture.ee.web.community.social.SocialNetwork.Media;
import architecture.ee.web.community.social.SocialNetworkManager;
import architecture.ee.web.community.social.SocialServiceProvider;
import architecture.ee.web.community.social.UserProfile;

import architecture.ee.web.struts2.action.support.FrameworkActionSupport;
import architecture.user.security.spring.userdetails.ExtendedUserDetailsService;

public abstract class SocialCallbackSupport extends FrameworkActionSupport implements SocialNetworkAware  {

	private SocialNetworkManager socialNetworkManager;
	private SocialNetwork socialNetwork;
	private String accessSecret;	
	private String accessToken;
	private UserManager userManager;
	private Cache socialOnetimeCache;
	private String onetime = null ;
	private Object userProfile = null;
	private User foundUser = null;
	private Boolean signin = false;
	private Boolean signup = false;
	
	public String getDomainName () {
		String domainToUse = null;
		if( getSession().containsKey("domainName")){
			domainToUse = (String) getSession().get("domainName");
		}else{
			domainToUse = request.getLocalName() ;
		}
		return domainToUse;
	}
	
		/**
	 * @return signin
	 */
	public Boolean getSignin() {
		return signin;
	}

	/**
	 * @param signin 설정할 signin
	 */
	public void setSignin(Boolean signin) {
		this.signin = signin;
	}

	/**
	 * @return signup
	 */
	public Boolean getSignup() {
		return signup;
	}

	/**
	 * @param signup 설정할 signup
	 */
	public void setSignup(Boolean signup) {
		this.signup = signup;
	}

		/**
	 * @return onetime
	 */
	public String getOnetime() {
		return onetime;
	}
	
	/**
	 * @param onetime 설정할 onetime
	 */
	public void setOnetime(String onetime) {
		this.onetime = onetime;
	}

	

	/**
	 * @return socialOnetimeCache
	 */
	public Cache getSocialOnetimeCache() {
		return socialOnetimeCache;
	}

	/**
	 * @param socialOnetimeCache 설정할 socialOnetimeCache
	 */
	public void setSocialOnetimeCache(Cache socialOnetimeCache) {
		this.socialOnetimeCache = socialOnetimeCache;
	}

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
	
	public Object getUserProfile(){
		if( userProfile == null ){
			SocialServiceProvider provider = getSocialNetwork().getSocialServiceProvider();
			this.userProfile = provider.getUserProfile();
		}
		return this.userProfile;
	}
	
	public abstract User findUser();
	
	public boolean signIn() {		
		User userToUse = findUser();
		if( userToUse != null ){			
			createSecurityContext(userToUse);			
			UserTemplate template = new UserTemplate(userToUse);
			template.setLastLoggedIn(new Date());				
			try {
				userManager.updateUser(template);
			} catch (Exception e) {} 			
			return true;
		}else{
			return false;		
		}
	}
	
	private void createSecurityContext(User userToUse) {				
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
	
	public Object getOnetimeSecureObject(){
		Object obj = socialOnetimeCache.get(onetime);
		if( obj != null ){
			return ((Element)obj).getValue();
		}
		return null;
	}

	protected void restoreOnetimeSecureObject(){		
		if( StringUtils.isNotEmpty(this.onetime) ){
			this.socialNetwork = (SocialNetwork)getOnetimeSecureObject();				
		
			log.debug("network:" + socialNetwork.getServiceProviderName());
		}
	}
	
	protected void setOnetimeSecureObject() {
		if(this.socialNetwork  != null ){
			if( StringUtils.isEmpty( this.onetime )){
				this.onetime = createOnetimeCode();
			}
			log.debug(this.onetime + "," + this.socialNetwork );
			socialOnetimeCache.put(new Element(this.onetime, this.socialNetwork ));	
		}
	}
	
	/**
	 * 랜덤 키를 생성하여 린턴한다.
	 * @return
	 */
	protected String createOnetimeCode(){
		return RandomStringUtils.random(64, true, true);		
	}
	
	/**
	 * 소셜 미디어 인자에 따라 로컬 사용자를 검색한다.
	 * 
	 * @param media
	 * @return
	 */
	protected User findUserByMedia(Media media) {		
		if( this.foundUser == null){
			UserProfile profileToUse = (UserProfile)getUserProfile();
			if( profileToUse != null ){
				SocialNetwork socialNetworkToUse = findSocialNetworkByUsername( media, profileToUse.getPrimaryKeyString());
				if( socialNetworkToUse != null )
					try {
						this.foundUser = getUserManager().getUser(socialNetworkToUse.getObjectId());
					} catch (UserNotFoundException e) {
						log.error(e);
					}
			}
		}
		return this.foundUser;
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
	
	/**
	 * 인자로 전달된 ID 값에 해당하는 쇼셜네트워크 연결정보가 존재하는 가를 검색한다.
	 * @param username
	 * @return
	 */
	protected  List<SocialNetwork> findSocialNetworksByUsername(String username){				
		int objectType = ModelTypeFactory.getTypeIdFromCode("USER") ;
		return socialNetworkManager.getSocialNetworks(objectType, username);
	}
	
	
}
