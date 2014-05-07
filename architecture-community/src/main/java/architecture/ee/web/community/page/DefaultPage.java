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
package architecture.ee.web.community.page;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import architecture.common.user.User;
import architecture.common.user.UserTemplate;
import architecture.common.util.StringUtils;

public class DefaultPage implements Page {

	private Integer objectType;
	private Long objectId;
	private Long pageId;
	private String name;
	private Integer versionId;
	private PageState pageState;
	private String title;
	private String summary;
	private BodyContent bodyContent;
	private Date creationDate;
	private Date modifiedDate;
	private Map<String, String> properties;
	private User user ;
	
	public DefaultPage() {
		this.name = null;
		this.pageId = -1L;
		this.objectType = -1;
		this.objectId = -1L;
		this.versionId = -1;
		this.pageState = PageState.INCOMPLETE;
		this.user = new UserTemplate(-1L);
		this.title = "";
		this.properties = new ConcurrentHashMap<String, String>();
		this.creationDate = Calendar.getInstance().getTime();
		this.modifiedDate = creationDate;
	}

	public DefaultPage(Long pageId) {
		this();
		this.pageId = pageId;
	}
	
	public DefaultPage(int objectType, long objectId) {
		this();
		this.objectType = objectType;
		this.objectId = objectId;
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
	 * @return pageId
	 */
	public Long getPageId() {
		return pageId;
	}

	/**
	 * @param pageId 설정할 pageId
	 */
	public void setPageId(Long pageId) {
		this.pageId = pageId;
	}

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name 설정할 name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return versionId
	 */
	public Integer getVersionId() {
		return versionId;
	}

	/**
	 * @param versionId 설정할 versionId
	 */
	public void setVersionId(Integer versionId) {
		this.versionId = versionId;
	}

	/**
	 * @return pageState
	 */
	public PageState getPageState() {
		return pageState;
	}

	/**
	 * @param pageState 설정할 pageState
	 */
	public void setPageState(PageState pageState) {
		this.pageState = pageState;
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

	/**
	 * @return bodyContent
	 */
	public BodyContent getBodyContent() {
		return bodyContent;
	}

	/**
	 * @param bodyContent 설정할 bodyContent
	 */
	public void setBodyContent(BodyContent bodyContent) {
		this.bodyContent = bodyContent;
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

	/**
	 * @return properties
	 */
	public Map<String, String> getProperties() {
		synchronized (this) {
			if (properties == null) {
				properties = new ConcurrentHashMap<String, String>();
			}
		}
		return properties;
	}


	/**
	 * @param properties 설정할 properties
	 */
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
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

	public int getCachedSize() {
		return 0;
	}

	public String getBodyText() {
		if( bodyContent == null)
			return null;
		else
			return bodyContent.getBodyText();
	}

	public void setBodyText(String body) {
		bodyContent.setBodyText(body);
	}

	public boolean getBooleanProperty(String name, boolean defaultValue) {
		String value = getProperties().get(name);		
		String valueToUse = StringUtils.defaultString(value, Boolean.toString(defaultValue));
		return Boolean.parseBoolean(valueToUse);
	}

	public long getLongProperty(String name, long defaultValue) {
		String value = getProperties().get(name);		
		String valueToUse = StringUtils.defaultString(value, Long.toString(defaultValue));
		return Long.parseLong(valueToUse);
	}

	public int getIntProperty(String name, int defaultValue) {
		String value = getProperties().get(name);		
		String valueToUse = StringUtils.defaultString(value, Integer.toString(defaultValue));
		return Integer.parseInt(valueToUse);
	}

	public String getProperty(String name, String defaultString) {
		String value = getProperties().get(name);
		return StringUtils.defaultString(value, defaultString);
	}

	/* (비Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DefaultPage [");
		if (objectType != null)
			builder.append("objectType=").append(objectType).append(", ");
		if (objectId != null)
			builder.append("objectId=").append(objectId).append(", ");
		if (pageId != null)
			builder.append("pageId=").append(pageId).append(", ");
		if (name != null)
			builder.append("name=").append(name).append(", ");
		if (versionId != null)
			builder.append("versionId=").append(versionId).append(", ");
		if (pageState != null)
			builder.append("pageState=").append(pageState).append(", ");
		if (title != null)
			builder.append("title=").append(title).append(", ");
		if (summary != null)
			builder.append("summary=").append(summary).append(", ");
		if (bodyContent != null)
			builder.append("bodyContent=").append(bodyContent).append(", ");
		if (creationDate != null)
			builder.append("creationDate=").append(creationDate).append(", ");
		if (modifiedDate != null)
			builder.append("modifiedDate=").append(modifiedDate).append(", ");
		if (properties != null)
			builder.append("properties=").append(properties).append(", ");
		if (user != null)
			builder.append("user=").append(user);
		builder.append("]");
		return builder.toString();
	}

	
}
