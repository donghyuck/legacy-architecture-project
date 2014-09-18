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
package architecture.ee.web.community.struts2.action.ajax;

import architecture.common.user.authentication.UnAuthorizedException;
import architecture.ee.exception.NotFoundException;
import architecture.ee.web.attachment.Image;
import architecture.ee.web.attachment.ImageLink;
import architecture.ee.web.attachment.ImageManager;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;

public class MyImageLinkAction extends FrameworkActionSupport {
	
	private Boolean createIfNotExist = true;
	
	private ImageLink imageLink ;
	
	private Long imageId = -1L;

	private Image targetImage;
	
	private ImageManager imageManager;
	
	
	
	/**
	 * @return createIfNotExist
	 */
	public Boolean getCreateIfNotExist() {
		return createIfNotExist;
	}

	/**
	 * @param createIfNotExist 설정할 createIfNotExist
	 */
	public void setCreateIfNotExist(Boolean createIfNotExist) {
		this.createIfNotExist = createIfNotExist;
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
	 * @return imageId
	 */
	public Long getImageId() {
		return imageId;
	}

	/**
	 * @param imageId 설정할 imageId
	 */
	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

	/**
	 * @return imageLink
	 * @throws NotFoundException 
	 */
	public ImageLink getImageLink() throws NotFoundException {
		if( imageLink == null ){			
			if(isGuest())
				imageLink = getImageManager().getImageLink(getTargetImage());
			else				
				imageLink = getImageManager().getImageLink(getTargetImage(), createIfNotExist);
		}		
		return imageLink;
	}

	/**
	 * @param imageLink 설정할 imageLink
	 */
	public void setImageLink(ImageLink imageLink) {
		this.imageLink = imageLink;
	}
	
	public Image getTargetImage( ){
		try {	
			if( targetImage == null){
				targetImage = getImageManager().getImage(imageId);
			}
			return targetImage;
		} catch (NotFoundException e) {
			log.error(e);
			return null;
		}
	}
	
	public String execute() throws Exception {
		return success();
	}
	
	public String deleteImageLink() throws Exception {  		
		if(isGuest())
			throw new UnAuthorizedException();
		Image image = getTargetImage();
		if( image.getImageId() > 0)
		{
			getImageManager().removeImageLink(image);
		}	
		return success();
	}
}
