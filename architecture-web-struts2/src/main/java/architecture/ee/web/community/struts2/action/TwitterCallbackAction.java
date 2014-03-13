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
package architecture.ee.web.community.struts2.action;

import org.apache.commons.lang.StringUtils;
import org.scribe.model.Token;

import architecture.common.user.User;
import architecture.ee.web.community.social.SocialNetwork;
import architecture.ee.web.community.social.SocialNetwork.Media;
import architecture.ee.web.community.social.twitter.TwitterServiceProvider;
import architecture.ee.web.community.struts2.action.support.SocialCallbackSupport;

public class TwitterCallbackAction extends SocialCallbackSupport {
			
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
	
	public String execute() throws Exception {
		
		if( StringUtils.isNotEmpty(oauth_token) && StringUtils.isNotEmpty(oauth_verifier) ){
			SocialNetwork newSocialNetwork = newSocialNetwork(Media.TWITTER);			
			TwitterServiceProvider provider = (TwitterServiceProvider) newSocialNetwork.getSocialServiceProvider();			
			Token token =provider.getTokenWithCallbackReturns(oauth_token, oauth_verifier);			
			newSocialNetwork.setAccessSecret(token.getSecret());
			newSocialNetwork.setAccessToken(token.getToken());
			setSocialNetwork(newSocialNetwork);			
		}else if ( StringUtils.isNotEmpty( getOnetime())){
			log.debug("restore secure object");
			restoreOnetimeSecureObject();
		}		
		return success();
	}
	public User findUser() {				
		return this.findUserByMedia(Media.TWITTER);
	}	
}