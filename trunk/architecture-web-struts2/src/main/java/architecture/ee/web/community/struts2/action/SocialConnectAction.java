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

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import architecture.ee.exception.NotFoundException;
import architecture.ee.web.community.social.SocialNetwork;
import architecture.ee.web.community.social.SocialNetwork.Media;
import architecture.ee.web.community.social.SocialNetworkManager;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;

import com.opensymphony.xwork2.Preparable;

public class SocialConnectAction extends FrameworkActionSupport implements  Preparable {
	
	private static final String DOMAIN_NAME_KEY = "domainName";
	
	private String domainName ;
	
	private String media ;
	
	private SocialNetworkManager socialNetworkManager;
	
	private Long socialNetworkId = -1L; 
	
	private SocialNetwork socialNetwork = null ;	
		
	public String execute() throws Exception {
		
		if(StringUtils.isNotEmpty(domainName)){			
			HttpSession session = request.getSession(true);
			String domainNameInSession = (String) session.getAttribute(DOMAIN_NAME_KEY);
			
			if( !StringUtils.equals(domainName, domainNameInSession)){
				session.setAttribute(DOMAIN_NAME_KEY, domainName);
				getSession().put(domainName, domainName);
			}	
			
		}
		return success();
	}	
	
	public String getDomainName() {
		if(StringUtils.isEmpty(domainName)){	
			if( getSession().containsKey(DOMAIN_NAME_KEY)){
				this.domainName = (String) getSession().get(DOMAIN_NAME_KEY);
			}else{
				this.domainName = request.getLocalName() ;
			}
		}
		return domainName;
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
	 * @return socialNetwork
	 */
	public SocialNetwork getMySocialNetwork() {
		return socialNetwork;
	}

	/**
	 * @param socialNetwork 설정할 socialNetwork
	 */
	public void setMySocialNetwork(SocialNetwork mySocialNetwork) {
		this.socialNetwork = mySocialNetwork;
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

	
	public Media getMediaType(){
		return Media.valueOf(media.toUpperCase());
	}
	
	public SocialNetwork getSocialNetwork() throws NotFoundException{
		if( socialNetwork == null && socialNetworkId > 0 ){			
			this.socialNetwork = socialNetworkManager.getSocialNetworkById(this.socialNetworkId);
		}else{		
			if( StringUtils.isNotEmpty(media)){
				Media selectedMedia = SocialNetwork.Media.valueOf(media.toUpperCase());
				socialNetwork = newSocialNetwork(selectedMedia);				
			}
		}
		return socialNetwork;
	}
	
	protected SocialNetwork newSocialNetwork( SocialNetwork.Media media ){	
		SocialNetwork network =  socialNetworkManager.createSocialNetwork(getUser(), media);
		return network;
	}
	
	public void prepare() throws Exception {
		response.addHeader("Access-Control-Allow-Origin", "*");
	}
	
}