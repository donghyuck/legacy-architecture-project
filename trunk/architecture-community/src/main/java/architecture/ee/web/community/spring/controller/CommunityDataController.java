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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Controller ("community-data-controller")
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
	

	/**
	 * get streams photo by imageId
	 * 
	 * @param imageId
	 * @param request
	 * @return
	 * @throws NotFoundException
	 */
	@RequestMapping(value="/streams/photos/get.json", method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public List<Photo>  getStreamPhoto(@RequestParam(value="imageId", defaultValue="0", required=true ) Long imageId, NativeWebRequest request ) throws NotFoundException {		
		Image image = imageManager.getImage(imageId);
		return photoStreamsManager.getPhotosByImage(image);
	}

	/**
	 * get stream photo by linkId
	 * @param linkId
	 * @param request
	 * @return
	 * @throws NotFoundException
	 */
			
	@RequestMapping(value="/streams/photos/getByLink.json", method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public Photo getStreamPhotoByLink(@RequestParam(value="linkId", required=true ) String linkId, NativeWebRequest request ) throws NotFoundException {		
		return photoStreamsManager.getPhotoById(linkId);
	}
	
	
	@RequestMapping(value="/streams/photos/insert.json",method={RequestMethod.POST} )
	@ResponseBody
	public void insertPhotoStream(@RequestParam(value="imageId", defaultValue="0", required=true ) Long imageId, NativeWebRequest request) throws NotFoundException {
		User user = SecurityHelper.getUser();		
		if(user.isAnonymous())
			throw new UnAuthorizedException();		
		
		Image image = imageManager.getImage(imageId);
		photoStreamsManager.addImage(image, user);
		
		
	}
	
	@RequestMapping(value="/streams/photos/delete.json",method={RequestMethod.POST} )
	@ResponseBody
	public void deletePhotosStreams(@RequestParam(value="imageId", defaultValue="0", required=true ) Long imageId, NativeWebRequest request) throws NotFoundException{		
		User user = SecurityHelper.getUser();		
		if(user.isAnonymous())
			throw new UnAuthorizedException();			
		Image image = imageManager.getImage(imageId);
		
		photoStreamsManager.deletePhotos(image, user);
	}
	
	@RequestMapping(value="/streams/photos/list.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public PhotoList  getStreamPhotoList(
			@RequestParam(value="objectType", defaultValue="2", required=false ) Integer objectType,
			@RequestParam(value="startIndex", defaultValue="0", required=false ) Integer startIndex,
			@RequestParam(value="pageSize", defaultValue="15", required=false ) Integer pageSize,
			NativeWebRequest request ) throws NotFoundException {		
		User user = SecurityHelper.getUser();		
		return getStreamPhotoList(objectType, startIndex, pageSize, request.getNativeRequest(HttpServletRequest.class));
	}
	
	private PhotoList getStreamPhotoList(int objectType, int startIndex, int pageSize, HttpServletRequest request) throws NotFoundException{			
		User user = SecurityHelper.getUser();
		long objectId = user.getUserId();		
		if( objectType == 1 ){
			objectId = user.getCompanyId();			
		}else if ( objectType == 30){
			objectId = WebSiteUtils.getWebSite(request).getWebSiteId();
		}				
		PhotoList list = new PhotoList();
		
		if(objectType > 0 && objectId == 0)
			list.setTotalCount( photoStreamsManager.getPhotoCount(objectType ));
		else if (objectType > 0 && objectId > 0)
			list.setTotalCount( photoStreamsManager.getPhotoCount(objectType, objectId));		
		else
			list.setTotalCount(photoStreamsManager.getTotalPhotoCount());

		if(objectType > 0 && objectId == 0)
			list.setPhotos(photoStreamsManager.getPhotos(objectType, startIndex, pageSize));
		else if (objectType > 0 && objectId > 0)
			list.setPhotos(photoStreamsManager.getPhotos(objectType, objectId, startIndex, pageSize));
		else
			list.setPhotos(photoStreamsManager.getPhotos(startIndex, pageSize));
		
		return list;
	}
	
	
	@RequestMapping(value="/images/upload_by_url.json", method=RequestMethod.POST)
	@ResponseBody
	public Image  uploadImageByUrl(@RequestBody UrlImageUploader uploader, NativeWebRequest request ) throws NotFoundException, IOException {		
		
		User user = SecurityHelper.getUser();
		int objectType = uploader.getObjectType();
		long objectId = user.getUserId();		
		if( objectType == 1 ){
			objectId = user.getCompanyId();			
		}else if ( objectType == 30){
			objectId = WebSiteUtils.getWebSite(request.getNativeRequest(HttpServletRequest.class)).getWebSiteId();
		}	
		
		
		Image imageToUse = imageManager.createImage(objectType, objectId,  uploader.getFileName(),  uploader.getContentType(),  uploader.readFileFromUrl());
		imageToUse.getProperties().put("source", uploader.getSourceUrl().toString());
		imageToUse.getProperties().put("url", uploader.getSourceUrl().toString());		
		log.debug(imageToUse);
		return  imageManager.saveImage(imageToUse);
	}
	
	public static class UrlImageUploader {
		
		 private int objectType = 2;
		 
		 private URL sourceUrl ;		 
		 
		 private URL imageUrl ;
		 
		 @JsonIgnore
		 private String contentType;
		/**
		 * @return sourceUrl
		 */
		 
		 
		public URL getSourceUrl() {
			return sourceUrl;
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
		 * @param sourceUrl 설정할 sourceUrl
		 */
		public void setSourceUrl(URL sourceUrl) {
			this.sourceUrl = sourceUrl;
		}

		/**
		 * @return imageUrl
		 */
		public URL getImageUrl() {
			return imageUrl;
		}

		/**
		 * @param imageUrl 설정할 imageUrl
		 */
		public void setImageUrl(URL imageUrl) {
			this.imageUrl = imageUrl;
		}
				
		public String getContentType(){			
			if(contentType == null){
				Tika tika = new Tika();
				try {
					contentType = tika.detect(imageUrl);
				} catch (IOException e) {
					contentType = null;
				}
			}
			return contentType;
		}		
		
		public String getFileName(){
			return FilenameUtils.getName(imageUrl.getFile());
		}
		
		public File readFileFromUrl() throws IOException{		
			File temp = File.createTempFile(UUID.randomUUID().toString(), ".tmp");
			temp.deleteOnExit();
			FileUtils.copyURLToFile(imageUrl, temp);			
			return temp;
		}
	}	
	
	public static class PhotoList {
		
		private List<Photo> photos ;
		private int totalCount ;


		/**
		 * @return photos
		 */
		public List<Photo> getPhotos() {
			return photos;
		}
		/**
		 * @param photos 설정할 photos
		 */
		public void setPhotos(List<Photo> photos) {
			this.photos = photos;
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

	
	@Secured({"ROLE_USER"})
	@RequestMapping(value="/announce/update.json", method=RequestMethod.POST)
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
	@RequestMapping(value="/announce/delete.json", method=RequestMethod.POST)
	@ResponseBody
	public Boolean destoryAnnounce(@RequestParam(value="announceId", defaultValue="0", required=true ) Long announceId, NativeWebRequest request){
		User user = SecurityHelper.getUser();
			
		
		return true;
	}
	
	@Secured({"ROLE_ANONYMOUS"})
	@RequestMapping(value="/announce/list.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public AnnounceList  getAnnounceList (
		@RequestParam(value="objectType", defaultValue="30", required=false ) Integer objectType, 
		@RequestParam(value="objectId", defaultValue="0", required=false  ) Long objectId, 
		@RequestParam(value="startIndex", defaultValue="0", required=false ) Integer startIndex,
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
