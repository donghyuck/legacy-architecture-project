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

import architecture.ee.web.community.social.SocialNetwork;
import architecture.ee.web.community.social.SocialNetworkManager;
import architecture.ee.web.community.social.facebook.FacebookServiceProvider;
import architecture.ee.web.community.struts2.action.support.SocialNetworkAware;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;

public class FacebookCallbackAction  extends FrameworkActionSupport implements SocialNetworkAware {
	
	private static final Token EMPTY_TOKEN = null;
	
	private String code;
	
	private String accessSecret;
	
	private String accessToken;
		
	private SocialNetworkManager socialNetworkManager;

	private SocialNetwork socialNetwork;
	
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
	 * @return code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code 설정할 code
	 */
	public void setCode(String code) {
		this.code = code;
	}
		
	
	public String execute() throws Exception {
		
		if( StringUtils.isNotEmpty(code) ){
			this.socialNetwork = newSocialNetwork();			
			FacebookServiceProvider provider = (FacebookServiceProvider) socialNetwork.getSocialServiceProvider();			
			Token token = provider.getTokenWithCallbackReturns(null, code);
			socialNetwork.setAccessSecret(token.getSecret());
			socialNetwork.setAccessToken(token.getToken());
			provider.setAccessToken(token.getToken());
			provider.setAccessSecret(token.getSecret());
			this.accessSecret = token.getSecret();
			this.accessToken = token.getToken();
		}		
		return success();
	}
	
	protected SocialNetwork newSocialNetwork(){	
		SocialNetwork network =  socialNetworkManager.createSocialNetwork(getUser(), SocialNetwork.Media.FACEBOOK );		
		return network;
	}
}
