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
package architecture.ee.web.struts2.action;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.Element;

import org.scribe.model.Token;

import architecture.common.user.Company;
import architecture.common.user.authentication.UnAuthorizedException;
import architecture.ee.exception.NotFoundException;
import architecture.ee.web.social.SocialAccount;
import architecture.ee.web.social.SocialAccountManager;
import architecture.ee.web.social.facebook.FacebookProfile;
import architecture.ee.web.social.facebook.Post;
import architecture.ee.web.social.twitter.Tweet;
import architecture.ee.web.social.twitter.TwitterProfile;
import architecture.ee.web.social.twitter.TwitterServiceProvider;
import architecture.ee.web.struts2.action.support.SocialActionSupport;
import architecture.ee.web.util.ParamUtils;

public class TwitterAction extends SocialActionSupport  {

	private Long socialAccountId = -1L; 
	
	private SocialAccount targetSocialAccount;
	
	private SocialAccountManager socialAccountManager;
	
	private String oauth_token;
	
	private String oauth_verifier;
	
		
	
	/**
	 * @return oauth_token
	 */
	public String getOauth_token() {
		return oauth_token;
	}

	/**
	 * @param oauth_token 설정할 oauth_token
	 */
	public void setOauth_token(String oauth_token) {
		this.oauth_token = oauth_token;
	}

	/**
	 * @return oauth_verifier
	 */
	public String getOauth_verifier() {
		return oauth_verifier;
	}

	/**
	 * @param oauth_verifier 설정할 oauth_verifier
	 */
	public void setOauth_verifier(String oauth_verifier) {
		this.oauth_verifier = oauth_verifier;
	}

	/**
	 * @return targetSocialAccount
	 */
	public SocialAccount getTargetSocialAccount() {
		try {	
			if( targetSocialAccount == null){
				targetSocialAccount = getSocialAccountManager().getSocialAccountById(socialAccountId);
			}
			return targetSocialAccount;
		} catch (NotFoundException e) {
			log.error(e);
			return null;
		}
	}
	
	/**
	 * @return socialAccountId
	 */
	public Long getSocialAccountId() {
		return socialAccountId;
	}

	
	public TwitterServiceProvider getServiceProvider () {
		return (TwitterServiceProvider)getTargetSocialAccount().getSocialServiceProvider();
	}
	
	
	public TwitterProfile getTwitterProfile () throws UnAuthorizedException {
		return getServiceProvider().authenticate();
	}
	
	private String getTimelineCacheKey(String type){		
		StringBuilder sb = new StringBuilder();
		sb.append( getTargetSocialAccount().getSocialAccountId() );
		sb.append( "-");
		sb.append( type );
		return sb.toString();
	}

	public List<Tweet> getUserTimeline () {		
		List<Tweet> list = Collections.EMPTY_LIST ;	
		if( isCacheEnabled()){
			String key = getTimelineCacheKey("usertimeline");			
			if( getSocialStreamsCache().get(key) == null){
				list = getServiceProvider().getUserTimeline();
				getSocialStreamsCache().put(new Element(key, list));
			}
			list = (List<Tweet>)getSocialStreamsCache().get( key ).getValue();
		}else{
			list = getServiceProvider().getUserTimeline();
		}
		return list ;
	}

	public List<Tweet> getHomeTimeline () {
		List<Tweet> list = Collections.EMPTY_LIST ;	
		if( isCacheEnabled()){
			String key = getTimelineCacheKey("hometimeline");			
			if( getSocialStreamsCache().get(key) == null){
				list = getServiceProvider().getHomeTimeline();
				getSocialStreamsCache().put(new Element(key, list));
			}
			list = (List<Tweet>)getSocialStreamsCache().get( key ).getValue();
		}else{
			list = getServiceProvider().getHomeTimeline();
		}
		return list ;
	}
	
	public String getAuthorizationUrl () {
		return getServiceProvider().getAuthorizationUrl();
	}
	
	
	/**
	 * @param socialAccountId 설정할 socialAccountId
	 */
	public void setSocialAccountId(Long socialAccountId) {
		this.socialAccountId = socialAccountId;
	}

	/**
	 * @return socialAccountManager
	 */
	public SocialAccountManager getSocialAccountManager() {
		return socialAccountManager;
	}

	/**
	 * @param socialAccountManager 설정할 socialAccountManager
	 */
	public void setSocialAccountManager(SocialAccountManager socialAccountManager) {
		
		this.socialAccountManager = socialAccountManager;
	}

	public String execute() throws Exception {		
		if( socialAccountId < 0 ){
			Company company = getCompany();
			List <SocialAccount> list = socialAccountManager.getSocialAccounts(company);
			for( SocialAccount account : list ){
				if( "twitter".toLowerCase().equals(account.getServiceProviderName()) ){				
					socialAccountId = account.getSocialAccountId();
					targetSocialAccount = account;
					break;
				}
			}		
		}
		return success();
	}
	
	public String update() throws Exception{
		try {
			Map map = ParamUtils.getJsonParameter(request, "item", Map.class);			
			oauth_token = (String)map.get("oauth_token");
			oauth_verifier = (String)map.get("oauth_verifier");			
			Boolean signedIn = (Boolean)map.get("signedIn");			
			if( socialAccountId == null){
				Integer  selectedSocialAccountId = (Integer)map.get("socialAccountId");	
				socialAccountId = selectedSocialAccountId.longValue();
			}
			
			TwitterServiceProvider provider = getServiceProvider();
			Token token = provider.getTokenWithCallbackReturns(oauth_token, oauth_verifier);			
			SocialAccount account = getTargetSocialAccount();
			account.setAccessSecret(token.getSecret());			
			account.setAccessToken(token.getToken());
			socialAccountManager.saveSocialAccount(account);			
			this.targetSocialAccount = null;			
			return success();
		} catch (Throwable e) {
			e.printStackTrace();
			throw new Exception(e);
		}	
	}	
}