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

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import architecture.ee.exception.NotFoundException;
import architecture.ee.web.attachment.Image;
import architecture.ee.web.attachment.ImageManager;

@Controller 
@RequestMapping("/download")
public class DownloadController {

	private static final Log log = LogFactory.getLog(DownloadController.class);
	
	ImageManager imageManager ;
	
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

	@RequestMapping(value = "/image/{fileName}", method = RequestMethod.GET)
	@ResponseBody
	public void handleImage(@PathVariable("fileName") String fileName, @RequestParam(value="width") int width, @RequestParam(value="height") int height, HttpServletResponse response )throws IOException {
		log.debug("download ------------------------------------------");
		log.debug("fileName:" + fileName);
		log.debug("width:"+ width);
		log.debug("height:" + height);
		log.debug("------------------------------------------");			
		try {

			
			Image image = imageManager.getImage(1);			
			InputStream input = imageManager.getImageInputStream(image);
			response.setContentType(image.getContentType());
			response.setContentLength(image.getSize());			
			IOUtils.copy(input, response.getOutputStream());
			response.flushBuffer();			
		
		} catch (NotFoundException e) {
			response.sendError(404);
		}		
	}
	
}
