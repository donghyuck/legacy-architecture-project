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

import java.io.Serializable;
import java.util.Date;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;

import com.fasterxml.jackson.annotation.JsonIgnore;

import architecture.common.model.factory.ModelTypeFactory;
import architecture.common.model.support.PropertyAwareModelObjectSupport;
import architecture.common.user.Company;
import architecture.common.user.User;

public class DefaultSocialConnect extends PropertyAwareModelObjectSupport implements SocialConnect {

    private Long socialConnectId = 0L;

    private Integer objectType = 0;

    private Long objectId = 0L;

    // private Media media ;

    private String providerId;

    private String providerUserId;

    private String displayName;

    private String profileUrl;

    private String imageUrl;

    private String accessToken;

    private String secret;

    private String refreshToken;

    private Long expireTime;

    private Date modifiedDate;

    private Date creationDate;

    private Connection<?> connection;

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
    public DefaultSocialConnect(Long socialConnectId, Integer objectType, Long objectId, String providerId,
	    String providerUserId, String displayName, String profileUrl, String imageUrl, String accessToken,
	    String secret, String refreshToken, Long expireTime, Date creationDate, Date modifiedDate) {

	this.socialConnectId = socialConnectId;
	this.objectType = objectType;
	this.objectId = objectId;
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
	this.connection = null;
    }

    /**
     * @param objectType
     * @param objectId
     * @param providerId
     */
    public DefaultSocialConnect(Integer objectType, Long objectId, Media media) {
	this.objectType = objectType;
	this.objectId = objectId;
	this.providerId = media.name().toLowerCase();
	Date now = new Date();
	this.creationDate = now;
	this.modifiedDate = now;
	this.connection = null;
    }

    public DefaultSocialConnect(User user, Media media) {
	this.objectType = user.getModelObjectType();
	this.objectId = user.getUserId();
	this.providerId = media.name().toLowerCase();
	Date now = new Date();
	this.creationDate = now;
	this.modifiedDate = now;
	this.connection = null;
    }

    public DefaultSocialConnect(Company company, Media media) {
	this.objectType = company.getModelObjectType();
	this.objectId = company.getCompanyId();
	this.providerId = media.name().toLowerCase();
	Date now = new Date();
	this.creationDate = now;
	this.modifiedDate = now;
	this.connection = null;
    }

    /**
     * @return socialConnectId
     */
    public Long getSocialConnectId() {
	return socialConnectId;
    }

    /**
     * @param socialConnectId
     *            설정할 socialConnectId
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
     * @param objectType
     *            설정할 objectType
     */
    public void setObjectType(Integer objectType) {
	this.objectType = objectType;
    }

    /**
     * @return media
     */

    /**
     * @return objectId
     */
    public Long getObjectId() {
	return objectId;
    }

    /**
     * @param objectId
     *            설정할 objectId
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
     * @param providerId
     *            설정할 providerId
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
     * @param providerUserId
     *            설정할 providerUserId
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
     * @param displayName
     *            설정할 displayName
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
     * @param profileUrl
     *            설정할 profileUrl
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
     * @param imageUrl
     *            설정할 imageUrl
     */
    public void setImageUrl(String imageUrl) {
	this.imageUrl = imageUrl;
    }

    /**
     * @return accessToken
     */
    @JsonIgnore
    public String getAccessToken() {
	return accessToken;
    }

    /**
     * @param accessToken
     *            설정할 accessToken
     */
    @JsonIgnore
    public void setAccessToken(String accessToken) {
	this.accessToken = accessToken;
    }

    /**
     * @return secret
     */
    @JsonIgnore
    public String getSecret() {
	return secret;
    }

    /**
     * @param secret
     *            설정할 secret
     */
    @JsonIgnore
    public void setSecret(String secret) {
	this.secret = secret;
    }

    /**
     * @return refreshToken
     */
    @JsonIgnore
    public String getRefreshToken() {
	return refreshToken;
    }

    /**
     * @param refreshToken
     *            설정할 refreshToken
     */
    @JsonIgnore
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
     * @param expireTime
     *            설정할 expireTime
     */
    public void setExpireTime(Long expireTime) {
	this.expireTime = expireTime;
    }

    /**
     * @return modifiedDate
     */
    public Date getModifiedDate() {
	return modifiedDate;
    }

    /**
     * @param modifiedDate
     *            설정할 modifiedDate
     */
    public void setModifiedDate(Date modifiedDate) {
	this.modifiedDate = modifiedDate;
    }

    /**
     * @return creationDate
     */
    public Date getCreationDate() {
	return creationDate;
    }

    /**
     * @param creationDate
     *            설정할 creationDate
     */
    public void setCreationDate(Date creationDate) {
	this.creationDate = creationDate;
    }

    @JsonIgnore
    public int getCachedSize() {
	return 0;
    }

    @JsonIgnore
    public Connection<?> getConnection() {
	if (this.connection == null)
	    this.connection = ConnectionFactoryLocator.getConnectionFactory(providerId)
		    .createConnection(getConnectionData());
	return this.connection;
    }

    @JsonIgnore
    public ConnectionData getConnectionData() {
	return new ConnectionData(this.providerId, this.providerUserId, this.displayName, this.profileUrl,
		this.imageUrl, this.accessToken, this.secret, this.refreshToken, this.expireTime);
    }

    @JsonIgnore
    public Serializable getPrimaryKeyObject() {
	return this.getSocialConnectId();
    }

    @JsonIgnore
    public int getModelObjectType() {
	return ModelTypeFactory.getTypeIdFromCode("SOCIAL_CONNECT");
    }

    /*
     * (비Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("DefaultSocialConnect [");
	if (socialConnectId != null)
	    builder.append("socialConnectId=").append(socialConnectId).append(", ");
	if (objectType != null)
	    builder.append("objectType=").append(objectType).append(", ");
	if (objectId != null)
	    builder.append("objectId=").append(objectId).append(", ");
	if (providerId != null)
	    builder.append("providerId=").append(providerId).append(", ");
	if (providerUserId != null)
	    builder.append("providerUserId=").append(providerUserId).append(", ");
	if (displayName != null)
	    builder.append("displayName=").append(displayName).append(", ");
	if (profileUrl != null)
	    builder.append("profileUrl=").append(profileUrl).append(", ");
	if (imageUrl != null)
	    builder.append("imageUrl=").append(imageUrl).append(", ");
	if (expireTime != null)
	    builder.append("expireTime=").append(expireTime).append(", ");
	if (modifiedDate != null)
	    builder.append("modifiedDate=").append(modifiedDate).append(", ");
	if (creationDate != null)
	    builder.append("creationDate=").append(creationDate);
	builder.append("]");
	return builder.toString();
    }

}
