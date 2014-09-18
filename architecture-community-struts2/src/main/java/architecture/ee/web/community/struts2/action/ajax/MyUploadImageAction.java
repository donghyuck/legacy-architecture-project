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
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;

import architecture.ee.exception.NotFoundException;
import architecture.ee.web.attachment.Image;
import architecture.ee.web.attachment.ImageManager;
import architecture.ee.web.attachment.impl.ImageImpl;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;

import com.opensymphony.xwork2.Preparable;

public class MyUploadImageAction extends FrameworkActionSupport  implements Preparable {

	private File uploadImage;

	private String uploadImageContentType;

	private String uploadImageFileName;

	private ImageManager imageManager ;
	
	private Image targetImage;
	
	 private Long imageId = -1L;
	 
	 private Integer objectType = 0;
	 
	 private Long objectId = -1L;
	 
	
	public ImageManager getImageManager() {
		return imageManager;
	}

	public void setImageManager(ImageManager imageManager) {
		this.imageManager = imageManager;
	}

	public File getUploadImage() {
		return uploadImage;
	}

	public void setUploadImage(File uploadImage) {
		this.uploadImage = uploadImage;
	}

	public String getUploadImageContentType() {
		return uploadImageContentType;
	}

	public void setUploadImageContentType(String uploadImageContentType) {
		this.uploadImageContentType = uploadImageContentType;
	}

	public String getUploadImageFileName() {
		return uploadImageFileName;
	}

	public void setUploadImageFileName(String uploadImageFileName) {
		this.uploadImageFileName = uploadImageFileName;
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

	protected boolean isMultiPart() {
		HttpServletRequest request = getRequest();
		if ( request instanceof MultiPartRequestWrapper ) 
			return true;
		else
			return false;		
	}
	
	protected MultiPartRequestWrapper getMultiPartRequestWrapper(){
		HttpServletRequest request = getRequest();
		MultiPartRequestWrapper multiWrapper = (MultiPartRequestWrapper) request;			
		return multiWrapper;		
	}
	
	public void prepare() throws Exception {		
		if ( isMultiPart() ) {			
			MultiPartRequestWrapper multiWrapper = getMultiPartRequestWrapper();		
			Enumeration<String> fileParameterNames = multiWrapper.getFileParameterNames();			
			while (fileParameterNames != null && fileParameterNames.hasMoreElements()) {				
				String inputName = (String) fileParameterNames.nextElement();				
				String[] contentTypes = multiWrapper.getContentTypes(inputName);				
				if (isNonEmpty(contentTypes)) {					
					String[] fileNames = multiWrapper.getFileNames(inputName);
					if (isNonEmpty(fileNames)) {						
						File[] files = multiWrapper.getFiles(inputName);						
						if (files != null && files.length > 0) {
							for (int index = 0; index < files.length; index++) {										
								uploadImage = files[index];
								uploadImageFileName =  fileNames[index];
								uploadImageContentType = contentTypes[index];									
							}
						}
					}
				}				
			}			
		}		
	}
	
	private static boolean isNonEmpty(Object[] objArray) {
		boolean result = false;
		for (int index = 0; index < objArray.length && !result; index++) {
			if (objArray[index] != null) {
				result = true;
			}
		}
		return result;
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
		
		return success();
	}
}
