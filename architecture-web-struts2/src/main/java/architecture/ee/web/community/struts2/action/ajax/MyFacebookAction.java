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

import net.sf.ehcache.Element;
import architecture.common.user.Company;
import architecture.ee.exception.NotFoundException;
import architecture.ee.web.community.social.SocialNetwork;
import architecture.ee.web.community.social.facebook.FacebookProfile;
import architecture.ee.web.community.social.facebook.FacebookServiceProvider;
import architecture.ee.web.community.social.facebook.Post;
import architecture.ee.web.community.struts2.action.support.SocialNetworkActionSupport;

public class MyFacebookAction extends SocialNetworkActionSupport {

	private int offset = 0;
			
	private int limit = 25 ;
	
	private Long socialNetworkId = -1L; 
	
	private String userId = "me";
	
	private SocialNetwork targetSocialNetwork;
	


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
	 * @return offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * @param offset 설정할 offset
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * @return limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @param limit 설정할 limit
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * @return userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId 설정할 userId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}


	private String getFacebookProfileCacheKey(String userId ){		
		StringBuilder sb = new StringBuilder();
		sb.append( getTargetSocialNetwork().getSocialAccountId() );
		sb.append( "-");
		sb.append( "profile" );
		sb.append( "-");
		sb.append(  userId );
		return sb.toString();
	}
	
	private String getHomeFeedCacheKey(){		
		StringBuilder sb = new StringBuilder();
		sb.append( getTargetSocialNetwork().getSocialAccountId() );
		sb.append( "-");
		sb.append( "homefeed" );
		sb.append( "-");
		sb.append(  offset  );
		sb.append( "-");
		sb.append(  limit  );		
		return sb.toString();
	}
	
	public FacebookProfile getFacebookProfile(){		
		FacebookProfile profile = null;
		if( isCacheEnabled()){
			String key = getFacebookProfileCacheKey(userId);			
			if( getSocialStreamsCache().get(key) == null){
				profile =  getServiceProvider().getUserProfile(userId);
				getSocialStreamsCache().put(new Element(key, profile));
			}
			profile = (FacebookProfile)getSocialStreamsCache().get( key ).getValue();
		}else{
			profile = getServiceProvider().getUserProfile(userId);
		}
		return profile ;
	}
	
	public List<Post> getHomeFeed() {
		List<Post> list = Collections.EMPTY_LIST ;		
		if( isCacheEnabled()){
			String key = getHomeFeedCacheKey();
			if( getSocialStreamsCache().get(key) == null){
				try {
					list = getServiceProvider().getHomeFeed(offset, limit);
					getSocialStreamsCache().put(new Element(key, list));
				} catch (Exception e) {
					log.warn(e);
				}
			}
			list =  (List<Post>) getSocialStreamsCache().get( key ).getValue();
		}else{
			list = getServiceProvider().getHomeFeed(offset, limit);			
		}		
		return list ;
	}
	
	public SocialNetwork getTargetSocialNetwork() {
		try {	
			if( targetSocialNetwork == null){
				if( socialNetworkId  > 0 )
					targetSocialNetwork = getSocialNetworkManager().getSocialNetworkById(socialNetworkId);
			}
			return targetSocialNetwork;
		} catch (NotFoundException e) {
			log.error(e);
			return null;
		}
	}
	
	public FacebookServiceProvider getServiceProvider () {
		return (FacebookServiceProvider)getTargetSocialNetwork().getSocialServiceProvider();
	}
	
	public String execute() throws Exception {
		if( socialNetworkId < 0 ){			
			List <SocialNetwork> list ;
			if ( getObjectType() == 1 ){					
				list = getSocialNetworkManager().getSocialNetworks(getCompany());
			}else{
				list = getSocialNetworkManager().getSocialNetworks(getUser());
			}			
			for( SocialNetwork network : list ){
				if( "facebook".toLowerCase().equals(network.getServiceProviderName()) ){				
					this.socialNetworkId = network.getSocialAccountId();
					this.targetSocialNetwork = network;
					break;
				}
			}		
		}
		return success();
	}

}
