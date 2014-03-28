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
package architecture.ee.web.community.social;

import java.util.List;

import architecture.common.user.Company;
import architecture.common.user.User;
import architecture.ee.exception.NotFoundException;

public interface SocialNetworkManager {
	
	public abstract SocialNetwork createSocialNetwork(Company company, SocialNetwork.Media media);
	
	public abstract SocialNetwork createSocialNetwork(User user, SocialNetwork.Media media);
	
	public abstract SocialNetwork getSocialNetworkById(long socialAccountId) throws NotFoundException ;
	
	public abstract List<SocialNetwork> getSocialNetworks(Company company);
	
	public abstract List<SocialNetwork> getSocialNetworks(User user);
	
	public abstract List<SocialNetwork> getSocialNetworks(int objectType, long objectId);
	
	public abstract List<SocialNetwork> getSocialNetworks(int objectType, String username);
			
	public abstract void saveSocialNetwork( SocialNetwork socialNetwork );	

	public abstract void removeSocialNetwork( SocialNetwork socialNetwork );
	
	public abstract void updateSocialNetworkProperties( SocialNetwork socialNetwork );
}
