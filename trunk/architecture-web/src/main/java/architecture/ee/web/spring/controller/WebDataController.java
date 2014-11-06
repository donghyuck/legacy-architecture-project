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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;

import architecture.common.user.SecurityHelper;
import architecture.common.user.User;
import architecture.ee.exception.NotFoundException;
import architecture.ee.web.attachment.Attachment;
import architecture.ee.web.attachment.AttachmentManager;
import architecture.ee.web.attachment.Image;
import architecture.ee.web.attachment.ImageManager;
import architecture.ee.web.util.ParamUtils;
import architecture.ee.web.ws.Property;

@Controller ("webDataController")
@RequestMapping("/data")
public class WebDataController {

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

	@RequestMapping(value="/image/{imageId}", method=RequestMethod.GET)
	@ResponseBody
	public Image  getImage(@PathVariable Long imageId, NativeWebRequest request ) throws NotFoundException {		
		User user = SecurityHelper.getUser();
		
		
		return imageManager.getImage(imageId);
	}
	
	@RequestMapping(value="/image/{imageId}/property", method=RequestMethod.GET)
	@ResponseBody
	public List<Property>  getImageProperty(@PathVariable Long imageId, NativeWebRequest request ) throws NotFoundException {		
		User user = SecurityHelper.getUser();
		
		Image image = imageManager.getImage(imageId);
		Map<String, String> properties = image.getProperties();
		return toList(properties);
	}
		
	@RequestMapping(value="/image/{imageId}/property", method=RequestMethod.POST)
	@ResponseBody
	public List<Property>  updateImageProperty(@PathVariable Long imageId, NativeWebRequest request ) throws NotFoundException {		
		User user = SecurityHelper.getUser();
		
		Image image = imageManager.getImage(imageId);
		Map<String, String> properties = image.getProperties();		
		// update or create
		List<Map> list = ParamUtils.getJsonParameter(request.getNativeRequest(HttpServletRequest.class), "items", List.class);		
		for (Map row : list) {
			String name = (String) row.get("name");
			String value = (String) row.get("value");
			properties.put(name, value);
		}		
		if( list.size() > 0 )
			imageManager.updateImageProperties(image);		
		return toList(properties);
	}

	@RequestMapping(value="/image/{imageId}/property", method=RequestMethod.DELETE)
	@ResponseBody
	public List<Property>  deleteImageProperty(@PathVariable Long imageId, NativeWebRequest request ) throws NotFoundException {		
		User user = SecurityHelper.getUser();
		
		Image image = imageManager.getImage(imageId);
		Map<String, String> properties = image.getProperties();		
		List<Map> list = ParamUtils.getJsonParameter(request.getNativeRequest(HttpServletRequest.class), "items", List.class);		
		for (Map row : list) {
			String name = (String) row.get("name");
			properties.remove(name);
		}
		if( list.size() > 0 )
			imageManager.updateImageProperties(image);

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
	
	
	
	
	@RequestMapping(value="/file/{fileId}", method=RequestMethod.GET)
	@ResponseBody
	public Attachment  getFile(@PathVariable Long fileId, NativeWebRequest request ) throws NotFoundException {		
		User user = SecurityHelper.getUser();
		return attachmentManager.getAttachment(fileId);
	}
	
	@RequestMapping(value="/file/{fileId}/property", method=RequestMethod.GET)
	@ResponseBody
	public List<Property>  getFileProperty(@PathVariable Long fileId, NativeWebRequest request ) throws NotFoundException {		
		User user = SecurityHelper.getUser();
		
		Attachment attachment = attachmentManager.getAttachment(fileId);
		Map<String, String> properties = attachment.getProperties();
		return toList(properties);
	}
	
	@RequestMapping(value="/file/{fileId}/property", method=RequestMethod.POST)
	@ResponseBody
	public List<Property>  updateFileProperty(@PathVariable Long fileId, NativeWebRequest request ) throws NotFoundException {		
		User user = SecurityHelper.getUser();
		
		Attachment attachment = attachmentManager.getAttachment(fileId);
		Map<String, String> properties = attachment.getProperties();		
		// update or create
		List<Map> list = ParamUtils.getJsonParameter(request.getNativeRequest(HttpServletRequest.class), "items", List.class);		
		for (Map row : list) {
			String name = (String) row.get("name");
			String value = (String) row.get("value");
			properties.put(name, value);
		}		
		if( list.size() > 0 )
			attachmentManager.saveAttachment(attachment);
		return toList(properties);
	}

	@RequestMapping(value="/file/{fileId}/property", method=RequestMethod.DELETE)
	@ResponseBody
	public List<Property>  deleteFileProperty(@PathVariable Long fileId, NativeWebRequest request ) throws NotFoundException {		
		User user = SecurityHelper.getUser();
		
		Attachment attachment = attachmentManager.getAttachment(fileId);
		Map<String, String> properties = attachment.getProperties();		
		List<Map> list = ParamUtils.getJsonParameter(request.getNativeRequest(HttpServletRequest.class), "items", List.class);		
		for (Map row : list) {
			String name = (String) row.get("name");
			properties.remove(name);
		}
		if( list.size() > 0 )
			attachmentManager.saveAttachment(attachment);
		return toList(properties);
	}	
}
