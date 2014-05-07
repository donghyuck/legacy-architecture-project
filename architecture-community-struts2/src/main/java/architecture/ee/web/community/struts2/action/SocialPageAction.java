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
import architecture.ee.web.community.social.SocialNetwork;
import architecture.ee.web.community.social.SocialNetworkManager;
import architecture.ee.web.community.struts2.action.support.SocialNetworkAware;

public class SocialPageAction extends PageAction implements SocialNetworkAware {

	private SocialNetworkManager socialNetworkManager;	
	
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
	
	public List<SocialNetwork> getConnectedCompanySocialNetworks(){
		 try {
			Company company = getCompany();
			 return socialNetworkManager.getSocialNetworks(company);
		} catch (Exception e) {
			return Collections.EMPTY_LIST;
		}
	}
}
