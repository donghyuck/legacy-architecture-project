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
package architecture.ee.web.community;

import architecture.common.model.BaseModelObject;
import architecture.common.user.User;

public interface Content extends BaseModelObject {
	
	public Long getContentId();
	
	public void setContentId(Long contentId);
	
	public String getContentType();
	
	public void setContentType(String contentType);
	
	public String getSubject();
	
	public void setSubject(String subject);
	
	public String getBody();
	
	public void setBody(String body);
	
	public User getCreator();
	
	public User getModifier();
	
	public void setCreator(User creator);
	
	public void setModifier(User modifier);	
	
}
