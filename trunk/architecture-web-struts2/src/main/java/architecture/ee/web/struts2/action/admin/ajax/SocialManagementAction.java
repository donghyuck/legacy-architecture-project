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
package architecture.ee.web.struts2.action.admin.ajax;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import architecture.ee.exception.NotFoundException;
import architecture.ee.web.social.SocialAccount;
import architecture.ee.web.social.SocialAccountManager;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;
import architecture.ee.web.util.ParamUtils;
import architecture.user.Group;

public class SocialManagementAction extends FrameworkActionSupport  {

    private int pageSize = 0 ;
    
    private int startIndex = 0 ;  
    
	private Long socialAccountId = -1L; 
	
	private Integer objectType = 0;
	
	private Long objectId = -1L;
	
	private SocialAccount targetSocialAccount;
	
	private SocialAccountManager socialAccountManager;
			
	public SocialManagementAction() {
	}

	/**
	 * @return pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize 설정할 pageSize
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return startIndex
	 */
	public int getStartIndex() {
		return startIndex;
	}

	/**
	 * @param startIndex 설정할 startIndex
	 */
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	/**
	 * @return socialAccountId
	 */
	public Long getSocialAccountId() {
		return socialAccountId;
	}

	/**
	 * @param socialAccountId 설정할 socialAccountId
	 */
	public void setSocialAccountId(Long socialAccountId) {
		this.socialAccountId = socialAccountId;
	}

	/**
	 * @return objectType
	 */
	public Integer getObjectType() {
		return objectType;
	}

	/**
	 * @param objectType 설정할 objectType
	 */
	public void setObjectType(Integer objectType) {
		this.objectType = objectType;
	}

	/**
	 * @return objectId
	 */
	public Long getObjectId() {
		return objectId;
	}

	/**
	 * @param objectId 설정할 objectId
	 */
	public void setObjectId(Long objectId) {
		this.objectId = objectId;
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
	
	public int getTotalTargetSocialAccountCount(){
		return 0; //getSocialAccountManager().		
	}
	
	public List<SocialAccount>getTargetSocialAccounts(){
		return getSocialAccountManager().getSocialAccounts(objectType, objectId);
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
		return success();
	}
	
	public String updateSocialAccount() throws Exception{
		try {
			Map map = ParamUtils.getJsonParameter(request, "item", Map.class);
			String accessSecret = (String)map.get("accessSecret");
			String accessToken = (String)map.get("accessToken");
			Boolean signedIn = (Boolean)map.get("signedIn");
			
			if( socialAccountId == null){
				Integer  selectedSocialAccountId = (Integer)map.get("socialAccountId");	
				socialAccountId = selectedSocialAccountId.longValue();
			}			
			
			SocialAccount account = getTargetSocialAccount();
			log.debug("=====================");
			
			if(!StringUtils.isEmpty(accessSecret))
				account.setAccessSecret(accessSecret);
			if(!StringUtils.isEmpty(accessToken))
				account.setAccessToken(accessToken);
			//if( signedIn!=null )
	
			log.debug(account);
			log.debug("=====================");
			socialAccountManager.saveSocialAccount(account);		
			this.targetSocialAccount = null;			
			return success();
		} catch (Throwable e) {
			e.printStackTrace();
			throw new Exception(e);
		}	
	}
}
