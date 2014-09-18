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
package architecture.ee.web.community.struts2.action.support;

import net.sf.ehcache.Cache;
import architecture.ee.web.community.social.SocialNetworkManager;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;

public class SocialNetworkActionSupport extends FrameworkActionSupport implements SocialNetworkAware  {

	private Cache socialStreamsCache;
	private SocialNetworkManager socialNetworkManager;	
	private Integer objectType = 2 ;
	
	/**
	 * @return objectType
	 */
	public Integer getObjectType() {
		return objectType;
	}

	/**
	 * @param objectType 설정할 objectType
	 */
	public void setObjectType(Integer objectType) {
		this.objectType = objectType;
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
	 * @return socialStreamsCache
	 */
	public Cache getSocialStreamsCache() {
		return socialStreamsCache;
	}

	/**
	 * @param socialStreamsCache 설정할 socialStreamsCache
	 */
	public void setSocialStreamsCache(Cache socialStreamsCache) {
		this.socialStreamsCache = socialStreamsCache;
	}
	
	public boolean isCacheEnabled(){
		
		if ( socialStreamsCache != null)
			return true;
		
		return false;
	}
	


	
}