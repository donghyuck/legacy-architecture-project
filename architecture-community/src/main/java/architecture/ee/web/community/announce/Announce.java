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
package architecture.ee.web.community.announce;

import java.util.Date;
import java.util.List;

import architecture.common.model.NoNamedEntityModelObject;
import architecture.common.user.User;
import architecture.ee.web.attachment.Attachment;

public interface Announce extends NoNamedEntityModelObject {

	/**
	 * @return announceId
	 */
	public Long getAnnounceId();

	/**
	 * @param announceId 설정할 announceId
	 */
	public void setAnnounceId(Long announceId);

	/**
	 * @return objectType
	 */
	public int getObjectType() ;

	/**
	 * @param objectType 설정할 objectType
	 */
	public void setObjectType(int objectType);
	/**
	 * @return objectId
	 */
	public Long getObjectId();

	/**
	 * @param objectId 설정할 objectId
	 */
	public void setObjectId(Long objectId) ;

	/**
	 * @return userId
	 */
	public Long getUserId() ;

	/**
	 * @param userId 설정할 userId
	 */
	public void setUserId(Long userId);

	/**
	 * @return subject
	 */
	public String getSubject();

	/**
	 * @return attachments
	 */
	public List getAttachments();


	/**
	 * @param attachments 설정할 attachments
	 */
	public void setAttachments(List attachments);


	/**
	 * @param subject 설정할 subject
	 */
	public void setSubject(String subject) ;

	/**
	 * @return body
	 */
	public String getBody() ;

	/**
	 * @param body 설정할 body
	 */
	public void setBody(String body);
	/**
	 * @return startDate
	 */
	public Date getStartDate() ;

	/**
	 * @param startDate 설정할 startDate
	 */
	public void setStartDate(Date startDate) ;

	/**
	 * @return endDate
	 */
	public Date getEndDate() ;

	/**
	 * @param endDate 설정할 endDate
	 */
	public void setEndDate(Date endDate);


	/**
	 * @return user
	 */
	public User getUser() ;

	/**
	 * @param user 설정할 user
	 */
	public void setUser(User user);
	
	public void deleteAttachments(Attachment attachment);
	
	public int attachmentCount();
	
}
