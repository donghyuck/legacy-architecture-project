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

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import architecture.ee.exception.NotFoundException;
import architecture.ee.web.attachment.Attachment;
import architecture.ee.web.attachment.AttachmentManager;
import architecture.ee.web.attachment.Image;
import architecture.ee.web.attachment.ImageManager;

@Controller 
public class DownloadController {

	private static final Log log = LogFactory.getLog(DownloadController.class);
	
	private ImageManager imageManager ;
	
	private AttachmentManager attachmentManager;
	
	
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
	
	@RequestMapping(value = "/file/{attachmentId}/{filename:.+}", method = RequestMethod.GET)
	@ResponseBody
	public void handleFile( @PathVariable("attachmentId") Long attachmentId, @PathVariable("filename") String filename , HttpServletResponse response )throws IOException {
		log.debug(" ------------------------------------------");
		log.debug("attachment:" + attachmentId);
		log.debug("filename:"+ filename);
		log.debug("------------------------------------------");
		try {
			if( attachmentId > 0 && StringUtils.isNotEmpty(filename)){
				Attachment attachment = attachmentManager.getAttachment(attachmentId);
				if( StringUtils.equals(filename, attachment.getName() ) ){
					InputStream input = attachmentManager.getAttachmentInputStream(attachment);
					String contentType  = attachment.getContentType();
					int contentLength = attachment.getSize();
					
					response.setContentType(contentType);
					response.setContentLength(contentLength);			
					response.setHeader("contentDisposition", "attachment;filename=" + getEncodedFileName(attachment) );
					IOUtils.copy(input, response.getOutputStream());					
					response.flushBuffer();					
				}else{
					throw new NotFoundException();
				}
			}else{
				throw new NotFoundException();
			}
		} catch (NotFoundException e) {
			response.sendError(404);
		}		
	}
	
	
	protected String getEncodedFileName(Attachment attachment) {
		try {
			return URLEncoder.encode(attachment.getName(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return attachment.getName();
		}
	}

	@RequestMapping(value = "/image/{fileName}", method = RequestMethod.GET)
	@ResponseBody
	public void handleImage(@PathVariable("fileName") String fileName, @RequestParam(value="width", defaultValue="0", required=false ) Integer width, @RequestParam(value="height", defaultValue="0", required=false ) Integer height, HttpServletResponse response )throws IOException {
		
		
		log.debug(" ------------------------------------------");
		log.debug("fileName:" + fileName);
		log.debug("width:"+ width);
		log.debug("height:" + height);
		log.debug("------------------------------------------");			
		
		try {
			Image image =imageManager.getImageByImageLink(fileName);
			
			InputStream input ;
			String contentType ;
			int contentLength ;
			
			if( width > 0 && width > 0 )			{
				input = imageManager.getImageThumbnailInputStream(image, width, height);
				contentType = image.getThumbnailContentType();
				contentLength = image.getThumbnailSize();
			}else{
				input = imageManager.getImageInputStream(image);
				contentType = image.getContentType();
				contentLength = image.getSize();			
				
			}			
			
			response.setContentType(contentType);
			response.setContentLength(contentLength);			
			IOUtils.copy(input, response.getOutputStream());
			response.flushBuffer();			
		
		} catch (NotFoundException e) {
			response.sendError(404);
		}		
	}
	
}
