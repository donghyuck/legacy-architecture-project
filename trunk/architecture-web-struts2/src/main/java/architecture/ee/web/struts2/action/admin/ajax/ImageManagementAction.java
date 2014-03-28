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
package architecture.ee.web.struts2.action.admin.ajax;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import architecture.common.user.UserManager;
import architecture.ee.exception.NotFoundException;
import architecture.ee.web.attachment.Attachment;
import architecture.ee.web.attachment.Image;
import architecture.ee.web.attachment.ImageManager;
import architecture.ee.web.attachment.impl.ImageImpl;
import architecture.ee.web.struts2.action.UploadImageAction;
import architecture.ee.web.util.ParamUtils;
import architecture.ee.web.ws.Property;

public class ImageManagementAction extends UploadImageAction  {

	private int pageSize = 0 ;
    
	private int startIndex = 0 ;  
    
	private Long imageId = -1L; 
		
	private UserManager userManager ;
	
	private Image targetImage;
	
	private Integer objectType = 0;
	
	private Long objectId = -1L;
	
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
		if( objectType < 0 || objectId < 0)
			return 0;
		
		return getImageManager().getTotalImageCount(objectType, objectId);
	}
	
	public List<Image> getTargetImages(){    	
    	if( objectType < 0 || objectId <0 )
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
	

	public List<Property> getTargetImageProperty() {
		Map<String, String> properties = getTargetImage().getProperties();
		List<Property> list = new ArrayList<Property>();
		for (String key : properties.keySet()) {
			String value = properties.get(key);
			list.add(new Property(key, value));
		}
		return list;
	}
	
	public String updateImageProperties() throws Exception {		
		Image group = getTargetImage();
		Map<String, String> properties = group.getProperties();
		List<Map> list = ParamUtils.getJsonParameter(request, "items", List.class);		
		for (Map row : list) {
			String n = (String) row.get("name");
			String v = (String) row.get("value");
			properties.put(n, v);
		}		
		updateTargetImageProperties(group, group.getProperties());
		return success();	
	}
	
	public String deleteImageProperties() throws Exception {
		Image img = getTargetImage();
		Map<String, String> properties = img.getProperties();
		List<Map> list = ParamUtils.getJsonParameter(request, "items", List.class);
		for (Map row : list) {
			String n = (String) row.get("name");
			String v = (String) row.get("value");
			properties.remove(n);
		}
		updateTargetImageProperties( img, properties );		
		return success();
	}

	protected void updateTargetImageProperties(Image image, Map<String, String> properties) {
		if (properties.size() > 0) {
			image.setProperties(properties);
			this.targetImage = image;			
			imageManager.updateImageProperties(targetImage);
			
		}
	}
}
