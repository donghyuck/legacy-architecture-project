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

import architecture.ee.exception.NotFoundException;
import architecture.ee.web.attachment.Image;
import architecture.ee.web.attachment.ImageManager;
import architecture.ee.web.attachment.impl.ImageImpl;
import architecture.ee.web.struts2.action.UploadImageAction;

public class MyImageBrowserAction extends UploadImageAction {

	private int pageSize = 0 ;
    
	private int startIndex = 0 ;  
    
	private Long imageId = -1L; 
			
	private Image targetImage;
	
	private Integer objectType = 0;
	
	private Long objectId = -1L;
	
	private ImageManager imageManager;

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
	 * @param targetImage 설정할 targetImage
	 */
	public void setTargetImage(Image targetImage) {
		this.targetImage = targetImage;
	}

	/**
	 * @return objectType
	 */
	public Integer getObjectType() {
		return objectType;
	}

	/**
	 * @param objectType 설정할 objectType
	 */
	public void setObjectType(Integer objectType) {
		this.objectType = objectType;
	}

	/**
	 * @return objectId
	 */
	public Long getObjectId() {
		return objectId;
	}

	/**
	 * @param objectId 설정할 objectId
	 */
	public void setObjectId(Long objectId) {
		this.objectId = objectId;
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
		if( objectType < 0 || objectId < 0)
			return 0;
		
		return getImageManager().getTotalImageCount(objectType, objectId);
	}
	
	public List<Image> getTargetImages(){    	
    	if( this.objectType < 0 || this.objectId <0 )
    		return Collections.EMPTY_LIST;    	
    	
    	log.debug(  request.getParameterMap() );
    	log.debug( "startIndex= " + startIndex + ", pageSize=" + pageSize  );

        if( pageSize > 0 ){
            return getImageManager().getImages(objectType, objectId, startIndex, pageSize);            
        }else{            
            return getImageManager().getImages(objectType, objectId);
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
					objectType, 
					objectId, 
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
