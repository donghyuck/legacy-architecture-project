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
package architecture.ee.web.community.spring.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;

import architecture.common.user.SecurityHelper;
import architecture.common.user.User;
import architecture.ee.exception.NotFoundException;
import architecture.ee.web.attachment.AttachmentManager;
import architecture.ee.web.attachment.Image;
import architecture.ee.web.attachment.ImageManager;
import architecture.ee.web.community.announce.Announce;
import architecture.ee.web.community.announce.AnnounceManager;
import architecture.ee.web.community.streams.Photo;
import architecture.ee.web.community.streams.PhotoStreamsManager;
import architecture.ee.web.util.WebSiteUtils;

@Controller ("communityDataController")
@RequestMapping("/data")
public class CommunityDataController {
	private static final Log log = LogFactory.getLog(CommunityDataController.class);

	@Inject
	@Qualifier("photoStreamsManager")
	private PhotoStreamsManager photoStreamsManager ;
	
	@Inject
	@Qualifier("imageManager")
	private ImageManager imageManager ;
	
	@Inject
	@Qualifier("attachmentManager")
	private AttachmentManager attachmentManager;

	@Inject
	@Qualifier("announceManager")
	private AnnounceManager announceManager ;
	
	
	/**
	 * @return announceManager
	 */
	public AnnounceManager getAnnounceManager() {
		return announceManager;
	}

	/**
	 * @param announceManager 설정할 announceManager
	 */
	public void setAnnounceManager(AnnounceManager announceManager) {
		this.announceManager = announceManager;
	}

	/**
	 * @return photoStreamsManager
	 */
	public PhotoStreamsManager getPhotoStreamsManager() {
		return photoStreamsManager;
	}

	/**
	 * @param photoStreamsManager 설정할 photoStreamsManager
	 */
	public void setPhotoStreamsManager(PhotoStreamsManager photoStreamsManager) {
		this.photoStreamsManager = photoStreamsManager;
	}

	/**
	 * @return imageManager
	 */
	public ImageManager getImageManager() {
		return imageManager;
	}

	/**
	 * @param imageManager 설정할 imageManager
	 */
	public void setImageManager(ImageManager imageManager) {
		this.imageManager = imageManager;
	}

	/**
	 * @return attachmentManager
	 */
	public AttachmentManager getAttachmentManager() {
		return attachmentManager;
	}

	/**
	 * @param attachmentManager 설정할 attachmentManager
	 */
	public void setAttachmentManager(AttachmentManager attachmentManager) {
		this.attachmentManager = attachmentManager;
	}
	
	
	
	
	@RequestMapping(value="/image/{imageId}/stream", method=RequestMethod.GET)
	@ResponseBody
	public List<Photo>  getImageProperty(@PathVariable Long imageId, NativeWebRequest request ) throws NotFoundException {		
		User user = SecurityHelper.getUser();		
		Image image = imageManager.getImage(imageId);
		return photoStreamsManager.getPhotosByImage(image);
	}
	
	@RequestMapping(value="/announce", params={"item"}, method=RequestMethod.POST)
	@ResponseBody
	public Announce saveOrUpdate(@RequestParam AnnounceForm item, NativeWebRequest request){
		
		log.debug(item);
		
		return null;
	}
	
	@RequestMapping(value="/announce/list.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public AnnounceList  getAnnounceList (
		@RequestParam(value="objectType", defaultValue="30", required=false ) Integer objectType, 
		@RequestParam(value="objectId", defaultValue="0", required=false  ) Long objectId, 
		@RequestParam(value="page", defaultValue="0", required=false ) Integer page,
		@RequestParam(value="pageSize", defaultValue="0", required=false ) Integer pageSize,
		NativeWebRequest request ) throws NotFoundException {		
		User user = SecurityHelper.getUser();		
		if(!user.isAnonymous()){
			if( objectType == 30 && objectId == 0L ){
				objectId = WebSiteUtils.getWebSite(request.getNativeRequest(HttpServletRequest.class)).getWebSiteId();			
			}else if (objectType == 1 && objectId == 0L ){
				objectId = user.getCompanyId();		
			}
		}else{
			objectType = 30 ;
			objectId = WebSiteUtils.getWebSite(request.getNativeRequest(HttpServletRequest.class)).getWebSiteId();		
		}
		return new AnnounceList(announceManager.getAnnounces(objectType, objectId), getTotalAnnounceCount(objectType, objectId));
	}
	
	public static class AnnounceForm {
		
		private Long announceId = 0L;
		
		private Integer objectType = 0;
		
		private String subject ;
		
		private String body;
		
		private Date startDate ;
		
		private Date endDate;

		private Date modifiedDate;
		
		private Date creationDate;
		
		private User user;
		
		private Map properties;
		
		
		/**
		 * @return properties
		 */
		public Map getProperties() {
			return properties;
		}

		/**
		 * @param properties 설정할 properties
		 */
		public void setProperties(Map properties) {
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
		 * @return subject
		 */
		public String getSubject() {
			return subject;
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
		}

		/**
		 * @return startDate
		 */
		public Date getStartDate() {
			return startDate;
		}

		/**
		 * @param startDate 설정할 startDate
		 */
		public void setStartDate(Date startDate) {
			this.startDate = startDate;
		}

		/**
		 * @return endDate
		 */
		public Date getEndDate() {
			return endDate;
		}

		/**
		 * @param endDate 설정할 endDate
		 */
		public void setEndDate(Date endDate) {
			this.endDate = endDate;
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

		/* (비Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("AnnounceForm [");
			if (announceId != null)
				builder.append("announceId=").append(announceId).append(", ");
			if (objectType != null)
				builder.append("objectType=").append(objectType).append(", ");
			if (subject != null)
				builder.append("subject=").append(subject).append(", ");
			if (body != null)
				builder.append("body=").append(body).append(", ");
			if (startDate != null)
				builder.append("startDate=").append(startDate).append(", ");
			if (endDate != null)
				builder.append("endDate=").append(endDate);
			builder.append("]");
			return builder.toString();
		}
	}
	
	
	
	private int getTotalAnnounceCount(int objectType, long objectId ){
		Date now = Calendar.getInstance().getTime();		
		return announceManager.getAnnounceCount(objectType, objectId, now);
	}
	
	public static class AnnounceList {
		private List<Announce> announces ;
		private int totalCount ;
		
		/**
		 * @param announces
		 * @param totalCount
		 */
		public AnnounceList(List<Announce> announces, int totalCount) {
			super();
			this.announces = announces;
			this.totalCount = totalCount;
		}
		/**
		 * @return announces
		 */
		public List<Announce> getAnnounces() {
			return announces;
		}
		/**
		 * @param announces 설정할 announces
		 */
		public void setAnnounces(List<Announce> announces) {
			this.announces = announces;
		}
		/**
		 * @return totalCount
		 */
		public int getTotalCount() {
			return totalCount;
		}
		/**
		 * @param totalCount 설정할 totalCount
		 */
		public void setTotalCount(int totalCount) {
			this.totalCount = totalCount;
		}
	} 

}
