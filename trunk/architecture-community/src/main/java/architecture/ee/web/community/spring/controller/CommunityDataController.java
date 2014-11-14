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
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;

import architecture.common.user.SecurityHelper;
import architecture.common.user.User;
import architecture.common.user.authentication.UnAuthorizedException;
import architecture.ee.exception.NotFoundException;
import architecture.ee.web.attachment.AttachmentManager;
import architecture.ee.web.attachment.Image;
import architecture.ee.web.attachment.ImageManager;
import architecture.ee.web.community.announce.Announce;
import architecture.ee.web.community.announce.AnnounceManager;
import architecture.ee.web.community.announce.AnnounceNotFoundException;
import architecture.ee.web.community.announce.impl.DefaultAnnounce;
import architecture.ee.web.community.streams.Photo;
import architecture.ee.web.community.streams.PhotoStreamsManager;
import architecture.ee.web.site.WebSiteNotFoundException;
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
	public List<Photo>  getImageProperty(
			@PathVariable Long imageId, 
			NativeWebRequest request ) throws NotFoundException {		
		User user = SecurityHelper.getUser();		
		Image image = imageManager.getImage(imageId);
		return photoStreamsManager.getPhotosByImage(image);
	}
	
	@Secured({"ROLE_USER"})
	@RequestMapping(value="/announce/save.json", method=RequestMethod.POST)
	@ResponseBody
	public Announce saveAnnounce(
			@RequestBody DefaultAnnounce announce, 
			NativeWebRequest request) throws AnnounceNotFoundException, WebSiteNotFoundException{		
		User user = SecurityHelper.getUser();
		if(announce.getUser() == null && announce.getAnnounceId() == 0)
			announce.setUser(user);
		
		if( user.isAnonymous() || user.getUserId() != announce.getUser().getUserId() )
			throw new UnAuthorizedException();
				
		Announce target ;
		if( announce.getAnnounceId() > 0){
			target = announceManager.getAnnounce(announce.getAnnounceId());
		}else{
			if( announce.getObjectType() == 30 && announce.getObjectId() == 0L ){
				announce.setObjectId(WebSiteUtils.getWebSite(request.getNativeRequest(HttpServletRequest.class)).getWebSiteId());			
			}else if (announce.getObjectType() == 1 && announce.getObjectId() == 0L ){
				announce.setObjectId(user.getCompanyId());		
			}
			target = announceManager.createAnnounce(user, announce.getObjectType() , announce.getObjectId());
		}
		
		target.setSubject(announce.getSubject());
		target.setBody(announce.getBody());	
		target.setStartDate( announce.getStartDate());
		target.setEndDate(announce.getEndDate());
		if(target.getAnnounceId() > 0 ){
			announceManager.updateAnnounce(target);		
		}else{
			announceManager.addAnnounce(target);
		}		
		return target;
	}
	
	@Secured({"ROLE_USER"})
	@RequestMapping(value="/announce/{announceId}/destory.json", method=RequestMethod.POST)
	@ResponseBody
	public Boolean destoryAnnounce(@PathVariable Long announceId, NativeWebRequest request){
		User user = SecurityHelper.getUser();
			
		
		return true;
	}
	
	@Secured({"ROLE_ANONYMOUS"})
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
