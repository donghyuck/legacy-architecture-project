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

import architecture.common.user.User;
import architecture.ee.web.site.page.WebPage;

public class WebPageWrapper implements Page {
	
	private WebPage page ;

	/**
	 * @param page
	 */
	public WebPageWrapper(WebPage page) {
		this.page = page;
	}
	
	public WebPage getWebPage(){
		return page;
	}

	@Override
	public int getCachedSize() {
		return page.getCachedSize();
	}

	@Override
	public Integer getObjectType() {
		return 30;
	}

	@Override
	public void setObjectType(Integer objectTyp) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Long getObjectId() {
		return page.getWebSiteId();
	}

	@Override
	public void setObjectId(Long objectId) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Long getPageId() {
		return page.getWebPageId();
	}

	@Override
	public void setPageId(Long pageId) {
		throw new UnsupportedOperationException();		
	}

	@Override
	public String getName() {
		return page.getName();
	}

	@Override
	public void setName(String name) {
		throw new UnsupportedOperationException();				
	}

	@Override
	public Integer getVersionId() {
		return 0;
	}

	@Override
	public void setVersionId(Integer versionId) {
		throw new UnsupportedOperationException();				
	}

	@Override
	public PageState getPageState() {
		return page.isEnabled() ? PageState.PUBLISHED : PageState.INCOMPLETE ;
	}

	@Override
	public String getTitle() {
		return page.getDisplayName();
	}

	@Override
	public void setTitle(String title) {
		throw new UnsupportedOperationException();						
	}

	@Override
	public String getSummary() {
		return page.getDescription();
	}

	@Override
	public void setSummary(String summary) {
		throw new UnsupportedOperationException();				
	}

	@Override
	public String getBodyText() {
		return null;
	}

	@Override
	public void setBodyText(String body) {
		throw new UnsupportedOperationException();				
	}

	@Override
	public BodyContent getBodyContent() {
		return null;
	}

	@Override
	public void setBodyContent(BodyContent bodyContent) {
		throw new UnsupportedOperationException();				
	}

	@Override
	public void setPageState(PageState state) {
		throw new UnsupportedOperationException();				
	}

	@Override
	public Date getCreationDate() {
		return page.getCreationDate();
	}

	@Override
	public void setCreationDate(Date creationDate) {
		throw new UnsupportedOperationException();				
	}

	@Override
	public Date getModifiedDate() {		
		return page.getModifiedDate();
	}

	@Override
	public void setModifiedDate(Date modifiedDate) {
		throw new UnsupportedOperationException();				
	}

	@Override
	public Map<String, String> getProperties() {
		return page.getProperties();
	}

	@Override
	public void setProperties(Map<String, String> properties) {		
		throw new UnsupportedOperationException();				
	}

	@Override
	public boolean getBooleanProperty(String name, boolean defaultValue) {
		return page.getBooleanProperty(name, defaultValue);
	}

	@Override
	public long getLongProperty(String name, long defaultValue) {
		return page.getLongProperty(name, defaultValue);
	}

	@Override
	public int getIntProperty(String name, int defaultValue) {
		return page.getIntProperty(name, defaultValue);
	}

	@Override
	public String getProperty(String name, String defaultValue) {
		return page.getProperty(name, defaultValue);
	}

	@Override
	public User getUser() {
		return null;
	}

	@Override
	public void setUser(User user) {
		throw new UnsupportedOperationException();						
	}

	/* (비Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("WebPageWrapper [");
		if (page != null)
			builder.append("page=").append(page).append(", ");
		if (super.toString() != null)
			builder.append("toString()=").append(super.toString());
		builder.append("]");
		return builder.toString();
	}
	
	
}
