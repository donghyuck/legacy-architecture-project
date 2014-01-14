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

import architecture.common.model.v2.support.PropertyEntityModelSupport;
import architecture.common.user.User;
import architecture.ee.web.community.Content;
import architecture.ee.web.community.ContentModel;

public class ContentModelImpl extends PropertyEntityModelSupport<Content> implements ContentModel {

	private Long contentId = -1L;
	
	private String subject;

	private User creator;

	private User modifier;
	
	private String body;
	
	private String summary;

	private String contentType;	
	
	
	/**
	 * @return summary
	 */
	public String getSummary() {
		return summary;
	}

	/**
	 * @param summary 설정할 summary
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Serializable getPrimaryKeyObject() {
		return contentId;
	}
	
	public void setPrimaryKeyObject(Serializable primaryKeyObj) {
		this.contentId = (Long) primaryKeyObj;		
	}


	/**
	 * @return subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject
	 *            설정할 subject
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return creator
	 */
	public User getCreator() {
		return creator;
	}

	/**
	 * @param creator
	 *            설정할 creator
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
	 * @param modifier
	 *            설정할 modifier
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
	 * @param body
	 *            설정할 body
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * @return contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @param contentType
	 *            설정할 contentType
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * @return contentId
	 */
	public Long getContentId() {
		return contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;		
	}
	
	public int getCachedSize() {
		return 0;
	}

}