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

import java.io.File;
import java.io.FileInputStream;
import java.util.Collections;
import java.util.List;

import architecture.common.user.UserManager;
import architecture.ee.exception.NotFoundException;
import architecture.ee.web.attachment.Image;
import architecture.ee.web.attachment.ImageManager;
import architecture.ee.web.attachment.impl.ImageImpl;
import architecture.ee.web.struts2.action.UploadImageAction;

public class MyImageAction extends UploadImageAction  {

	private int DEFAULT_OBJEDT_TYPE = 2 ;
	
    private int pageSize = 0 ;
    
    private int startIndex = 0 ;  
    
	private Long imageId = -1L; 
		
	private UserManager userManager ;
	
	private Image targetImage;
	
	private ImageManager imageManager;
	
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
	 * @return pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize 설정할 pageSize
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return startIndex
	 */
	public int getStartIndex() {
		return startIndex;
	}

	/**
	 * @param startIndex 설정할 startIndex
	 */
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	
	
	public Long getImageId() {
		return imageId;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
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
	
	public int getTotalTargetImageCount(){
		if( getUser().getUserId() < 1 )
			return 0 ;
		return getImageManager().getTotalImageCount(DEFAULT_OBJEDT_TYPE, getUser().getUserId());
	}
	
	public List<Image> getTargetImages(){    	
    	if( getUser().getUserId() < 1 )
    		return Collections.EMPTY_LIST;
    	
        if( pageSize > 0 ){
            return getImageManager().getImages(DEFAULT_OBJEDT_TYPE, getUser().getUserId(), startIndex, pageSize);            
        }else{            
            return getImageManager().getImages(DEFAULT_OBJEDT_TYPE, getUser().getUserId());
        }
    }
	
	public String execute() throws Exception {
		return success();
	}
	
	public String deleteImage() throws Exception {  		
		Image image = getTargetImage();
		if( image.getImageId() > 0)
		{
			
			
		}
		
        return success();
    }  
	
	
	
	public String updateImage() throws Exception {		
		
		if(isMultiPart() ){
			Image imageToUse;
			if( this.imageId < 0  ){	
				File fileToUse = getUploadImage();			
				imageToUse = getImageManager().createImage(
					DEFAULT_OBJEDT_TYPE, 
					getUser().getUserId(), 
					getUploadImageFileName(), 
					getUploadImageContentType(), 
					fileToUse);	
				this.imageId = getImageManager().saveImage(imageToUse).getImageId();	
			}else{
				imageToUse = getTargetImage();
				File fileToUse = getUploadImage();			
				((ImageImpl)imageToUse).setSize( (int)fileToUse.length());
				((ImageImpl)imageToUse).setInputStream( new FileInputStream(fileToUse));
				log.debug("image size:" + imageToUse.getSize());
				log.debug("image stream:" + imageToUse.getInputStream());
				getImageManager().saveImage(imageToUse);
			}		
		}
		return success();
	}
}
