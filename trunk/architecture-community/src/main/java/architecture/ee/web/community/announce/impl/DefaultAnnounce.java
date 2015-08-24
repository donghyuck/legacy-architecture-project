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
package architecture.ee.web.community.announce.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import architecture.common.model.factory.ModelTypeFactory;
import architecture.common.model.json.CustomJsonDateDeserializer;
import architecture.common.model.json.CustomJsonDateSerializer;
import architecture.common.model.json.UserDeserializer;
import architecture.common.model.support.NoNamedEntityModelObjectSupport;
import architecture.common.user.User;
import architecture.ee.web.attachment.Attachment;
import architecture.ee.web.community.announce.Announce;

public class DefaultAnnounce extends NoNamedEntityModelObjectSupport implements Announce {

	private Long announceId;
	private int objectType ;
	private Long objectId;
	private Long userId ;
	private String subject ;
	private String body;
	private Date startDate;
	private Date endDate;
	private User user;
	private List<Attachment> attachments;
	
	private String firstImageSrc ;
	private int imageCount = 0;
	
	/**
	 * 
	 */
	public DefaultAnnounce() {
		this.announceId = -1L;
		this.subject = "";
		this.attachments = new ArrayList<Attachment>();
		Date now = new Date();
		this.startDate = now;
		this.endDate = now;
		this.firstImageSrc =null;
		super.setCreationDate(now);
		super.setModifiedDate(now);
	}
	
	

	/**
	 * @param announceId
	 */
	public DefaultAnnounce(Long announceId) {
		this.announceId = announceId;
		subject = "";
		attachments = new ArrayList<Attachment>();
		Date now = new Date();
		startDate = null;
		endDate = null;
		this.firstImageSrc = null;
		super.setCreationDate(now);
		super.setModifiedDate(now);
	}



	/**
	 * @param announceId
	 * @param objectType
	 * @param objectId
	 * @param user
	 */
	public DefaultAnnounce(Long announceId, int objectType, Long objectId, User user) {
		this.announceId = announceId;
		this.objectType = objectType;
		this.objectId = objectId;
		this.user = user;
		this.subject = "";
		this.attachments = new ArrayList<Attachment>();
		Date now = new Date();
		this.startDate = null;
		this.endDate = null;
		this.userId = user == null ? -1L : user.getUserId();				
		this.firstImageSrc = null;
		super.setCreationDate(now);
		super.setModifiedDate(now);
	}

	/**
	 * @return announceId
	 */
	public Long getAnnounceId() {
		return announceId;
	}

	/**
	 * @param announceId 설정할 announceId
	 */
	public void setAnnounceId(Long announceId) {
		this.announceId = announceId;
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
	 * @return userId
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * @param userId 설정할 userId
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * @return subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @return attachments
	 */
	public List getAttachments() {
		return attachments;
	}



	/**
	 * @param attachments 설정할 attachments
	 */
	public void setAttachments(List attachments) {
		this.attachments = attachments;
	}

	

	/**
	 * @param subject 설정할 subject
	 */
	public void setSubject(String subject) {
		this.subject = subject;
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
		
		Document doc = Jsoup.parse(this.body);		
		Elements  links = doc.select("img");
		this.imageCount = links.size();
		if( imageCount > 0 )
			firstImageSrc = links.first().attr("src");	
		
	}

	public String getFirstImageSrc(){
		return this.firstImageSrc;
	}
	
	/**
	 * @return imageCount
	 */
	public int getImageCount() {
		return imageCount;
	}



	/**
	 * @return startDate
	 */
	@JsonSerialize(using = CustomJsonDateSerializer.class)	
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate 설정할 startDate
	 */
	
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	public void setStartDate(Date startDate) {
		if( startDate == null )
			throw new NullPointerException("Start data cannot be null.");
		
		if( this.startDate != null && this.startDate.getTime() == startDate.getTime())
			return;
		
		if( endDate != null && startDate.getTime() > endDate.getTime())
		{
			throw new IllegalArgumentException();
		}else{
			this.startDate = startDate;
			return;
		}
	}

	/**
	 * @return endDate
	 */
	
	@JsonSerialize(using = CustomJsonDateSerializer.class)	
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate 설정할 endDate
	 */
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	public void setEndDate(Date endDate) {
		
		if( this.endDate != null && endDate != null && this.endDate.getTime() == endDate.getTime())
			return ;
		
		if( endDate != null && endDate.getTime() < this.startDate.getTime())
			throw new IllegalArgumentException();
		
		if(endDate != null)
			this.endDate = endDate;
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
	
	@JsonDeserialize(using = UserDeserializer.class)
	public void setUser(User user) {
		this.user = user;
	}

	public void deleteAttachments(Attachment attachment) {
		this.attachments.remove(attachment);
	}
	
	public int attachmentCount(){
		return attachments.size(); 
	}
	
	@JsonIgnore
	public Serializable getPrimaryKeyObject() {
		return announceId;
	}

	@JsonIgnore
	public int getModelObjectType() {
		return ModelTypeFactory.getTypeIdFromCode("ANNOUNCE");
	}

	@JsonIgnore
	public int getCachedSize() {
		return 0;
	}
	
	/* (비Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@JsonIgnore
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DefaultAnnounce [");
		if (announceId != null) {
			builder.append("announceId=");
			builder.append(announceId);
			builder.append(", ");
		}
		if (userId != null) {
			builder.append("userId=");
			builder.append(userId);
			builder.append(", ");
		}
		if (subject != null) {
			builder.append("subject=");
			builder.append(subject);
			builder.append(", ");
		}
		if (body != null) {
			builder.append("body=");
			builder.append(body);
			builder.append(", ");
		}
		if (startDate != null) {
			builder.append("startDate=");
			builder.append(startDate);
			builder.append(", ");
		}
		if (endDate != null) {
			builder.append("endDate=");
			builder.append(endDate);
		}
		builder.append("]");
		return builder.toString();
	}

}
