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

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.scribe.model.Token;

import architecture.common.user.User;
import architecture.common.user.UserNotFoundException;
import architecture.ee.web.community.social.SocialNetwork;
import architecture.ee.web.community.social.SocialNetwork.Media;
import architecture.ee.web.community.social.facebook.FacebookProfile;
import architecture.ee.web.community.social.facebook.FacebookServiceProvider;
import architecture.ee.web.community.struts2.action.support.SocialCallbackSupport;

public class FacebookCallbackAction  extends SocialCallbackSupport {
	
	private String code;
	private FacebookProfile userProfile = null;
	private User foundUser = null;
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
	
	public Object getUserProfile(){
		if( this.userProfile == null ){
			FacebookServiceProvider provider = (FacebookServiceProvider) getSocialNetwork().getSocialServiceProvider();			
			this.userProfile = provider.getUserProfile();
		}
		return this.userProfile;
	}

	public String execute() throws Exception {
		
		if( StringUtils.isNotEmpty(code) ){
			SocialNetwork newSocialNetwork = newSocialNetwork(Media.FACEBOOK);			
			FacebookServiceProvider provider = (FacebookServiceProvider) newSocialNetwork.getSocialServiceProvider();			
			Token token = provider.getTokenWithCallbackReturns(null, code);
			newSocialNetwork.setAccessSecret(token.getSecret());
			newSocialNetwork.setAccessToken(token.getToken());
			setSocialNetwork(newSocialNetwork);
		}		
		return success();
	}

	@Override
	public User findUser() {
		
		if( this.foundUser == null){
			FacebookProfile profileToUse = (FacebookProfile)getUserProfile();
			if( profileToUse != null ){
				SocialNetwork found = findSocialNetworkByUsername( Media.FACEBOOK, profileToUse.getId());
				if( found != null )
					try {
						this.foundUser = getUserManager().getUser(found.getObjectId());
					} catch (UserNotFoundException e) {
						log.error(e);
					}
			}
		}
		return this.foundUser;
	}
		
}
