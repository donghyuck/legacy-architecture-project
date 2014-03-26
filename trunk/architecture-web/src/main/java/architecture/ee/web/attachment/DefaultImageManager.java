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
package architecture.ee.web.attachment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.coobird.thumbnailator.Thumbnails;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.poi.util.IOUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import architecture.common.util.PlatformHelper.Platform;
import architecture.ee.exception.NotFoundException;
import architecture.ee.exception.SystemException;
import architecture.ee.util.ApplicationHelper;
import architecture.ee.web.attachment.dao.ImageDao;
import architecture.ee.web.attachment.impl.ImageImpl;

public class DefaultImageManager extends AbstractAttachmentManager implements ImageManager {

	private Lock lock = new ReentrantLock();
	private ImageConfig imageConfig;
	private ImageDao imageDao;
	private Cache imageCache;
	private File imageDir;	
	
	public ImageConfig getImageConfig() {
		return imageConfig;
	}

	public void setImageConfig(ImageConfig imageConfig) {
		this.imageConfig = imageConfig;
	}

	public ImageDao getImageDao() {
		return imageDao;
	}

	public void setImageDao(ImageDao imageDao) {
		this.imageDao = imageDao;
	}
	
	public boolean isImageEnabled() {
		return imageConfig.isEnabled();
	}

	public void setImageEnabled(boolean enabled) {
		imageConfig.setEnabled(enabled);
		String str = (new StringBuilder()).append("").append(enabled).toString();
		ApplicationHelper.getConfigService().setApplicationProperty("image.enabled", str);
	}

	public int getMaxImageSize() {
		return imageConfig.getMaxImageSize();
	}

	public void setMaxImageSize(int maxImageSize) {
		imageConfig.setMaxImageSize(maxImageSize);
		String str = (new StringBuilder()).append("").append(maxImageSize).toString();
		ApplicationHelper.getConfigService().setApplicationProperty("image.maxImageSize", str);	
	}

	public int getImagePreviewMaxSize() {
		return imageConfig.getImagePreviewMaxSize();
	}

	public void setImagePreviewMaxSize(int imagePreviewMaxSize) {
		imageConfig.setImagePreviewMaxSize(imagePreviewMaxSize);
		String str = (new StringBuilder()).append("").append(imagePreviewMaxSize).toString();
		ApplicationHelper.getConfigService().setApplicationProperty("image.imagePreviewMaxSize", str);		
	}

	public boolean isForceThumbnailsEnabled() {
		return imageConfig.isForceThumbnailsEnabled();
	}

	public void setFourceThumbnailsEnabled(boolean forceThumbnailsEnabled) {
		imageConfig.setForceThumbnailsEnabled(forceThumbnailsEnabled);
		String str = (new StringBuilder()).append("").append(forceThumbnailsEnabled).toString();
		ApplicationHelper.getConfigService().setApplicationProperty("image.forceThumbnailsEnabled", str);		
	}

	public int getImageMaxWidth() {
		return imageConfig.getImageMaxWidth();
	}

	public void setImageMaxWidth(int imageMaxWidth) {
		imageConfig.setImageMaxWidth(imageMaxWidth);
		String str = (new StringBuilder()).append("").append(imageMaxWidth).toString();
		ApplicationHelper.getConfigService().setApplicationProperty("image.imageMaxWidth", str);	
	}

	public int getImageMaxHeight() {
		return imageConfig.getImageMaxHeight();
	}

	public void setImageMaxHeight(int imageMaxHeight) {
		imageConfig.setImageMaxHeight(imageMaxHeight);
		String str = (new StringBuilder()).append("").append(imageMaxHeight).toString();
		ApplicationHelper.getConfigService().setApplicationProperty("image.imageMaxHeight", str);	
	}

	public boolean isValidType(String contentType) {
		boolean flag;		
		 if(isAllowAllByDefault())
             flag = !getDisallowedTypes().contains(contentType);
         else
             flag = getAllowedTypes().contains(contentType);
		 return flag;
	}

	public List<String> getDisallowedTypes(){		
		return imageConfig.getDisallowedTypes();
	}
	
	public void addDisallowedType(String contentType)
	{
		if (!imageConfig.getDisallowedTypes().contains(contentType)){			
			imageConfig.getDisallowedTypes().add(contentType);
			String str = listToString( imageConfig.getDisallowedTypes());
			ApplicationHelper.getConfigService().setApplicationProperty("image.disallowedTypes", str);	
		}	
	}
	
	public void removeDisallowedType(String contentType)
	{
		if (imageConfig.getDisallowedTypes().contains(contentType)){			
			imageConfig.getDisallowedTypes().remove(contentType);
			String str = listToString( imageConfig.getDisallowedTypes());
			ApplicationHelper.getConfigService().setApplicationProperty("image.disallowedTypes", str);	
		}			
	}
	
	public void addAllowedType(String contentType) {
		if (!imageConfig.getAllowedTypes().contains(contentType)){			
			imageConfig.getAllowedTypes().add(contentType);
			String str = listToString( imageConfig.getAllowedTypes());
			ApplicationHelper.getConfigService().setApplicationProperty("image.allowedTypes", str);	
		}		
	}

	public void removeAllowedType(String contentType) {
		if (imageConfig.getAllowedTypes().contains(contentType)){			
			imageConfig.getAllowedTypes().remove(contentType);
			String str = listToString( imageConfig.getAllowedTypes());
			ApplicationHelper.getConfigService().setApplicationProperty("image.allowedTypes", str);	
		}			
	}

	public List<String> getAllowedTypes() {
		return imageConfig.getAllowedTypes();
	}

	public boolean isAllowAllByDefault() {
		return imageConfig.isAllowAllByDefault();
	}

	public void setAllowAllByDefault(boolean allowed) {
		imageConfig.setAllowAllByDefault(allowed);
		String str = (new StringBuilder()).append("").append(allowed).toString();
		ApplicationHelper.getConfigService().setApplicationProperty("image.allowAllByDefault", str);			
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW )
	public void deleteImage(Image image){		
		Image imageToUse = imageDao.getImageById(image.getImageId());
		imageDao.deleteImage(imageToUse);
		File dir = new File(getImageDir(), "cache" );
		 File imageFile = new File(dir, (new StringBuilder()).append(imageToUse.getImageId()).append(".bin").toString());
         if(imageFile.exists())
             imageFile.delete();
	}
	
	
	public Image createImage(int objectType, long objectId, String name, String contentType, File file) {
		Date now = new Date();
		ImageImpl image = new ImageImpl();
		image.setCreationDate(now);
		image.setModifiedDate(now);
		image.setObjectType(objectType);
		image.setObjectId(objectId);
		image.setContentType(contentType);
		image.setName(name);
		image.setImageId(-1L);
		
		image.setSize( (int) FileUtils.sizeOf(file));
		try {
			image.setInputStream(FileUtils.openInputStream(file));
		} catch (IOException e) {
			log.debug(e);
		}		
		return image;
	}
	
	public Image createImage(int objectType, long objectId, String name, String contentType, InputStream inputStream) {
		Date now = new Date();
		ImageImpl image = new ImageImpl();
		image.setCreationDate(now);
		image.setModifiedDate(now);
		image.setObjectType(objectType);
		image.setObjectId(objectId);
		image.setContentType(contentType);
		image.setName(name);
		image.setImageId(-1L);
		image.setInputStream(inputStream);		
		try {
			image.setSize( IOUtils.toByteArray(inputStream).length );
		} catch (IOException e) {
			log.debug(e);
		}
		return image;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW )
	public void saveOrUpdate(Image image) {
		try {
			if( image.getImageId() < 0 ){
				imageDao.createImage(image);
				imageDao.saveImageInputStream(image, image.getInputStream());
			}else{
				Date now = new Date();
				((ImageImpl)image).setModifiedDate(now);
				imageDao.updateImage(image)
				;
				imageDao.saveImageInputStream(image, image.getInputStream());
			}
			
			Collection<File> files = FileUtils.listFiles(getImageCacheDir(), FileFilterUtils.prefixFileFilter(image.getImageId() + ""), null);
			for(File file : files){
				FileUtils.forceDelete(file);
			}			
			Image imageToUse = getImage(image.getImageId());			
			imageCache.remove(imageToUse.getImageId());
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW )
	public Image saveImage(Image image){
		
		try {
			if( image.getImageId() < 0 ){
				Image newImage = imageDao.createImage(image);
				imageDao.saveImageInputStream(newImage, image.getInputStream());
			}else{
				Date now = new Date();
				((ImageImpl)image).setModifiedDate(now);
				imageDao.updateImage(image);
				imageDao.saveImageInputStream(image, image.getInputStream());				
			}					
			
			deleteImageFileCache(image);
			
			Image imageToUse = getImage(image.getImageId());			
			imageCache.remove(imageToUse.getImageId());
			
			return imageToUse;
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}
	
	private void deleteImageFileCache(Image image ){
		
		Collection<File> files = FileUtils.listFiles(getImageCacheDir(), FileFilterUtils.prefixFileFilter(image.getImageId() + ""), null);
		for(File file : files){
			log.debug( file.getPath() + ":" + file.isFile() );
			try {
				FileUtils.forceDelete(file);
			} catch (IOException e) {
				log.error(e);
			}
		}
		
	}
		
	public Image getImage(long imageId) throws NotFoundException {
		Image imageToUse = null  ;
		if( imageCache.get(imageId) == null){
			try {
				imageToUse = getImageById(imageId);
				imageCache.put(new Element(imageId, imageToUse));
			} catch (Exception e) {
				 String msg = (new StringBuilder()).append("Unable to find image ").append(imageId).toString();
	             throw new NotFoundException(msg, e);
			}
		}else{
			imageToUse =  (Image) imageCache.get( imageId ).getValue();
		}		
		return imageToUse;
	}

	private Image getImageById(long imageId) throws NotFoundException {
		try {
			return imageDao.getImageById(imageId);
		} catch (Exception e) {
			 String msg = (new StringBuilder()).append("Unable to find image ").append(imageId).toString();
             throw new NotFoundException(msg, e);
		}
	}
		
	public Cache getImageCache() {
		return imageCache;
	}

	public void setImageCache(Cache imageCache) {
		this.imageCache = imageCache;
	}

	protected synchronized File getImageDir() {
		if(imageDir == null)
        {
			imageDir = ApplicationHelper.getRepository().getFile("images");
			if(!imageDir.exists())
            {
                boolean result = imageDir.mkdir();
                if(!result)
                    log.error((new StringBuilder()).append("Unable to create image directory: '").append(imageDir).append("'").toString());
                
                getImageCacheDir(); //new File(imageDir, "cache");                
                getImageTempDir();                
            }else{
            	File dir = getImageTempDir();
            	try {
					FileUtils.cleanDirectory(dir);
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        }
        return imageDir;
	}
	

	public InputStream getImageInputStream(Image image) {		
		try {
			File file = getImageFromCacheIfExist(image);
			return FileUtils.openInputStream(file);
		} catch (IOException e) {
			throw new SystemException(e);
		}
	}
		
	public InputStream getImageThumbnailInputStream(Image image, int width, int height ) {		
		try {			
			File file = getThumbnailFromCacheIfExist(image, width, height);
			return FileUtils.openInputStream(file);
		} catch (IOException e) {
			throw new SystemException(e);
		}finally {
			
		}
	}
	
	/**
	 * 
	 * @param image
	 * @return
	 * @throws IOException
	 */
	protected File getImageFromCacheIfExist(Image image) throws IOException{		
		
		File dir = getImageCacheDir();
		
		StringBuilder sb = new StringBuilder();
		sb.append( image.getImageId() ).append(".bin");		
		File file = new File(dir, sb.toString() );	
		
		
		if( file.exists() ){
			long size = FileUtils.sizeOf(file);
			if( size != image.getSize() ){
				// size different make cache new one....
				InputStream inputStream = imageDao.getImageInputStream(image);
				FileUtils.copyInputStreamToFile(inputStream, file);
			}
		}else{
			// doesn't exist, make new one ..
			InputStream inputStream = imageDao.getImageInputStream(image);
			FileUtils.copyInputStreamToFile(inputStream, file);
		}		
		
		return file;
	}	
	
	protected File getImageCacheDir(){
		File dir = new File(getImageDir(), "cache" );	
		if( !dir.exists() ){
			dir.mkdir();
		}
		return dir;
	}

	protected File getImageTempDir(){
		File dir = new File(getImageDir(), "temp" );
		if( !dir.exists() ){
			dir.mkdir();
		}
		return dir;
	}
	
	protected String toThumbnailFilename(Image image,  int width, int height){
		StringBuilder sb = new StringBuilder();
		sb.append( image.getImageId() ).append("_").append(width).append("_").append(height).append(".bin");	
		return sb.toString();
	}
	
	protected File getTemeFile(){
		UUID uuid = UUID.randomUUID();		
		File tmp = new File(getImageTempDir(), uuid.toString());		
		return  tmp;
	}
	
	protected File getThumbnailFromCacheIfExist(Image image,  int width, int height ) throws IOException{			
		try {
			lock.lock();
			
			log.debug( "thumbnail : " + width + " x " + height );
			File dir = getImageCacheDir();
			File file = new File(dir, toThumbnailFilename(image, width, height) );		
			File originalFile = getImageFromCacheIfExist( image );	
			
			log.debug( "orignal image source: " + originalFile.getAbsoluteFile() + ", " + originalFile.length() + " thumbnail:" + file.getAbsoluteFile() + " - " + file.exists() );
			
			if( file.exists() ){
				log.debug(file.length());
				if( file.length() > 0 ){
					image.setThumbnailSize((int)file.length());
					return file;
				}else{
				}
			}
			
			/**
			 * TIP : 윈동우 경우 Thumbnail 파일 생성후에도 해당 파일을 참조하는 문제가 있음. 
			 */
			log.debug( "create thumbnail : " + file.getAbsolutePath()  );
			if( Platform.current() == Platform.WINDOWS ){
				File tmp = getTemeFile();
				Thumbnails.of(originalFile).size(width, height).outputFormat("png").toOutputStream(new FileOutputStream(tmp)); 		
				image.setThumbnailSize((int)tmp.length());
				FileUtils.copyFile(tmp, file);			
			}else{				
				try {
					Thumbnails.of(originalFile).allowOverwrite(true).size(width, height).outputFormat("png").toOutputStream(new FileOutputStream(file));
				} catch (Throwable e) {
					log.error(e);
				} 		
				image.setThumbnailSize((int)file.length());
			}
			
			return file;
		
		} finally  {
			lock.unlock();
		}		
		
	}
	
		
	public void destroy(){
		
	}
		
	public void  initialize() {		
		log.debug( "initializing image manager" );		
		ImageConfig imageConfigToUse = new ImageConfig();
		imageConfigToUse.setEnabled( ApplicationHelper.getApplicationBooleanProperty("image.enabled", true) );
		imageConfigToUse.setAllowAllByDefault( ApplicationHelper.getApplicationBooleanProperty("image.allowAllByDefault", true) );
		imageConfigToUse.setForceThumbnailsEnabled( ApplicationHelper.getApplicationBooleanProperty("image.forceThumbnailsEnabled", true) );
		imageConfigToUse.setMaxImageSize( ApplicationHelper.getApplicationIntProperty("", 2048) );
		imageConfigToUse.setImagePreviewMaxSize(ApplicationHelper.getApplicationIntProperty("image.imagePreviewMaxSize", 250));
		imageConfigToUse.setImageMaxWidth(ApplicationHelper.getApplicationIntProperty("image.imageMaxWidth", 450));
		imageConfigToUse.setImageMaxHeight(ApplicationHelper.getApplicationIntProperty("image.imageMaxHeight", 600));
		imageConfigToUse.setMaxImagesPerObject(ApplicationHelper.getApplicationIntProperty("image.maxImagesPerObject", 50));
		imageConfigToUse.setAllowedTypes( stringToList(ApplicationHelper.getApplicationProperty("image.allowedTypes", ""))  );
		imageConfigToUse.setDisallowedTypes( stringToList( ApplicationHelper.getApplicationProperty("image.disallowedTypes", "")));
		this.imageConfig = imageConfigToUse;
		
		getImageDir();				
		log.debug( imageConfig );
	}

	public List<Image> getImages(int objectType, long objectId) {
		List<Long> imageIds = imageDao.getImageIds(objectType, objectId);
		List<Image> list = new ArrayList<Image>(imageIds.size());
		for( Long imageId : imageIds )
			try {
				list.add(getImage(imageId));
			} catch (Exception e) {}
		
		return list;
	}

	public List<Image> getImages(int objectType, long objectId, int startIndex, int maxResults) {
		List<Long> imageIds = imageDao.getImageIds(objectType, objectId, startIndex, maxResults);
		List<Image> list = new ArrayList<Image>(imageIds.size());
		for( Long imageId : imageIds )
			try {
				list.add(getImage(imageId));
			} catch (Exception e) {}
		
		return list;
	}

	public int getTotalImageCount(int objectType, long objectId) {
		return imageDao.getImageCount(objectType, objectId);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW )
	public void updateImageProperties(Image image) {
		try {
			if( image.getImageId() > 0 ){
				Date now = new Date();
				((ImageImpl)image).setModifiedDate(now);
				imageDao.updateImage(image);
			}
			imageCache.remove(image.getImageId());
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}	

}