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
package architecture.ee.web.template.impl;

import java.io.Serializable;
import java.util.List;

import architecture.common.cache.CacheSizes;
import architecture.common.model.factory.ModelTypeFactory;
import architecture.common.model.support.EntityModelObjectSupport;
import architecture.common.user.User;
import architecture.ee.web.attachment.Attachment;
import architecture.ee.web.template.Template;

public class TemplateImpl extends EntityModelObjectSupport implements Template{

	private long templateId;
	
	private String title;
	
	private String templateType;
	
	private List<Attachment> attachments;
	
	private User creator;
	
	private User modifier;
	
	private String body;
	
	private int objectType;
	
	private long objectId;
	
	private String location;
	
	
	/**
	 * @return location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location 설정할 location
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return contentId
	 */
	public long getTemplateId() {
		return templateId;
	}

	/**
	 * @param contentId 설정할 contentId
	 */
	public void setTemplateId(long templateId) {
		this.templateId = templateId;
	}

	/**
	 * @return title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title 설정할 title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return contentType
	 */
	public String getTemplateType() {
		return templateType;
	}

	/**
	 * @param contentType 설정할 contentType
	 */
	public void setTemplateType(String contentType) {
		this.templateType = contentType;
	}

	/**
	 * @return attachments
	 */
	public List<Attachment> getAttachments() {
		return attachments;
	}

	/**
	 * @param attachments 설정할 attachments
	 */
	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	/**
	 * @return creator
	 */
	public User getCreator() {
		return creator;
	}

	/**
	 * @param creator 설정할 creator
	 */
	public void setCreator(User creator) {
		this.creator = creator;
	}

	/**
	 * @return modifier
	 */
	public User getModifier() {
		return modifier;
	}

	/**
	 * @param modifier 설정할 modifier
	 */
	public void setModifier(User modifier) {
		this.modifier = modifier;
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

	public Serializable getPrimaryKeyObject() {
		return templateId;
	}

	public int getModelObjectType() {
		return ModelTypeFactory.getTypeIdFromCode("TEMPLATE");
	}
	
	public int getCachedSize() {
		int cacheSize = CacheSizes.sizeOfLong() + 
				CacheSizes.sizeOfInt() + 
				CacheSizes.sizeOfLong() + 
				CacheSizes.sizeOfString(title) + 
				CacheSizes.sizeOfString(location) + 
				CacheSizes.sizeOfString(body) + 
				CacheSizes.sizeOfString(templateType) + 
				CacheSizes.sizeOfMap(getProperties()) +
				CacheSizes.sizeOfDate() + 
				CacheSizes.sizeOfDate() ;		
		return cacheSize;
	}

}
