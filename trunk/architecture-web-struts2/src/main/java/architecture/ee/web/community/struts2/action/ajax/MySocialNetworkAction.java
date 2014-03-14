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

import org.apache.commons.lang3.StringUtils;
import org.scribe.model.Token;

import architecture.ee.exception.NotFoundException;
import architecture.ee.web.community.social.SocialNetwork;
import architecture.ee.web.community.social.SocialNetwork.Media;
import architecture.ee.web.community.social.SocialNetworkManager;
import architecture.ee.web.community.struts2.action.support.SocialNetworkAware;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;
import architecture.ee.web.util.ParamUtils;

public class MySocialNetworkAction extends FrameworkActionSupport implements SocialNetworkAware   {
    
	private Long socialNetworkId = -1L; 
	
	private SocialNetwork mySocialNetwork = null ;	
	
	private static final Token EMPTY_TOKEN = null;
	
	private SocialNetworkManager socialNetworkManager;
	
	
	private String media;
	
	
	
	/**
	 * @return media
	 */
	public String getMedia() {
		return media;
	}

	/**
	 * @param media 설정할 media
	 */
	public void setMedia(String media) {
		this.media = media;
	}

	/**
	 * @return socialNetworkId
	 */
	public Long getSocialNetworkId() {
		return socialNetworkId;
	}

	/**
	 * @param socialNetworkId 설정할 socialNetworkId
	 */
	public void setSocialNetworkId(Long socialNetworkId) {
		this.socialNetworkId = socialNetworkId;
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
	
	public SocialNetwork getSocialNetwork() throws NotFoundException{
		if( mySocialNetwork == null && socialNetworkId > 0 ){			
			this.mySocialNetwork = socialNetworkManager.getSocialNetworkById(this.socialNetworkId);
		}else{		
			if( StringUtils.isNotEmpty(media)){
				Media selectedMedia = SocialNetwork.Media.valueOf(media.toUpperCase());
				mySocialNetwork = newSocialNetwork(selectedMedia);				
			}
		}
		return mySocialNetwork;
	}
	
	public String execute() throws Exception {
		return success();
	}
	
	public String deleteSocialNetwork() throws Exception {				
		socialNetworkManager.removeSocialNetwork(getSocialNetwork());
		return success();
	}
	
	public String updateSocialNetwork() throws Exception{		
		
		try {
			Map map = ParamUtils.getJsonParameter(request, "item", Map.class);
			String accessSecret = (String)map.get("accessSecret");
			String accessToken = (String)map.get("accessToken");
			String username = (String)map.get("username");
			Boolean signedIn = (Boolean)map.get("signedIn");
			Integer  socialAccountId = (Integer)map.get("socialAccountId");			
			String serviceProviderName = (String)map.get("serviceProviderName");
			Media media = SocialNetwork.Media.valueOf(serviceProviderName.toUpperCase());
			
			if( socialNetworkId == null){				
				socialNetworkId = socialAccountId.longValue();
			}				
			SocialNetwork account = null;
			if( socialNetworkId > 0 ){				
				try {
					account = getSocialNetwork();
				} catch (NotFoundException e) {
					account = null;
				}
			}
			
			if( account == null ){				
				Map<Media, SocialNetwork> listMap = getConnectedSocialNetworkMap();
				if( listMap.containsKey(media)){
					account = listMap.get(media);
				}else{
					account = newSocialNetwork(media);				
				}				
			}
			
			if(!StringUtils.isEmpty(accessSecret))
				account.setAccessSecret(accessSecret);
			if(!StringUtils.isEmpty(accessToken))
				account.setAccessToken(accessToken);
			if(!StringUtils.isEmpty(username))
				account.setUsername(username);
			
			socialNetworkManager.saveSocialNetwork(account);					
			this.socialNetworkId =account.getSocialAccountId();
			this.mySocialNetwork = account;
			
			return success();
		} catch (Throwable e) {
			throw new Exception(e);
		}
	}
	
	protected SocialNetwork newSocialNetwork( SocialNetwork.Media media ){	
		SocialNetwork network =  socialNetworkManager.createSocialNetwork(getUser(), media);
		return network;
	}
	
}
