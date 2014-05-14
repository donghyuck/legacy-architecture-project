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
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import architecture.common.user.Company;
import architecture.common.user.CompanyManager;
import architecture.ee.web.community.profile.ProfileImage;
import architecture.ee.web.community.profile.ProfileManager;
import architecture.ee.web.logo.LogoImage;
import architecture.ee.web.logo.LogoManager;
import architecture.ee.web.site.WebSite;
import architecture.ee.web.site.WebSiteManager;

@Controller 
public class DownloadController {

	private static final Log log = LogFactory.getLog(DownloadController.class);
	
	ProfileManager profileManager ;
	
	LogoManager logoManager;
	
	CompanyManager companyManager;
	
	WebSiteManager webSiteManager;
	
			
	@Autowired private ServletContext servletContext;
		
	
	
	
	/**
	 * @return companyManager
	 */
	public CompanyManager getCompanyManager() {
		return companyManager;
	}

	/**
	 * @param companyManager 설정할 companyManager
	 */
	public void setCompanyManager(CompanyManager companyManager) {
		this.companyManager = companyManager;
	}

	/**
	 * @return webSiteManager
	 */
	public WebSiteManager getWebSiteManager() {
		return webSiteManager;
	}

	/**
	 * @param webSiteManager 설정할 webSiteManager
	 */
	public void setWebSiteManager(WebSiteManager webSiteManager) {
		this.webSiteManager = webSiteManager;
	}

	/**
	 * @return logoManager
	 */
	public LogoManager getLogoManager() {
		return logoManager;
	}

	/**
	 * @param logoManager 설정할 logoManager
	 */
	public void setLogoManager(LogoManager logoManager) {
		this.logoManager = logoManager;
	}

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
	
	@RequestMapping(value = "/logo/{type}/{name}", method = RequestMethod.GET)
	@ResponseBody
	public void handleLogo( @PathVariable("type") String type, @PathVariable("name") String name , HttpServletResponse response )throws IOException {
		
		try {
			LogoImage image = null;
			if( StringUtils.equals(type, "company")){
				Company company = companyManager.getCompany(name);
				image = logoManager.getPrimaryLogoImage(company);			
			}else 	if( StringUtils.equals(type, "site")){
				WebSite site = webSiteManager.getWebSiteByName(name);
				image = logoManager.getPrimaryLogoImage(site);			
			}			
			if( image != null ){
				InputStream input = logoManager.getImageInputStream(image);
				String contentType = image.getImageContentType();
				int contentLength = image.getImageSize();
				response.setContentType(contentType);
				response.setContentLength(contentLength);			
				IOUtils.copy(input, response.getOutputStream());
				response.flushBuffer();			
			}
			
		} catch (Exception e) {
			log.warn(e);
			response.setStatus(301);
			response.addHeader("Location", "/images/common/what-to-know-before-getting-logo-design.png");
		}		
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
