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


public interface Page {

	public Integer getObjectType();
	
	public void setObjectType(Integer objectTyp );
	
	public Long getObjectId();
	
	public void setObjectId(Long objectId);
	
	public Long getPageId();
	
	public void setPageId(Long pageId);
	
	public String getName();
	
	public void setName(String name);	
	
	public Integer getVersionId();
	
	public void setVersionId(Integer versionId);
	
	public PageState getPageState();
	
	public void setPageState();
		
	public String getTitle();
	
	public void setTitle(String title);
			
	public String getSummary();
	
	public void setSummary(String summary);
	
	public String getBodyText();
	
	public void setBodyText(String body);
	
	public BodyContent getBodyContent();
	
	public void setBodyContent(BodyContent bodyContent);
	
	public void setPageState(PageState state);
	
	public Date getCreationDate();

	public void setCreationDate(Date creationDate) ;

	public Date getModifiedDate() ;

	public void setModifiedDate(Date modifiedDate) ;
	
	public abstract Map<String, String> getProperties();
	
	public abstract void setProperties(Map<String, String> properties);
	
	public abstract boolean getBooleanProperty(String name, boolean defaultValue );
	
	public abstract long getLongProperty(String name, long defaultValue );
	
	public abstract int getIntProperty(String name, int defaultValue );
	
	public abstract String getProperty(String name, String defaultValue );
	
	public abstract User getUser();
	
	public abstract void setUser(User user);
			
}