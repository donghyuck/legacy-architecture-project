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
package architecture.ee.web.site.page;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import architecture.common.cache.CacheSizes;
import architecture.common.model.factory.ModelTypeFactory;
import architecture.common.model.json.CustomJsonDateDeserializer;
import architecture.common.model.json.CustomJsonDateSerializer;
import architecture.common.model.support.PropertyModelSupport;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class WebPage extends PropertyModelSupport {

	private long webPageId;
	
	private long webSiteId;

	private String name;

	private String displayName;

	private String description;

	private String contentType;

	private String template;

	private boolean enabled;

	private String locale;

	private Date creationDate;

	private Date modifiedDate;

	public WebPage() {
		super();
		this.webPageId = -1L;
		this.webSiteId = -1L;
		this.name = null;
		this.displayName = null;
		this.description = null;
		this.contentType = null;
		this.template = null;
		this.enabled = false;
		this.creationDate = Calendar.getInstance().getTime();
		this.modifiedDate = creationDate;
	}

	/**
	 * @param webPageId
	 */
	public WebPage(long webPageId) {
		this();
		this.webPageId = webPageId;
	}

	/**
	 * @return webPageId
	 */
	public long getWebPageId() {
		return webPageId;
	}

	/**
	 * @param webPageId 설정할 webPageId
	 */
	public void setWebPageId(long webPageId) {
		this.webPageId = webPageId;
	}

	/**
	 * @return webSiteId
	 */
	public long getWebSiteId() {
		return webSiteId;
	}

	/**
	 * @return creationDate
	 */
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate
	 *            설정할 creationDate
	 */
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
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
	 * @param modifiedDate
	 *            설정할 modifiedDate
	 */
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/**
	 * @param webSiteId
	 *            설정할 webSiteId
	 */
	public void setWebSiteId(long webSiteId) {
		this.webSiteId = webSiteId;
	}

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            설정할 name
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            설정할 description
	 */
	public void setDescription(String description) {
		this.description = description;
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
	 * @return template
	 */
	public String getTemplate() {
		return template;
	}

	/**
	 * @param template
	 *            설정할 template
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

	/**
	 * @return enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled
	 *            설정할 enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return locale
	 */
	public String getLocale() {
		return locale;
	}

	/**
	 * @param locale
	 *            설정할 locale
	 */
	public void setLocale(String locale) {
		this.locale = locale;
	}
 
	public int getCachedSize() {
		return CacheSizes.sizeOfMap(getProperties()) 
				+ CacheSizes.sizeOfBoolean()
				+ CacheSizes.sizeOfString(locale)
				+ CacheSizes.sizeOfString(template)
				+ CacheSizes.sizeOfString(contentType)
				+ CacheSizes.sizeOfString(description)
				+ CacheSizes.sizeOfString(displayName)
				+ CacheSizes.sizeOfString(name) + CacheSizes.sizeOfLong();
	}
 
	@JsonIgnore
	public Serializable getPrimaryKeyObject() {
		return this.getWebPageId();
	}
 
	@JsonIgnore
	public int getModelObjectType() {
		return ModelTypeFactory.getTypeIdFromCode("WEBPAGE");
	}

	/* (비Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("WebPage [webPageId=").append(webPageId)
				.append(", webSiteId=").append(webSiteId).append(", ");
		if (name != null)
			builder.append("name=").append(name).append(", ");
		if (displayName != null)
			builder.append("displayName=").append(displayName).append(", ");
		if (description != null)
			builder.append("description=").append(description).append(", ");
		if (contentType != null)
			builder.append("contentType=").append(contentType).append(", ");
		if (template != null)
			builder.append("template=").append(template).append(", ");
		builder.append("enabled=").append(enabled).append(", ");
		if (locale != null)
			builder.append("locale=").append(locale).append(", ");
		if (creationDate != null)
			builder.append("creationDate=").append(creationDate).append(", ");
		if (modifiedDate != null)
			builder.append("modifiedDate=").append(modifiedDate).append(", ");
		if (getProperties() != null)
			builder.append("properties=").append(getProperties());
		builder.append("]");
		return builder.toString();
	}

}
