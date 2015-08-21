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
package architecture.ee.web.community.comment;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.primitives.LongList;

import architecture.common.cache.CacheSizes;
import architecture.common.model.json.CustomJsonDateSerializer;
import architecture.common.user.User;
import architecture.common.user.UserTemplate;
import architecture.ee.web.community.model.ContentObject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class DefaultComment implements Comment  {
	
	private ContentObject.Status status;
	private Long commentId;
	private Long parentCommentId;
	private Integer objectType;
	private Long objectId;
	private Integer parentObjectType;
	private Long parentObjectId;	
	private String name;
	private String email;
	private String url;
	private String ip;
	private String body;
	private Date creationDate;
	private Date modifiedDate;	
	private LongList images;
	private Map<String, String> properties;
	private User user;
	
	public DefaultComment() {
		this.commentId = -1L;
		this.parentCommentId = -1L;
		this.objectType = -1;
		this.objectId = -1L;
		this.parentObjectType = -1;
		this.parentObjectId = -1L;	
		this.user = new UserTemplate(-1L);
		this.name = null;
		this.email = null;
		this.url = null;
		this.creationDate = Calendar.getInstance().getTime();
		this.modifiedDate = this.creationDate;
		this.properties = new HashMap<String, String>();
		this.status = ContentObject.Status.PUBLISHED;
		this.body = null;
	}

	
	/**
	 * @param commentId
	 */
	public DefaultComment(Long commentId) {
		this();
		this.commentId = commentId;
	}


	/**
	 * @return parentObjectType
	 */
	public Integer getParentObjectType() {
		return parentObjectType;
	}


	/**
	 * @param parentObjectType 설정할 parentObjectType
	 */
	public void setParentObjectType(Integer parentObjectType) {
		this.parentObjectType = parentObjectType;
	}


	/**
	 * @return parentObjectId
	 */
	public Long getParentObjectId() {
		return parentObjectId;
	}


	/**
	 * @param parentObjectId 설정할 parentObjectId
	 */
	public void setParentObjectId(Long parentObjectId) {
		this.parentObjectId = parentObjectId;
	}


	/**
	 * @return user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user 설정할 user
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return status
	 */
	public ContentObject.Status getStatus() {
		return status;
	}

	/**
	 * @param status 설정할 status
	 */
	public void setStatus(ContentObject.Status status) {
		this.status = status;
	}

	/**
	 * @return commentId
	 */
	public Long getCommentId() {
		return commentId;
	}

	/**
	 * @param commentId 설정할 commentId
	 */
	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}

	/**
	 * @return parentCommentId
	 */
	public Long getParentCommentId() {
		return parentCommentId;
	}

	/**
	 * @param parentCommentId 설정할 parentCommentId
	 */
	public void setParentCommentId(Long parentCommentId) {
		this.parentCommentId = parentCommentId;
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
	 * @return name
	 */
	public String getName() {
		if(!user.isAnonymous() && user.isNameVisible())
			return user.getName();
		
		return name;
	}

	/**
	 * @param name 설정할 name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return email
	 */
	public String getEmail() {
		if(!user.isAnonymous() && user.isEmailVisible())
			return user.getEmail();
		
		return email;
	}

	/**
	 * @param email 설정할 email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return url
	 */
	public String getURL() {
		return url;
	}

	/**
	 * @param url 설정할 url
	 */
	public void setURL(String url) {
		this.url = url;
	}

	/**
	 * @return ip
	 */
	public String getIPAddress() {
		return ip;
	}

	/**
	 * @param ip 설정할 ip
	 */
	public void setIPAddress(String ip) {
		this.ip = ip;
	}

	/**
	 * @return body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param body 설정할 body
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * @return creationDate
	 */
	@JsonSerialize(using = CustomJsonDateSerializer.class)
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
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	public Date getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * @param modifiedDate 설정할 modifiedDate
	 */
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/**
	 * @return images
	 */
	public LongList getImages() {
		return images;
	}

	/**
	 * @param images 설정할 images
	 */
	public void setImages(LongList images) {
		this.images = images;
	}

	/**
	 * @return properties
	 */
	public Map<String, String> getProperties() {
		return properties;
	}

	/**
	 * @param properties 설정할 properties
	 */
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
	
	
	
	@JsonIgnore
	@Override
	public int getCachedSize() {
        int size = 0;
        size += CacheSizes.sizeOfObject();
        size += CacheSizes.sizeOfLong();
        size += CacheSizes.sizeOfLong();
        size += CacheSizes.sizeOfInt();
        size += CacheSizes.sizeOfLong();
        size += CacheSizes.sizeOfInt();
        size += CacheSizes.sizeOfLong();
        size += CacheSizes.sizeOfLong();
        size += CacheSizes.sizeOfString(name);
        size += CacheSizes.sizeOfString(email);
        size += CacheSizes.sizeOfString(url);
        size += CacheSizes.sizeOfString(ip);
        size += CacheSizes.sizeOfString(body);
        size += CacheSizes.sizeOfLong();
        size += CacheSizes.sizeOfLong();
        size += CacheSizes.sizeOfBoolean();
        size += CacheSizes.sizeOfMap(properties);
        size += CacheSizes.sizeOfBoolean();
        size += CacheSizes.sizeOfInt();
        if(images != null)
            size += CacheSizes.sizeOfLong() * images.size() + CacheSizes.sizeOfObject();
        return size;
	}

	@Override
	public boolean isAnonymous() {
		return user.isAnonymous();
	}


	@Override
	public String toString() {
		return "DefaultComment [status=" + status + ", commentId=" + commentId + ", parentCommentId=" + parentCommentId
				+ ", objectType=" + objectType + ", objectId=" + objectId + ", parentObjectType=" + parentObjectType
				+ ", parentObjectId=" + parentObjectId + ", name=" + name + ", email=" + email + ", url=" + url
				+ ", ip=" + ip + ", body=" + body + ", creationDate=" + creationDate + ", modifiedDate=" + modifiedDate
				+ ", images=" + images + ", properties=" + properties + ", user=" + user + "]";
	}

}
