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

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import architecture.common.user.CompanyManager;
import architecture.ee.util.ApplicationHelper;
import architecture.ee.web.attachment.AttachmentManager;
import architecture.ee.web.attachment.ImageManager;
import architecture.ee.web.logo.LogoImage;
import architecture.ee.web.logo.LogoManager;
import architecture.ee.web.site.WebSiteManager;

@Controller ("secure-web-download-controller")
@RequestMapping("/secure/download")
public class SecureDownloadController {

	private static final Log log = LogFactory.getLog(SecureDownloadController.class);
	
	@Inject
	@Qualifier("imageManager")
	private ImageManager imageManager ;
	
	@Inject
	@Qualifier("attachmentManager")
	private AttachmentManager attachmentManager;
	
	@Inject
	@Qualifier("logoManager")
	private LogoManager logoManager;
	/**
	 * @return attachmentManager
	 */
	@Inject
	@Qualifier("companyManager")
	private CompanyManager companyManager;
	
	@Inject 
	@Qualifier("webSiteManager")
	private WebSiteManager webSiteManager;
	
	
	@RequestMapping(value = "/logo/{logoId}", method = RequestMethod.GET)
	@ResponseBody
	public void handleLogo( 
		@PathVariable("logoId") Long logoId,
		@RequestParam(value="width", defaultValue="0", required=false ) Integer width, 
		@RequestParam(value="height", defaultValue="0", required=false ) Integer height, 
		HttpServletResponse response )throws IOException {
		
		try {
			LogoImage image = logoManager.getLogoImageById(logoId);	
	
			if( image != null ){
				InputStream input ;
				String contentType ;
				int contentLength ;				
				if( width > 0 && width > 0 )			{
					input = logoManager.getImageThumbnailInputStream(image, width, height);
					contentType = image.getThumbnailContentType();
					contentLength = image.getThumbnailSize();
				}else{
					input = logoManager.getImageInputStream(image);
					contentType = image.getImageContentType();
					contentLength = image.getImageSize();
				}				
				response.setContentType(contentType);
				response.setContentLength(contentLength);			
				IOUtils.copy(input, response.getOutputStream());
				response.flushBuffer();			
			}
			
		} catch (Exception e) {
			log.warn(e);
			response.setStatus(301);
			String url = ApplicationHelper.getApplicationProperty("components.download.images.no-logo-url", "/images/common/what-to-know-before-getting-logo-design.png");
			response.addHeader("Location", url );
		}		
	}
		
	
}
