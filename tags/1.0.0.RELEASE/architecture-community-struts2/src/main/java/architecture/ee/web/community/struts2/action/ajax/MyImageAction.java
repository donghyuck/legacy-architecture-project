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
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;

import architecture.common.user.UserManager;
import architecture.ee.exception.NotFoundException;
import architecture.ee.web.attachment.Image;
import architecture.ee.web.attachment.ImageManager;
import architecture.ee.web.attachment.impl.ImageImpl;
import architecture.ee.web.struts2.action.UploadImageAction;
import architecture.ee.web.util.ParamUtils;
import architecture.ee.web.ws.Property;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

public class MyImageAction extends UploadImageAction  {

	private static final Integer USER_OBJEDT_TYPE = 2 ;
	
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
		return getImageManager().getTotalImageCount(USER_OBJEDT_TYPE, getUser().getUserId());
	}
	
	public List<Image> getTargetImages(){    	
    	if( getUser().getUserId() < 1 )
    		return Collections.EMPTY_LIST;    	
        if( pageSize > 0 ){
            return getImageManager().getImages(USER_OBJEDT_TYPE, getUser().getUserId(), startIndex, pageSize);            
        }else{            
            return getImageManager().getImages(USER_OBJEDT_TYPE, getUser().getUserId());
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
	
	protected void extractMetadata( Image image , File file){		
		// jpeg
		try {
			Metadata metadata ;
			if( "image/jpeg".equalsIgnoreCase(image.getContentType())){
				metadata = JpegMetadataReader.readMetadata(file);
			}else{
				metadata = ImageMetadataReader.readMetadata(file);
			}			
			for (Directory directory :  metadata.getDirectories() ){
				for( Tag tag : directory.getTags()	){
					log.debug( "Tag(" +"\n" +
					tag.getTagName() + "\n" +
					tag.getDescription() + "\n" +
					tag.getDirectoryName() + "\n" +
					tag.getTagType() + "\n" +
					tag.getTagTypeHex() + ")" 
					);
				}
			}
		} catch (Exception e) {
			log.warn(e);
		}			
	}

	public String uploadByUrl() throws Exception {
		
		ImageUploadForm form = ParamUtils.getJsonParameter(request, "item", ImageUploadForm.class);
		Image imageToUse; 
		
		File fileToUse = form.readFileFromUrl();
		imageToUse = getImageManager().createImage( USER_OBJEDT_TYPE, getUser().getUserId(),  form.getFileName(),  form.getContentType(),  fileToUse);
		imageToUse.getProperties().put("source", form.getSourceUrl().toString());
		imageToUse.getProperties().put("url", form.getSourceUrl().toString());
		
		log.debug(imageToUse);
		this.imageId = getImageManager().saveImage(imageToUse).getImageId();
		// need checking file url and source .. in image properites ...
		return success();
	}
	
	public String updateImage() throws Exception {		
		if(isMultiPart() ){
			Image imageToUse;
			if( this.imageId < 0  ){	
				File fileToUse = getUploadImage();							
				imageToUse = getImageManager().createImage( USER_OBJEDT_TYPE, getUser().getUserId(),  getUploadImageFileName(),  getUploadImageContentType(),  fileToUse);					
				extractMetadata( imageToUse, fileToUse);				
				this.imageId = getImageManager().saveImage(imageToUse).getImageId();					
			}else{
				imageToUse = getTargetImage();
				File fileToUse = getUploadImage();
				extractMetadata( imageToUse, fileToUse);				
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

	 public static class ImageUploadForm {
		 
		 private URL sourceUrl ;
		 
		 private URL imageUrl ;
		
		 private String contentType;
		 
		 

		/**
		 * @return sourceUrl
		 */
		public URL getSourceUrl() {
			return sourceUrl;
		}

		/**
		 * @param sourceUrl 설정할 sourceUrl
		 */
		public void setSourceUrl(URL sourceUrl) {
			this.sourceUrl = sourceUrl;
		}

		/**
		 * @return imageUrl
		 */
		public URL getImageUrl() {
			return imageUrl;
		}

		/**
		 * @param imageUrl 설정할 imageUrl
		 */
		public void setImageUrl(URL imageUrl) {
			this.imageUrl = imageUrl;
		}
				
		public String getContentType(){			
			if(contentType == null){
				Tika tika = new Tika();
				try {
					contentType = tika.detect(imageUrl);
				} catch (IOException e) {
					contentType = null;
				}
			}
			return contentType;
		}		
		
		public String getFileName(){
			return FilenameUtils.getName(imageUrl.getFile());
		}
		
		public File readFileFromUrl() throws IOException{		
			File temp = File.createTempFile(UUID.randomUUID().toString(), ".tmp");
			temp.deleteOnExit();
			FileUtils.copyURLToFile(imageUrl, temp);			
			return temp;
		}
	 }
}