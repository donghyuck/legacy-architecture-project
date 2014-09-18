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

import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.Element;

import org.scribe.model.Token;

import architecture.common.user.Company;
import architecture.common.user.authentication.UnAuthorizedException;
import architecture.ee.exception.NotFoundException;
import architecture.ee.web.community.social.SocialNetwork;
import architecture.ee.web.community.social.SocialNetworkManager;
import architecture.ee.web.community.social.twitter.Tweet;
import architecture.ee.web.community.social.twitter.TwitterProfile;
import architecture.ee.web.community.social.twitter.TwitterServiceProvider;
import architecture.ee.web.community.struts2.action.support.SocialNetworkActionSupport;
import architecture.ee.web.community.struts2.action.support.SocialNetworkAware;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;
import architecture.ee.web.util.ParamUtils;

public class MyTwitterAction extends SocialNetworkActionSupport {

	private Long socialNetworkId = -1L; 
	
	private SocialNetwork targetSocialNetwork;
	
	/**
	 * @return targetSocialNetwork
	 */
	public SocialNetwork getTargetSocialNetwork() {
		try {	
			if( targetSocialNetwork == null){
				targetSocialNetwork = getSocialNetworkManager().getSocialNetworkById(socialNetworkId);
			}
			return targetSocialNetwork;
		} catch (NotFoundException e) {
			log.error(e);
			return null;
		}
	}
	
	/**
	 * @return socialNetworkId
	 */
	public Long getSocialNetworkId() {
		return socialNetworkId;
	}

	
	public TwitterServiceProvider getServiceProvider () {
		return (TwitterServiceProvider)getTargetSocialNetwork().getSocialServiceProvider();
	}
	
	
	public TwitterProfile getTwitterProfile () throws UnAuthorizedException {
		return getServiceProvider().authenticate();
	}
	
	private String getTimelineCacheKey(String type){		
		StringBuilder sb = new StringBuilder();
		sb.append( getTargetSocialNetwork().getSocialAccountId() );
		sb.append( "-");
		sb.append( type );
		return sb.toString();
	}

	public List<Tweet> getUserTimeline () {		
		List<Tweet> list = Collections.EMPTY_LIST ;	
		if( isCacheEnabled()){
			String key = getTimelineCacheKey("usertimeline");			
			if( getSocialStreamsCache().get(key) == null){
				list = getServiceProvider().getUserTimeline();
				getSocialStreamsCache().put(new Element(key, list));
			}
			list = (List<Tweet>)getSocialStreamsCache().get( key ).getValue();
		}else{
			list = getServiceProvider().getUserTimeline();
		}
		return list ;
	}

	public List<Tweet> getHomeTimeline () {
		List<Tweet> list = Collections.EMPTY_LIST ;	
		if( isCacheEnabled()){
			String key = getTimelineCacheKey("hometimeline");			
			if( getSocialStreamsCache().get(key) == null){
				list = getServiceProvider().getHomeTimeline();
				getSocialStreamsCache().put(new Element(key, list));
			}
			list = (List<Tweet>)getSocialStreamsCache().get( key ).getValue();
		}else{
			list = getServiceProvider().getHomeTimeline();
		}
		return list ;
	}
	
	public String getAuthorizationUrl () {
		return getServiceProvider().getAuthorizationUrl();
	}
	
	
	/**
	 * @param socialNetworkId 설정할 socialNetworkId
	 */
	public void setSocialNetworkId(Long socialAccountId) {
		this.socialNetworkId = socialAccountId;
	}

	public String execute() throws Exception {		
		if( socialNetworkId < 0 ){
						
			List <SocialNetwork> list ;
			if ( getObjectType() == 1 ){				
				list = getSocialNetworkManager().getSocialNetworks(getCompany());
			}else{
				list = getSocialNetworkManager().getSocialNetworks(getUser());
			}	
			for( SocialNetwork account : list ){
				if( "twitter".toLowerCase().equals(account.getServiceProviderName()) ){				
					socialNetworkId = account.getSocialAccountId();
					targetSocialNetwork = account;
					break;
				}
			}		
		}
		return success();
	}
	
}
