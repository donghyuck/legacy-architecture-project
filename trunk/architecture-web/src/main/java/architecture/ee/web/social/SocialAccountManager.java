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
package architecture.ee.web.social;

import java.util.List;

import architecture.common.user.Company;
import architecture.common.user.User;
import architecture.ee.exception.NotFoundException;

public interface SocialAccountManager {

	public abstract SocialAccount createSocialAccount(Company company, SocialAccount.SocialMedia media);
	
	public abstract SocialAccount createSocialAccount(User user, SocialAccount.SocialMedia media);
	
	public abstract SocialAccount getSocialAccountById(long socialAccountId) throws NotFoundException ;
	
	public abstract List<SocialAccount> getSocialAccounts(Company company);
	
	public abstract List<SocialAccount> getSocialAccounts(User user);
	
	public abstract List<SocialAccount> getSocialAccounts(int objectType, long objectId);
			
	public abstract void saveSocialAccount( SocialAccount socialAccount );	

	public abstract void removeSocialAccount( SocialAccount socialAccount );
	
}
