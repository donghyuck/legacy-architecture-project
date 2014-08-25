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
package architecture.ee.web.community.logo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.coobird.thumbnailator.Thumbnails;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import architecture.common.user.Company;
import architecture.common.util.PlatformHelper.Platform;
import architecture.ee.exception.SystemException;
import architecture.ee.util.ApplicationHelper;
import architecture.ee.web.community.logo.dao.LogoImageDao;
import architecture.ee.web.logo.LogoImage;
import architecture.ee.web.logo.LogoImageNotFoundException;
import architecture.ee.web.logo.LogoManager;
import architecture.ee.web.site.WebSite;

public class DefaultLogoManager implements LogoManager{

	private LogoImageDao logoImageDao;
	
	private Lock lock = new ReentrantLock();
	
	private Log log = LogFactory.getLog(getClass());
		
	private Cache logoImageIdsCache ;
	
	private Cache logoImageCache ;
	
	private File imageDir;
	
	public DefaultLogoManager() {
	}

	/**
	 * @param logoImageIdsCache 설정할 logoImageIdsCache
	 */
	public void setLogoImageIdsCache(Cache logoImageIdsCache) {
		this.logoImageIdsCache = logoImageIdsCache;
	}



	/**
	 * @param logoImageCache 설정할 logoImageCache
	 */
	public void setLogoImageCache(Cache logoImageCache) {
		this.logoImageCache = logoImageCache;
	}


	/**
	 * @param logoImageDao 설정할 logoImageDao
	 */
	public void setLogoImageDao(LogoImageDao logoImageDao) {
		this.logoImageDao = logoImageDao;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW )
	public void addLogoImage(LogoImage logoImage, File file) {
		if( logoImage.getLogoId() < 1) {
			// clear cache  			
			List<Long> list = getLogoImageIdList(logoImage.getObjectType(), logoImage.getObjectId());
			for( Long logoImageId : list){
				this.logoImageCache.remove(logoImageId);
			}
			this.logoImageIdsCache.remove(getLogoImageIdListCacheKey(logoImage.getObjectType(), logoImage.getObjectId()));			
			logoImageDao.addLogoImage(logoImage, file);			
		}
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW )
	public void removeLogoImage(LogoImage logoImage) throws LogoImageNotFoundException {
		if( logoImage.getLogoId() > 1) {
			// clear cache  			
			List<Long> list = getLogoImageIdList(logoImage.getObjectType(), logoImage.getObjectId());
			for( Long logoImageId : list){
				this.logoImageCache.remove(logoImageId);
			}
			this.logoImageIdsCache.remove(getLogoImageIdListCacheKey(logoImage.getObjectType(), logoImage.getObjectId()));			
			logoImageDao.removeLogoImage(logoImage);
			deleteImageFileCache(logoImage);
		}
	}
	private void deleteImageFileCache(LogoImage image ){		
		Collection<File> files = FileUtils.listFiles(
			getImageCacheDir(), 
			FileFilterUtils.prefixFileFilter(image.getLogoId().toString()),
			FileFilterUtils.suffixFileFilter(".logo")
		);
		for(File file : files){
			log.debug( file.getPath() + ":" + file.isFile() );
			try {
				FileUtils.forceDelete(file);
			} catch (IOException e) {
				log.error(e);
			}
		}
			
	}

	public LogoImage getLogoImageById(Long logoId) throws LogoImageNotFoundException {
		if( logoId < 1 )
			throw new LogoImageNotFoundException();		
		LogoImage imageToUse ;		
		if( logoImageCache.get(logoId) == null ){
			imageToUse = logoImageDao.getLogoImageById(logoId);
			logoImageCache.put(new Element(imageToUse.getLogoId(), imageToUse ));
		}else{
			imageToUse = (LogoImage)logoImageCache.get(logoId).getValue();
		}
		return imageToUse;
	}

	public LogoImage getPrimaryLogoImage(int objectType, long objectId) throws LogoImageNotFoundException {		
		List<LogoImage> list = getLogoImages(objectType, objectId);
		for(LogoImage logo : list ){
			if( logo.isPrimary() )
				return logo;
		}
		throw new LogoImageNotFoundException();
	}

	public List<LogoImage> getLogoImages(int objectType, long objectId) {
		List<Long> ids = getLogoImageIdList(objectType, objectId);
		List<LogoImage> list = new ArrayList<LogoImage>(ids.size());
		for(long logoId : ids ){
			try {
				list.add(getLogoImageById(logoId));
			} catch (LogoImageNotFoundException e) {}
		}
		return list;
	}

	public int getLogoImageCount(int objectType, long objectId) {
		String key = getLogoImageIdListCacheKey(objectType, objectId);
		if( logoImageIdsCache.get(key) != null ){
			return ((List<Long>) logoImageIdsCache.get(key).getValue()).size();
		}		
		return logoImageDao.getLogoImageCount(objectType, objectId);
	}

	public InputStream getImageInputStream(LogoImage image) throws IOException {		
		try {
			File file = getImageFromCacheIfExist(image);
			return FileUtils.openInputStream(file);
		} catch (IOException e) {
			throw new SystemException(e);
		}		
	}
	
	public InputStream getImageThumbnailInputStream(LogoImage image, int width, int height ) {		
		try {			
			File file = getThumbnailFromCacheIfExist(image, width, height);
			return FileUtils.openInputStream(file);
		} catch (IOException e) {
			throw new SystemException(e);
		}finally {
			
		}
	}
	

	

	public LogoImage getPrimaryLogoImage(Company company) throws LogoImageNotFoundException {
		return getPrimaryLogoImage(company.getModelObjectType(), company.getCompanyId());
	}

	public LogoImage getPrimaryLogoImage(WebSite site) throws LogoImageNotFoundException {
		return getPrimaryLogoImage(site.getModelObjectType(), site.getWebSiteId());
	}

	public List<LogoImage> getLogoImages(Company company) {
		return getLogoImages(company.getModelObjectType(), company.getCompanyId());
	}

	public int getLogoImageCount(Company company) {
		return getLogoImageCount(company.getModelObjectType(), company.getCompanyId());
	}

	public List<LogoImage> getLogoImages(WebSite site) {
		return getLogoImages(site.getModelObjectType(), site.getWebSiteId());
	}

	public int getLogoImageCount(WebSite site) {
		return getLogoImageCount(site.getModelObjectType(), site.getWebSiteId());
	}

	public void  initialize() {		
		log.debug( "initializing profile manager" );	
		getImageDir();
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
	
	protected List<Long> getLogoImageIdList( int objectType, long objectId){
		String key = getLogoImageIdListCacheKey(objectType, objectId);
		List<Long> idsList ; 
		if( logoImageIdsCache.get(key) == null ){
			idsList = logoImageDao.getLogoImageIds(objectType, objectId);
			logoImageIdsCache.put(new Element(key, idsList));			
		}else{
			idsList = (List<Long>) logoImageIdsCache.get(key).getValue();
		}
		return idsList;
	}
	
	protected String getLogoImageIdListCacheKey (int objectType, long objectId) {
		return (new StringBuilder()).append("objectType-").append(objectType).append("-objectId-").append(objectId).toString(); 
	}

	protected String toThumbnailFilename(LogoImage image,  int width, int height){
		StringBuilder sb = new StringBuilder();
		sb.append( image.getLogoId() ).append("_").append(width).append("_").append(height).append(".logo");	
		return sb.toString();
	}
	
	protected File getThumbnailFromCacheIfExist(LogoImage image,  int width, int height ) throws IOException{			
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
	
	protected File getTemeFile(){
		UUID uuid = UUID.randomUUID();		
		File tmp = new File(getImageTempDir(), uuid.toString());		
		return  tmp;
	}
	
	/**
	 * 
	 * @param image
	 * @return
	 * @throws IOException
	 */
	protected File getImageFromCacheIfExist(LogoImage image) throws IOException{
		File dir = getImageCacheDir();		
		StringBuilder sb = new StringBuilder();
		sb.append( image.getLogoId() ).append(".logo");		
		File file = new File(dir, sb.toString() );			
		if( file.exists() ){
			long size = FileUtils.sizeOf(file);
			if( size != image.getImageSize() ){
				InputStream inputStream = logoImageDao.getInputStream(image);
				FileUtils.copyInputStreamToFile(inputStream, file);
			}
		}else{
			// doesn't exist, make new one ..
			InputStream inputStream = logoImageDao.getInputStream(image);
			FileUtils.copyInputStreamToFile(inputStream, file);
		}		
		return file;
	}	
	
}
