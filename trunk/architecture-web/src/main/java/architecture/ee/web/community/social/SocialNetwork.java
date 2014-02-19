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

import java.util.Map;

import architecture.common.model.EntityModelObject;

public interface SocialNetwork extends EntityModelObject {

	
	public enum Media {
		TWITTER, 
		FACEBOOK,
		TUMBLR
	};	
		
	/**
	 * @return username
	 */
	public String getUsername() ;

	/**
	 * @param username 설정할 username
	 */
	public void setUsername(String username);
	
	/**
	 * @return connected
	 */
	public boolean isConnected();	
		
	public abstract int getObjectType();
	
	public abstract long getObjectId();

	public abstract long getSocialAccountId();	

	public String getServiceProviderName() ;

	/**
	 * @param servicePrivider 설정할 servicePrivider
	 */
	public void setServiceProviderName(String servicePrivider) ;

	/**
	 * @return serviceProvider
	 */
	public SocialServiceProvider getSocialServiceProvider();

	/**
	 * @param serviceProvider 설정할 serviceProvider
	 */
	public void setSocialServiceProvider(SocialServiceProvider serviceProvider);

	/**
	 * @return accessToken
	 */
	public String getAccessToken() ;

	/**
	 * @param accessToken 설정할 accessToken
	 */
	public void setAccessToken(String accessToken) ;

	/**
	 * @return accessSecret
	 */
	public String getAccessSecret() ;

	/**
	 * @param accessSecret 설정할 accessSecret
	 */
	public void setAccessSecret(String accessSecret) ;

	/**
	 * @return isSignedIn
	 */
	public boolean isSignedIn() ;

	public abstract Map<String, String> getProperties();

	/**
	 * @param properties
	 */
	public abstract void setProperties(Map<String, String> properties);
	
	public abstract String getAuthorizationUrl();

}