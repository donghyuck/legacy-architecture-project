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
package architecture.ee.web.spring.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import architecture.common.model.factory.ModelTypeFactory;
import architecture.common.user.SecurityHelper;
import architecture.common.user.User;
import architecture.ee.exception.NotFoundException;
import architecture.ee.web.attachment.Attachment;
import architecture.ee.web.attachment.AttachmentManager;
import architecture.ee.web.attachment.Image;
import architecture.ee.web.attachment.ImageLink;
import architecture.ee.web.attachment.ImageManager;
import architecture.ee.web.attachment.impl.ImageImpl;
import architecture.ee.web.util.ParamUtils;
import architecture.ee.web.util.WebSiteUtils;
import architecture.ee.web.ws.Property;

@Controller ("webDataController")
@RequestMapping("/data")
public class WebDataController {

	private Log log = LogFactory.getLog(getClass());
	
	@Inject
	@Qualifier("imageManager")
	private ImageManager imageManager ;
	
	@Inject
	@Qualifier("attachmentManager")
	private AttachmentManager attachmentManager;
	
	
	
	
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

	
	@Secured({"ROLE_USER"})
	@RequestMapping(value="/images/list.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public ImageList  getImageList(
			@RequestParam(value="objectType", defaultValue="2", required=false ) Integer objectType,
			@RequestParam(value="startIndex", defaultValue="0", required=false ) Integer startIndex,
			@RequestParam(value="pageSize", defaultValue="0", required=false ) Integer pageSize,
			NativeWebRequest request ) throws NotFoundException {		
		User user = SecurityHelper.getUser();
		
		return getImageList(objectType, startIndex, pageSize, request.getNativeRequest(HttpServletRequest.class));
	}
	
	
	private ImageList getImageList(int objectType, int startIndex, int pageSize, HttpServletRequest request) throws NotFoundException{			
		User user = SecurityHelper.getUser();
		long objectId = user.getUserId();		
		if( objectType == 1 ){
			objectId = user.getCompanyId();			
		}else if ( objectType == 30){
			objectId = WebSiteUtils.getWebSite(request).getWebSiteId();
		}				
		ImageList list = new ImageList();
		list.setTotalCount(imageManager.getTotalImageCount(objectType, objectId));		
		if( pageSize > 0 ){
			list.setImages(imageManager.getImages(objectType, objectId, startIndex, pageSize));
		}else{
			list.setImages(imageManager.getImages(objectType, objectId));
		}
		return list;
	}
	
	public static class ImageList {
		
		private List<Image> images ;
		private int totalCount ;

		/**
		 * @return images
		 */
		public List<Image> getImages() {
			return images;
		}
		/**
		 * @param images 설정할 images
		 */
		public void setImages(List<Image> images) {
			this.images = images;
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
	
	
	private boolean hasPermissions(Image image, User user){		
		if(user.isAnonymous())
			return false;		
		 if (image.getObjectType() == ModelTypeFactory.getTypeIdFromCode("COMPANY") && image.getObjectId() != user.getCompanyId() ){
			 return false;			
		}else if (image.getObjectType() == ModelTypeFactory.getTypeIdFromCode("USER") && image.getObjectId() != user.getUserId()){
			return false;			
		}
		return true;
	}	
	
	@RequestMapping(value="/images/get.json", method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public Image  getImage(@RequestParam(value="imageId", defaultValue="0", required=true ) Long imageId, NativeWebRequest request ) throws NotFoundException {		
		User user = SecurityHelper.getUser();
		return imageManager.getImage(imageId);
	}

	@RequestMapping(value="/images/update_with_media.json", method=RequestMethod.POST)
	@ResponseBody
	public Image  updateImageWithMedia(
			@RequestParam(value="objectType", defaultValue="2", required=false ) Integer objectType,
			@RequestParam(value="imageId", defaultValue="0", required=false ) Long imageId, 
			MultipartHttpServletRequest request) throws NotFoundException, IOException {		
		
		User user = SecurityHelper.getUser();
		Iterator<String> fileName =  request.getFileNames();
		MultipartFile mpf = null;
		while(fileName.hasNext()){			
			mpf = request.getFile(fileName.next()); 
			break;
		}
		
		Image image;
		if( imageId > 0 ){
			image = imageManager.getImage(imageId);
			image.setName(mpf.getOriginalFilename());
			((ImageImpl)image).setSize( (int)mpf.getSize());
			((ImageImpl)image).setInputStream(mpf.getInputStream());
		}else{
			image = imageManager.createImage(objectType, user.getUserId(), mpf.getOriginalFilename(), mpf.getContentType(), mpf.getInputStream());
		}
		log.debug(hasPermissions(image, user));
		return imageManager.saveImage(image);
	}

	@RequestMapping(value="/images/insert.json", method=RequestMethod.POST)
	@ResponseBody
	public Image  updateImage(@RequestBody ImageImpl newImage, NativeWebRequest request ) throws NotFoundException {		
		User user = SecurityHelper.getUser();
		//return imageManager.getImage(imageId);
		
		return null;
	}
	
	@RequestMapping(value="/images/link.json", method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public ImageLink  getImageLink(@RequestParam(value="imageId", defaultValue="0", required=true ) Long imageId, NativeWebRequest request ) throws NotFoundException {	
		User user = SecurityHelper.getUser();
		Image image = imageManager.getImage(imageId);
		if(hasPermissions(image, user)){
			return imageManager.getImageLink(image, true);
		}		
		return imageManager.getImageLink(image);
	}
	
	
	@RequestMapping(value="/images/properties/list.json", method=RequestMethod.GET)
	@ResponseBody
	public List<Property>  getImageProperty(@RequestParam(value="imageId", defaultValue="0", required=true ) Long imageId, NativeWebRequest request ) throws NotFoundException {		
		User user = SecurityHelper.getUser();		
		Image image = imageManager.getImage(imageId);
		Map<String, String> properties = image.getProperties();
		return toList(properties);
	}
		
	@RequestMapping(value="/images/properties/update.json", method=RequestMethod.POST)
	@ResponseBody
	public List<Property>  updateImageProperty(@RequestParam(value="imageId", defaultValue="0", required=true ) Long imageId, @RequestBody List<Property> newProperties, NativeWebRequest request ) throws NotFoundException {		
		User user = SecurityHelper.getUser();		
		Image image = imageManager.getImage(imageId);
		Map<String, String> properties = image.getProperties();		
		// update or create
		for (Property property : newProperties) {
			properties.put(property.getName(), property.getValue().toString());
		}		
		if( newProperties.size() > 0){
			imageManager.updateImageProperties(image);		
		}	
		return toList(properties);
	}

	@RequestMapping(value="/images/properties/delete.json", method={RequestMethod.POST, RequestMethod.DELETE})
	@ResponseBody
	public List<Property>  deleteImageProperty(@RequestParam(value="imageId", defaultValue="0", required=true ) Long imageId, @RequestBody List<Property> newProperties,  NativeWebRequest request ) throws NotFoundException {		
		User user = SecurityHelper.getUser();
		
		Image image = imageManager.getImage(imageId);
		Map<String, String> properties = image.getProperties();	
		log.debug(properties);		
		log.debug(newProperties);		
		for (Property property : newProperties) {
			properties.remove(property.getName());
		}
		if( newProperties.size() > 0){
			log.debug(properties);
			imageManager.updateImageProperties(image);
		}		
		return toList(properties);
	}
	
	
	protected List<Property> toList (Map<String, String> properties){
		List<Property> list = new ArrayList<Property>();
		for (String key : properties.keySet()) {
			String value = properties.get(key);
			list.add(new Property(key, value));
		}
		return list;
	}
	
	protected void updateImageProperties(Image image, Map<String, String> properties) {
		if (properties.size() > 0) {
			image.setProperties(properties);
			imageManager.updateImageProperties(image);			
		}
	}
	
	
	
	
	@RequestMapping(value="/files/get.json}", method={RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Attachment  getFile(@RequestParam(value="fileId", defaultValue="0", required=true ) Long fileId, NativeWebRequest request ) throws NotFoundException {		
		User user = SecurityHelper.getUser();
		return attachmentManager.getAttachment(fileId);
	}
	
	@RequestMapping(value="/files/properties/list.json", method={RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public List<Property>  getFileProperty(@RequestParam(value="fileId", defaultValue="0", required=true ) Long fileId, NativeWebRequest request ) throws NotFoundException {		
		User user = SecurityHelper.getUser();
		
		Attachment attachment = attachmentManager.getAttachment(fileId);
		Map<String, String> properties = attachment.getProperties();
		return toList(properties);
	}
	
	@RequestMapping(value="/files/properties/update.json", method=RequestMethod.POST)
	@ResponseBody
	public List<Property>  updateFileProperty(@RequestParam(value="fileId", defaultValue="0", required=true ) Long fileId, @RequestBody List<Property> newProperties, NativeWebRequest request ) throws NotFoundException {		
		User user = SecurityHelper.getUser();
		
		Attachment attachment = attachmentManager.getAttachment(fileId);
		Map<String, String> properties = attachment.getProperties();		
		// update or create
		for (Property row : newProperties) {
			properties.put(row.getName(), (String)row.getValue());
		}		
		if( newProperties.size() > 0 )
			attachmentManager.saveAttachment(attachment);
		return toList(properties);
	}

	@RequestMapping(value="/files/properties/delete.json", method={RequestMethod.DELETE, RequestMethod.POST })
	@ResponseBody
	public List<Property>  deleteFileProperty(@RequestParam(value="fileId", defaultValue="0", required=true ) Long fileId, @RequestBody List<Property> newProperties, NativeWebRequest request ) throws NotFoundException {		
		User user = SecurityHelper.getUser();
		
		Attachment attachment = attachmentManager.getAttachment(fileId);
		Map<String, String> properties = attachment.getProperties();
		for (Property row: newProperties) {
			properties.remove(row.getName());
		}
		if( newProperties.size() > 0 )
			attachmentManager.saveAttachment(attachment);
		return toList(properties);
	}	
}
