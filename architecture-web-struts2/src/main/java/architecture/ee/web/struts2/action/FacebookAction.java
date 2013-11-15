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

import java.util.List;

import architecture.ee.exception.NotFoundException;
import architecture.ee.web.social.SocialAccount;
import architecture.ee.web.social.SocialAccountManager;
import architecture.ee.web.social.facebook.FacebookProfile;
import architecture.ee.web.social.facebook.FacebookServiceProvider;
import architecture.ee.web.social.facebook.Post;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;

public class FacebookAction  extends FrameworkActionSupport {
	
	private int offset = 0, limit = 25 ;
	
	private Long socialAccountId = -1L; 
	
	private String userId = "me";
	
	private SocialAccount targetSocialAccount;
	
	private SocialAccountManager socialAccountManager;
	
	private String code;
	
	private String token;

	/**
	 * @return socialAccountId
	 */
	public Long getSocialAccountId() {
		return socialAccountId;
	}

	/**
	 * @return offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * @param offset 설정할 offset
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * @return limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @param limit 설정할 limit
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * @return userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId 설정할 userId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
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

	/**
	 * @return token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token 설정할 token
	 */
	public void setToken(String token) {
		this.token = token;
	}
	
	public FacebookProfile getFacebookProfile(){
		return getServiceProvider ().getUserProfile(userId);
	}
	
	public List<Post> getHomeFeed() {
		return getServiceProvider().getHomeFeed(offset, limit);		
	}
	
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
	
	public FacebookServiceProvider getServiceProvider () {
		return (FacebookServiceProvider)getTargetSocialAccount().getSocialServiceProvider();
	}
	
	public String execute() throws Exception {
		return success();
	}
	
}
