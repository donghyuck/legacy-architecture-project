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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import architecture.ee.web.community.social.SocialNetwork;
import architecture.ee.web.community.social.SocialNetwork.Media;
import architecture.ee.web.community.social.SocialNetworkManager;
import architecture.ee.web.community.struts2.action.SocialNetworkAware;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;

public class MySocialNetworkAction extends FrameworkActionSupport implements SocialNetworkAware  {

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
	
	public List<SocialNetwork> getConnectedSocialNetworks () {		
		if(getUser().isAnonymous())
			return Collections.EMPTY_LIST;
		return socialNetworkManager.getSocialNetworks(getUser());		
	}
	
	protected Map<Media, SocialNetwork> getConnectedSocialNetworkMap () {		
		if(getUser().isAnonymous())
			return Collections.EMPTY_MAP;
		
		Map<Media, SocialNetwork> map = new HashMap<Media, SocialNetwork>();
		for( SocialNetwork network : getConnectedSocialNetworks() ){
			map.put(network.getSocialServiceProvider().getMedia(), network);
		}
		return map;
	}
	
	public List<SocialNetwork> getSocialNetworks () {				
		Map<Media, SocialNetwork> map = getConnectedSocialNetworkMap();
		List<SocialNetwork> networkList = new ArrayList<SocialNetwork>(SocialNetwork.Media.values().length);
		for( SocialNetwork.Media media : SocialNetwork.Media.values() )
		{
			if( map.containsKey(media)){
				networkList.add(map.get(media));
			}else{
				networkList.add(newSocialNetwork(media));
			}
		}
		return networkList;
	}
	
	public String execute() throws Exception {
		return success();
	}
	
	protected SocialNetwork newSocialNetwork( SocialNetwork.Media media ){	
		return socialNetworkManager.createSocialNetwork(getUser(), media);
	}
	
}
