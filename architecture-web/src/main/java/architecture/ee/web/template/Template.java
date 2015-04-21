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
package architecture.ee.web.template;

import java.util.List;

import architecture.common.model.EntityModelObject;
import architecture.common.user.User;
import architecture.ee.web.attachment.Attachment;

public interface Template extends EntityModelObject {

	public long getTemplateId();
	
	public void setTemplateId(long contentId);
	
	public String getTitle();
	
	public void setTitle(String title);
	
	public String getTemplateType();
	
	public void setTemplateType(String contentType);
	
	public List<Attachment> getAttachments();
	
	public User getCreator();
	
	public void setCreator(User creator);
	
	public User getModifier();
	
	public void setModifier(User modifier);
	
	public String getBody();
	
	public void setBody(String contentBody);
		
	public long getObjectId();
	
	public int getObjectType();
	
	public void setObjectId(long objectId);
	
	public void setObjectType(int objectType);
	
	public void setLocation(String location);
	
	public String getLocation();
	
}
