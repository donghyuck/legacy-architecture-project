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
import architecture.ee.exception.NotFoundException;
import architecture.ee.web.community.social.SocialNetwork;
import architecture.ee.web.community.social.tumblr.BlogInfo;
import architecture.ee.web.community.social.tumblr.Post;
import architecture.ee.web.community.social.tumblr.TumblrServiceProvider;
import architecture.ee.web.community.social.tumblr.UserInfo;
import architecture.ee.web.community.struts2.action.support.SocialNetworkActionSupport;

public class MyTumblrAction extends SocialNetworkActionSupport {

	private SocialNetwork targetSocialNetwork;

	private Long socialNetworkId = -1L;
	
	private Integer blogAvatarSize = 30; //16, 24, 30, 40, 48 , 64 , 96, 128, 512 
	
	private String blogName; 
	
	
	/**
	 * @return blogAvatarSize
	 */
	public Integer getBlogAvatarSize() {
		return blogAvatarSize;
	}

	/**
	 * @param blogAvatarSize 설정할 blogAvatarSize
	 */
	public void setBlogAvatarSize(Integer blogAvatarSize) {
		this.blogAvatarSize = blogAvatarSize;
	}

	/**
	 * @return blogName
	 */
	public String getBlogName() {
		return blogName;
	}

	/**
	 * @param blogName 설정할 blogName
	 */
	public void setBlogName(String blogName) {
		this.blogName = blogName;
	}

	/**
	 * @return socialNetworkId
	 */
	public Long getSocialNetworkId() {
		return socialNetworkId;
	}

	/**
	 * @param socialNetworkId
	 *            설정할 socialNetworkId
	 */
	public void setSocialNetworkId(Long socialNetworkId) {
		this.socialNetworkId = socialNetworkId;
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
	
	public TumblrServiceProvider getServiceProvider () {
		return (TumblrServiceProvider)getTargetSocialNetwork().getSocialServiceProvider();
	}
	
	public UserInfo getTumblrProfile(){		
		return getServiceProvider().getUserProfile();
	}
	
	public BlogInfo getBlogInfo(){
		return getServiceProvider().getBlogInfo(blogName);
	}
	
	public String getBlogAvatar(){
		return getServiceProvider().getBlogAvatar(blogName, blogAvatarSize);
	}
	
	private String getCacheKey(String type){		
		StringBuilder sb = new StringBuilder();
		sb.append( getTargetSocialNetwork().getSocialAccountId() );
		sb.append( "-");
		sb.append( type );
		return sb.toString();
	}
	
	public List<Post> getDashboardPosts() {
		List<Post> list = Collections.EMPTY_LIST ;	
		if( isCacheEnabled()){
			String key = getCacheKey("dashboard");			
			if( getSocialStreamsCache().get(key) == null){
				list = getServiceProvider().getDashboardPosts();
				getSocialStreamsCache().put(new Element(key, list));
			}
		}else{
			list = getServiceProvider().getDashboardPosts();
		}
		return list ;
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
				if( "tumblr".toLowerCase().equals(network.getServiceProviderName()) ){				
					this.socialNetworkId = network.getSocialAccountId();
					this.targetSocialNetwork = network;
					break;
				}
			}		
		}
		return success();
	}
	
	public String showAvatar() throws Exception {
		
	}
}
