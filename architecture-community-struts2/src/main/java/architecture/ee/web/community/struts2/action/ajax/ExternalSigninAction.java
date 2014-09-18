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
package architecture.ee.web.community.struts2.action.ajax;

import org.apache.commons.lang.StringUtils;

import architecture.common.user.User;
import architecture.common.user.authentication.UnAuthorizedException;
import architecture.ee.web.community.social.SocialNetwork.Media;
import architecture.ee.web.community.struts2.action.support.SocialCallbackSupport;

public class ExternalSigninAction extends SocialCallbackSupport {

	private User signinUser ;
	
	
	public String execute() throws Exception {		

		if ( StringUtils.isNotEmpty( getOnetime())){
			log.debug("restore secure object");
			restoreOnetimeSecureObject();			
			signIn();
			return success();
		}		
		throw new UnAuthorizedException("not valid access.");
	}
	
	/**
	 * @return signinUser
	 */
	public User getSigninUser() {
		return signinUser;
	}


	/**
	 * @param signinUser 설정할 signinUser
	 */
	public void setSigninUser(User signinUser) {
		this.signinUser = signinUser;
	}

	@Override
	public User findUser() {
		Media media = Media.valueOf(getSocialNetwork().getServiceProviderName().toUpperCase());
		if( signinUser == null)
			signinUser = findUserByMedia(media);
		
		return signinUser;
	}

}
