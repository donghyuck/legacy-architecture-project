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
package architecture.ee.web.community.social.impl;

import java.io.Serializable;

import org.scribe.model.Token;

import architecture.common.cache.CacheSizes;
import architecture.common.model.factory.ModelTypeFactory;
import architecture.common.model.support.EntityModelObjectSupport;
import architecture.ee.web.community.social.SocialNetwork;
import architecture.ee.web.community.social.SocialServiceProvider;

public class SocailNetworkImpl extends EntityModelObjectSupport implements SocialNetwork {

	private Long socialAccountId = -1L;
	private Integer objectType;
	private Long objectId;
	private String servicePrividerName;
	private String accessToken;
	private String accessSecret ;
	private boolean isSignedIn = false;
	private boolean connected = false ;
	private String username;
		
	
	/**
	 * @return username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username 설정할 username
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	

	private SocialServiceProvider serviceProvider;	
	
	public SocailNetworkImpl() {
		
	}
	
	/**
	 * @return connected
	 */
	public boolean isConnected() {
		return connected;
	}


	/**
	 * @param connected 설정할 connected
	 */
	public void setConnected(boolean connected) {
		this.connected = connected;
	}


	public Serializable getPrimaryKeyObject() {
		return socialAccountId;
	}

	/**
	 * @return socialAccountId
	 */
	public long getSocialAccountId() {
		return socialAccountId;
	}

	/**
	 * @param socialAccountId 설정할 socialAccountId
	 */
	public void setSocialAccountId(Long socialAccountId) {
		this.socialAccountId = socialAccountId;
	}

	/**
	 * @return objectType
	 */
	public int getObjectType() {
		return objectType;
	}

	/**
	 * @param objectType 설정할 objectType
	 */
	public void setObjectType(int objectType) {
		this.objectType = objectType;
	}

	/**
	 * @return objectId
	 */
	public long getObjectId() {
		return objectId;
	}

	/**
	 * @param objectId 설정할 objectId
	 */
	public void setObjectId(long objectId) {
		this.objectId = objectId;
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
	 * @return accessSecret
	 */
	public String getAccessSecret() {
		return accessSecret;
	}

	/**
	 * @param accessSecret 설정할 accessSecret
	 */
	public void setAccessSecret(String accessSecret) {
		this.accessSecret = accessSecret;
	}

	/**
	 * @return servicePrividerName
	 */
	public String getServiceProviderName() {
		return servicePrividerName;
	}

	/**
	 * @param servicePrividerName 설정할 servicePrividerName
	 */
	public void setServiceProviderName(String servicePrividerName) {
		this.servicePrividerName = servicePrividerName;
	}

	/**
	 * @return serviceProvider
	 */
	public SocialServiceProvider getSocialServiceProvider() {
		return serviceProvider;
	}

	/**
	 * @param serviceProvider 설정할 serviceProvider
	 */
	public void setSocialServiceProvider(SocialServiceProvider serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	/**
	 * @return isSignedIn
	 */
	public boolean isSignedIn() {
		return isSignedIn;
	}

	/**
	 * @param isSignedIn 설정할 isSignedIn
	 */
	public void setSignedIn(boolean isSignedIn) {
		this.isSignedIn = isSignedIn;
	}

	public int getModelObjectType() {
		return ModelTypeFactory.getTypeIdFromCode("SOCIAL_NETWORK");
	}
		
	public int getCachedSize() {
		return CacheSizes.sizeOfLong() 
				+ CacheSizes.sizeOfLong() 
				+ CacheSizes.sizeOfInt() 
				+ CacheSizes.sizeOfString(servicePrividerName)
				+ CacheSizes.sizeOfString(accessToken)
				+ CacheSizes.sizeOfString(accessSecret)
				+ CacheSizes.sizeOfBoolean()
				+ CacheSizes.sizeOfMap(getProperties())
				+ CacheSizes.sizeOfDate() + CacheSizes.sizeOfDate();
	}

	/* (비Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SocailNetworkImpl [");
		if (socialAccountId != null) {
			builder.append("socialAccountId=");
			builder.append(socialAccountId);
			builder.append(", ");
		}
		if (objectType != null) {
			builder.append("objectType=");
			builder.append(objectType);
			builder.append(", ");
		}
		if (objectId != null) {
			builder.append("objectId=");
			builder.append(objectId);
			builder.append(", ");
		}
		if (servicePrividerName != null) {
			builder.append("servicePrividerName=");
			builder.append(servicePrividerName);
			builder.append(", ");
		}
		if (accessToken != null) {
			builder.append("accessToken=");
			builder.append(accessToken);
			builder.append(", ");
		}
		if (accessSecret != null) {
			builder.append("accessSecret=");
			builder.append(accessSecret);
			builder.append(", ");
		}
		builder.append("isSignedIn=");
		builder.append(isSignedIn);
		builder.append(", ");
		if (serviceProvider != null) {
			builder.append("serviceProvider=");
			builder.append(serviceProvider);
		}
		builder.append("]");
		return builder.toString();
	}

	public String getAuthorizationUrl() {
		if(serviceProvider!=null)
			try {
				return serviceProvider.getAuthorizationUrl();						
			} catch (Exception e) {
			}		
		return "";
	}

}
