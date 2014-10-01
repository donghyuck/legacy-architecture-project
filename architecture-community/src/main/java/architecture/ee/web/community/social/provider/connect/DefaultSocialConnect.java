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


public class DefaultSocialConnect implements SocialConnect {
	
	private Long socialConnectId = 0L;
	
	private Integer objectType = 0;
	
	private Long objectId = 0L;
		
	private Media media ;
	
	private String providerId;
	
	private String providerUserId;
	
	private String displayName;
	
	private String profileUrl;
	
	private String imageUrl;
	
	private String accessToken;
	
	private String secret;
	
	private String refreshToken;
	
	private Long expireTime;
	
	private Date creationDate;
	
	private Date modifiedDate;
				
	/**
	 * @param socialConnectId
	 * @param objectType
	 * @param objectId
	 * @param providerId
	 * @param providerUserId
	 * @param displayName
	 * @param profileUrl
	 * @param imageUrl
	 * @param accessToken
	 * @param secret
	 * @param refreshToken
	 * @param expireTime
	 * @param creationDate
	 * @param modifiedDate
	 */
	public DefaultSocialConnect(
			Long socialConnectId, 
			Integer objectType,
			Long objectId, 
			String providerId, 
			String providerUserId,
			String displayName, 
			String profileUrl, 
			String imageUrl,
			String accessToken, 
			String secret, 
			String refreshToken,
			Long expireTime, 
			Date creationDate, 
			Date modifiedDate) {

		this.socialConnectId = socialConnectId;
		this.objectType = objectType;
		this.objectId = objectId;
		this.media = Media.UNKNOWN;
		this.providerId = providerId;
		this.providerUserId = providerUserId;
		this.displayName = displayName;
		this.profileUrl = profileUrl;
		this.imageUrl = imageUrl;
		this.accessToken = accessToken;
		this.secret = secret;
		this.refreshToken = refreshToken;
		this.expireTime = expireTime;				
		this.creationDate = creationDate;
		this.modifiedDate = modifiedDate;
	}

	/**
	 * 
	 * @param socialConnectId
	 * @param objectType
	 * @param objectId
	 * @param media
	 * @param providerId
	 * @param providerUserId
	 * @param displayName
	 * @param profileUrl
	 * @param imageUrl
	 * @param accessToken
	 * @param secret
	 * @param refreshToken
	 * @param expireTime
	 * @param creationDate
	 * @param modifiedDate
	 */
	public DefaultSocialConnect(
			Long socialConnectId, 
			Integer objectType,
			Long objectId, 
			Media media,
			String providerId, 
			String providerUserId,
			String displayName, 
			String profileUrl, 
			String imageUrl,
			String accessToken, 
			String secret, 
			String refreshToken,
			Long expireTime, 
			Date creationDate, 
			Date modifiedDate) {

		this.socialConnectId = socialConnectId;
		this.objectType = objectType;
		this.objectId = objectId;
		this.media = media;
		this.providerId = providerId;
		this.providerUserId = providerUserId;
		this.displayName = displayName;
		this.profileUrl = profileUrl;
		this.imageUrl = imageUrl;
		this.accessToken = accessToken;
		this.secret = secret;
		this.refreshToken = refreshToken;
		this.expireTime = expireTime;				
		this.creationDate = creationDate;
		this.modifiedDate = modifiedDate;
	}

	/**
	 * @return socialConnectId
	 */
	public Long getSocialConnectId() {
		return socialConnectId;
	}




	/**
	 * @param socialConnectId 설정할 socialConnectId
	 */
	public void setSocialConnectId(Long socialConnectId) {
		this.socialConnectId = socialConnectId;
	}




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
	 * @return media
	 */
	public Media getMedia() {
		return media;
	}

	/**
	 * @param media 설정할 media
	 */
	public void setMedia(Media media) {
		this.media = media;
	}

	/**
	 * @return objectId
	 */
	public Long getObjectId() {
		return objectId;
	}




	/**
	 * @param objectId 설정할 objectId
	 */
	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}




	/**
	 * @return providerId
	 */
	public String getProviderId() {
		return providerId;
	}




	/**
	 * @param providerId 설정할 providerId
	 */
	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}




	/**
	 * @return providerUserId
	 */
	public String getProviderUserId() {
		return providerUserId;
	}




	/**
	 * @param providerUserId 설정할 providerUserId
	 */
	public void setProviderUserId(String providerUserId) {
		this.providerUserId = providerUserId;
	}




	/**
	 * @return displayName
	 */
	public String getDisplayName() {
		return displayName;
	}




	/**
	 * @param displayName 설정할 displayName
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}




	/**
	 * @return profileUrl
	 */
	public String getProfileUrl() {
		return profileUrl;
	}




	/**
	 * @param profileUrl 설정할 profileUrl
	 */
	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}




	/**
	 * @return imageUrl
	 */
	public String getImageUrl() {
		return imageUrl;
	}




	/**
	 * @param imageUrl 설정할 imageUrl
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}




	/**
	 * @return accessToken
	 */
	public String getAccessToken() {
		return accessToken;
	}




	/**
	 * @param accessToken 설정할 accessToken
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}




	/**
	 * @return secret
	 */
	public String getSecret() {
		return secret;
	}




	/**
	 * @param secret 설정할 secret
	 */
	public void setSecret(String secret) {
		this.secret = secret;
	}




	/**
	 * @return refreshToken
	 */
	public String getRefreshToken() {
		return refreshToken;
	}




	/**
	 * @param refreshToken 설정할 refreshToken
	 */
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}




	/**
	 * @return expireTime
	 */
	public Long getExpireTime() {
		return expireTime;
	}




	/**
	 * @param expireTime 설정할 expireTime
	 */
	public void setExpireTime(Long expireTime) {
		this.expireTime = expireTime;
	}




	/**
	 * @return creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}




	/**
	 * @param creationDate 설정할 creationDate
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}




	/**
	 * @return modifiedDate
	 */
	public Date getModifiedDate() {
		return modifiedDate;
	}




	/**
	 * @param modifiedDate 설정할 modifiedDate
	 */
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}


	
	public Connection<?> getConnection(){
		return ConnectionFactoryLocator.getConnectionFactory(providerId).createConnection(getConnectionData());
	}

	@Override
	public int getCachedSize() {
		return 0;
	}

	@Override
	public ConnectionData getConnectionData() {
		return new ConnectionData(
				this.providerId ,
				this.providerUserId,
				this.displayName ,
				this.profileUrl,
				this.imageUrl,
				this.accessToken,
				this.secret,
				this.refreshToken,
				this.expireTime 
		);
	}
}
