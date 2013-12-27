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

import java.util.Collections;
import java.util.List;

import architecture.common.user.Company;
import architecture.ee.web.social.SocialAccount;
import architecture.ee.web.social.SocialAccountManager;

public class SocialPageAction extends PageAction {

	private SocialAccountManager socialAccountManager;	

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

	public boolean hasCompanySocial () {
		 Company company = getCompany();
		 List<SocialAccount> asl = socialAccountManager.getSocialAccounts(company);
		 if( asl.size() > 0 ){
			 return true;
		 }else {
			 return false;
		 }
	}
	
	public List<SocialAccount> getCompanySocials(){
		 try {
			Company company = getCompany();
			 return socialAccountManager.getSocialAccounts(company);
		} catch (Exception e) {
			return Collections.EMPTY_LIST;
		}
	}
}
