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
package architecture.ee.web.community.social.provider.connect;

import java.util.Date;

import org.springframework.social.connect.ConnectionData;

import architecture.common.cache.Cacheable;

public interface SocialConnect extends Cacheable {
	
	public enum Media {
		TWITTER, 
		FACEBOOK,
		LINKEDIN,
		TUMBLR,
		GITHUB,
		TRIPIT,
		INSTAGRAM,
		DAUM,
		LIVE,
		GOOGLE,
		UNKNOWN
	};	
		
	/**
	 * @return socialConnectId
	 */
	public Long getSocialConnectId();

	/**
	 * @return objectType
	 */
	public Integer getObjectType() ;


	/**
	 * @return objectId
	 */
	public Long getObjectId();

	public Media getMedia();
	
	/**
	 * @return providerId
	 */
	public String getProviderId() ;
	/**
	 * @return providerUserId
	 */
	public String getProviderUserId() ;

	/**
	 * @return displayName
	 */
	public String getDisplayName();


	/**
	 * @return profileUrl
	 */
	public String getProfileUrl() ;



	/**
	 * @return imageUrl
	 */
	public String getImageUrl();



	/**
	 * @return accessToken
	 */
	public String getAccessToken();



	/**
	 * @return secret
	 */
	public String getSecret() ;




	/**
	 * @return refreshToken
	 */
	public String getRefreshToken() ;




	/**
	 * @return expireTime
	 */
	public Long getExpireTime();

	/**
	 * @return creationDate
	 */
	public Date getCreationDate();



	/**
	 * @return modifiedDate
	 */
	public Date getModifiedDate();
	
	
	public ConnectionData getConnectionData();

	
}
