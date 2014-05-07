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

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import architecture.common.user.authentication.UnAuthorizedException;
import architecture.common.util.StringUtils;
import architecture.ee.exception.NotFoundException;
import architecture.ee.web.attachment.Image;
import architecture.ee.web.attachment.ImageManager;
import architecture.ee.web.community.streams.Photo;
import architecture.ee.web.community.streams.PhotoStreamsManager;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;

import com.opensymphony.xwork2.Preparable;

public class MyPhotoStreamsAction extends FrameworkActionSupport  implements Preparable  {

	private PhotoStreamsManager photoStreamsManager ;

    private int pageSize = 25 ;
    
    private int startIndex = 0 ;  
    
    private Long imageId = -1L; 
    
    private String key ;
    
    private int objectType = 0 ;
    
    private long objectId = 0 ;
    	
	private ImageManager imageManager ;
	
	private Photo targetPhoto ;
	
	private Image targetImage ;
	
	/**
	 * @return objectType
	 */
	public int getObjectType() {
		return objectType;
	}



	/**
	 * @param objectType 설정할 objectType
	 */
	public void setObjectType(int objectType) {
		this.objectType = objectType;
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
	 * @return photoStreamsManager
	 */
	public PhotoStreamsManager getPhotoStreamsManager() {
		return photoStreamsManager;
	}



	/**
	 * @param photoStreamsManager 설정할 photoStreamsManager
	 */
	public void setPhotoStreamsManager(PhotoStreamsManager photoStreamsManager) {
		this.photoStreamsManager = photoStreamsManager;
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
	 * @return key
	 */
	public String getKey() {
		return key;
	}



	/**
	 * @return targetPhoto
	 */
	public Photo getTargetPhoto() {
		return targetPhoto;
	}



	/**
	 * @param key 설정할 key
	 */
	public void setKey(String key) {
		this.key = key;
	}


	public void prepare() throws Exception {
		
	}
	
	
	public String execute() throws Exception {
		return success();
    }

	public String viewPhoto() throws Exception {
		if( StringUtils.isNotEmpty( key ) ){
			this.targetPhoto = this.photoStreamsManager.getPhotoById(key);
			this.targetImage = this.imageManager.getImage(targetPhoto.getImageId());
		}
		return success();
    }
	
	public String addPhoto() throws Exception {
		return success();
    }
	
	public String removePhoto() throws Exception {
		return success();
    }
	
	public int getPhotoCount(){
		if(objectType > 0 && objectId == 0)
			return getPhotoStreamsManager().getPhotoCount(objectType);
		else if (objectType > 0 && objectId > 0)
			return getPhotoStreamsManager().getPhotoCount(objectType, objectId);
		
		return getPhotoStreamsManager().getTotalPhotoCount();
	}
	
	public List<Photo> getPhotos(){		
		if( imageId > 0 ){
			if( getTargetImage() != null )
				return getPhotoStreamsManager().getPhotosByImage(getTargetImage());
			return Collections.EMPTY_LIST;
		}		
		if(objectType > 0 && objectId == 0)
			return getPhotoStreamsManager().getPhotos(objectType, this.startIndex, this.pageSize);
		else if (objectType > 0 && objectId > 0)
			return getPhotoStreamsManager().getPhotos(objectType, objectId, this.startIndex, this.pageSize);			
		return getPhotoStreamsManager().getPhotos(this.startIndex, this.pageSize);
	}
		
	
	/**
	 * @return targetImage
	 */
	public Image getTargetImage() {
		if(targetImage == null && imageId > 0){
			try {
				this.targetImage = this.imageManager.getImage(imageId);
			} catch (NotFoundException e) {
				log.warn(e);
			}
		}
		return targetImage;
	}

	public InputStream getTargetImageInputStream() {
		Image imageToUse = getTargetImage();		
		return imageManager.getImageInputStream(imageToUse);
	}
	
	public String getTargetImageContentType(){
		return getTargetImage().getContentType();
	}

	public String getTargetImageFileName(){
		return getTargetImage().getName();
	}	
	
	public int getTargetImageContentLength(){
		return getTargetImage().getSize();	
	}
	
	public String addImageToStreams() throws Exception {
		if(getUser().isAnonymous())
			throw new UnAuthorizedException();
		
		Image imageToUse = this.getTargetImage();
		getPhotoStreamsManager().addImage(imageToUse, getUser());
		return success();
    }
	
	public String removeImageFromStreams() throws Exception {
		if(getUser().isAnonymous())
			throw new UnAuthorizedException();
						
		Image imageToUse = this.getTargetImage();
		getPhotoStreamsManager().deletePhotos(imageToUse, getUser());
		
		return success();
    }
}
