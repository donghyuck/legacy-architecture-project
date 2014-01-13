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
package architecture.ee.web.community.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

import architecture.common.model.factory.ModelTypeFactory;
import architecture.common.model.support.EntityModelObjectSupport;
import architecture.common.user.User;
import architecture.ee.web.community.Comment;

public class CommentImpl extends EntityModelObjectSupport implements Comment  {

	private Long commentId ;
	private Long parentCommentId ;
	
	private int objectType ;
	private Long objectId;
	private int parentObjectType ;
	private Long parentObjectId;
	
	private Long userId ; 
	private String email;
	private String url ;
	private String ip;
	private String body;
	
	private User user;
	
	
	
	/**
	 * 
	 */
	public CommentImpl() {
		commentId = -1L;
		parentCommentId = -1L;
		objectType = 0 ;
		objectId = -1L;
		parentObjectType = 0 ;
		parentObjectId = -1L;		
		userId = -1L;
		
		super.setCreationDate(new Date());
		super.setModifiedDate(getCreationDate());
		super.setProperties(new HashMap());
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
	 * @param user 설정할 user
	 */
	public void setUser(User user) {
		this.user = user;
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
	 * @return parentObjectType
	 */
	public int getParentObjectType() {
		return parentObjectType;
	}
	/**
	 * @param parentObjectType 설정할 parentObjectType
	 */
	public void setParentObjectType(int parentObjectType) {
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
	 * @return userId
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * @param userId 설정할 userId
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * @return email
	 */
	public String getEmail() {
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
        if(url == null || "".equals(url.trim()))
            this.url = null ; 
        else
        if(url.indexOf("://") == -1)
        	this.url = (new StringBuilder()).append("http://").append(url).toString();
        else
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
        if(ip == null || "".equals(ip.trim()) || ip.length() > 15)
            this.ip = null;
        else
            this.ip = ip;
	}
	/**
	 * @return body
	 */
	public String getBodyText() {
		return body;
	}
	/**
	 * @param body 설정할 body
	 */
	public void setBodyText(String body) {
		this.body = body;
	}
	public Serializable getPrimaryKeyObject() {
		return commentId;
	}
	public int getModelObjectType() {
		return ModelTypeFactory.getTypeIdFromCode("COMMENT");
	}
	public int getCachedSize() {
		return 0;
	}
	public User getUser() {
		return user;
	}
	public boolean isAnonymous() {
		if( userId < 0 )
			return true;
		return false;
	}
	
	
}
