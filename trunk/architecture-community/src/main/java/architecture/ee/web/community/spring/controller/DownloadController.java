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

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import architecture.ee.web.community.profile.ProfileImage;
import architecture.ee.web.community.profile.ProfileManager;

@Controller 
public class DownloadController {

	private static final Log log = LogFactory.getLog(DownloadController.class);
	
	ProfileManager profileManager ;
		
	@Autowired private ServletContext servletContext;
		
	
	/**
	 * @return profileManager
	 */
	public ProfileManager getProfileManager() {
		return profileManager;
	}

	/**
	 * @param profileManager 설정할 profileManager
	 */
	public void setProfileManager(ProfileManager profileManager) {
		this.profileManager = profileManager;
	}
	
	@RequestMapping(value = "/profile/{userName}", method = RequestMethod.GET)
	@ResponseBody
	public void handleProfile(@PathVariable("userName") String userName, @RequestParam(value="width", defaultValue="0", required=false ) Integer width, @RequestParam(value="height", defaultValue="0", required=false ) Integer height, HttpServletResponse response )throws IOException {
		
		
		log.debug(" ------------------------------------------");
		log.debug("userName:" + userName);
		log.debug("width:"+ width);
		log.debug("height:" + height);
		log.debug("------------------------------------------");			
		
		try {
			
			ProfileImage image = profileManager.getProfileImageByUsername(userName);			
			
			log.debug( "using profile image : " + image.getFilename()  );
			
			InputStream input ;
			String contentType ;
			int contentLength ;			
			if( width > 0 && width > 0 )			{
				input = profileManager.getImageThumbnailInputStream(image, width, height);
				contentType = image.getThumbnailContentType();
				contentLength = image.getThumbnailSize();
			}else{
				input = profileManager.getImageInputStream(image);
				contentType = image.getImageContentType();
				contentLength = image.getImageSize();				
			}		
			
			response.setContentType(contentType);
			response.setContentLength(contentLength);			
			IOUtils.copy(input, response.getOutputStream());
			response.flushBuffer();		
		} catch (Exception e) {
			response.setStatus(301);
			response.addHeader("Location", "/images/common/anonymous.png");
		}	
	}
	
	
	
}
