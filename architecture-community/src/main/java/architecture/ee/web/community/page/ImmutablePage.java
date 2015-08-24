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

import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import architecture.common.model.json.CustomJsonDateSerializer;
import architecture.common.model.json.UserDeserializer;
import architecture.common.user.User;
import architecture.ee.web.community.tag.TagDelegator;

public class ImmutablePage implements Page {

	private Page page;
	boolean allowShowContentBody = false;

	public ImmutablePage(Page page) {
		this.page = page;
		this.allowShowContentBody = false;
	}

	@JsonIgnore
	public int getCachedSize() {
		return 0;
	}

	public Integer getObjectType() {
		return page.getObjectType();
	}

	public void setObjectType(Integer objectTyp) {
		throw new UnsupportedOperationException();
	}

	public Long getObjectId() {
		return this.page.getObjectId();
	}

	@Override
	public void setObjectId(Long objectId) {
		throw new UnsupportedOperationException();
	}

	public Long getPageId() {
		return this.page.getPageId();
	}

	public void setPageId(Long pageId) {
		throw new UnsupportedOperationException();
	}

	public String getName() {
		return this.page.getName();
	}

	@Override
	public void setName(String name) {
		throw new UnsupportedOperationException();
	}

	public Integer getVersionId() {
		return this.page.getVersionId();
	}

	public void setVersionId(Integer versionId) {
		throw new UnsupportedOperationException();
	}

	public PageState getPageState() {
		return this.page.getPageState();
	}

	public String getTitle() {
		return this.page.getTitle();
	}

	public void setTitle(String title) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getSummary() {
		return this.page.getSummary();
	}

	@Override
	public void setSummary(String summary) {
		throw new UnsupportedOperationException();
	}

	public String getBodyText() {
		if (allowShowContentBody)
			return this.page.getBodyText();
		else
			return null;
	}

	@JsonIgnore
	public void setBodyText(String body) {
		throw new UnsupportedOperationException();
	}

	public BodyContent getBodyContent() {
		if (allowShowContentBody)
			return this.page.getBodyContent();
		else
			return new BodyContentAdaptor(
					(DefaultBodyContent) this.page.getBodyContent());
	}

	public void setBodyContent(BodyContent bodyContent) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setPageState(PageState state) {
		throw new UnsupportedOperationException();
	}

	@JsonSerialize(using = CustomJsonDateSerializer.class)
	public Date getCreationDate() {
		return this.page.getCreationDate();
	}

	@Override
	public void setCreationDate(Date creationDate) {
		throw new UnsupportedOperationException();
	}

	@JsonSerialize(using = CustomJsonDateSerializer.class)
	public Date getModifiedDate() {
		return this.page.getModifiedDate();
	}

	@Override
	public void setModifiedDate(Date modifiedDate) {
		throw new UnsupportedOperationException();
	}

	public Map<String, String> getProperties() {

		return this.page.getProperties();
	}

	public void setProperties(Map<String, String> properties) {
		throw new UnsupportedOperationException();
	}

	public boolean getBooleanProperty(String name, boolean defaultValue) {
		return this.page.getBooleanProperty(name, defaultValue);
	}

	public long getLongProperty(String name, long defaultValue) {
		return this.page.getLongProperty(name, defaultValue);
	}

	public int getIntProperty(String name, int defaultValue) {
		return this.page.getIntProperty(name, defaultValue);
	}

	public String getProperty(String name, String defaultValue) {
		return this.page.getProperty(name, defaultValue);
	}

	@JsonDeserialize(using = UserDeserializer.class)
	public User getUser() {
		return this.page.getUser();
	}

	public void setUser(User user) {
		throw new UnsupportedOperationException();
	}

	public static class BodyContentAdaptor implements BodyContent {

		private DefaultBodyContent body;

		public BodyContentAdaptor(DefaultBodyContent body) {
			super();
			this.body = body;
		}

		@Override
		public long getBodyId() {
			return body.getBodyId();
		}

		@Override
		public void setBodyId(long bodyId) {
			throw new UnsupportedOperationException();
		}

		@Override
		public long getPageId() {
			return body.getPageId();
		}

		@Override
		public void setPageId(long pageId) {
			throw new UnsupportedOperationException();
		}

		@Override
		public BodyType getBodyType() {
			return body.getBodyType();
		}

		public void setBodyType(BodyType bodyType) {
			throw new UnsupportedOperationException();
		}

		@JsonIgnore
		public String getBodyText() {
			return null;
		}

		@Override
		public void setBodyText(String bodyText) {
			throw new UnsupportedOperationException();
		}

		public String getFirstImageSrc() {
			return body.getFirstImageSrc();
		}

		/**
		 * @return imageCount
		 */
		public int getImageCount() {
			return body.getImageCount();
		}
	}

	@Override
	public Integer getViewCount() {
		return page.getViewCount();
	}

	@Override
	public Integer getCommentCount() {
		return page.getCommentCount();
	}
	
	@JsonIgnore
	public TagDelegator getTagDelegator() {
		return page.getTagDelegator();
	}
	
	@JsonProperty
	public String getTagsString(){
		return page.getTagsString();
	}
	
}
