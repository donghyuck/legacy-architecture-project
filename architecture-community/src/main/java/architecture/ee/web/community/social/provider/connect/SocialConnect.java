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

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import architecture.common.model.PropertyAware;
import architecture.common.model.json.CustomJsonDateSerializer;

public interface SocialConnect extends PropertyAware {
	
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
	 @JsonIgnore
	public String getAccessToken();



	/**
	 * @return secret
	 */
	 @JsonIgnore
	public String getSecret() ;


	/**
	 * @return refreshToken
	 */
	 @JsonIgnore
	public String getRefreshToken() ;


	/**
	 * @return expireTime
	 */
	public Long getExpireTime();

	/**
	 * @return creationDate
	 */
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	public Date getCreationDate();

	/**
	 * @return modifiedDate
	 */
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	public Date getModifiedDate();
	
	@JsonIgnore
	public Connection<?> getConnection();
	
	 @JsonIgnore
	public ConnectionData getConnectionData();
	
}
