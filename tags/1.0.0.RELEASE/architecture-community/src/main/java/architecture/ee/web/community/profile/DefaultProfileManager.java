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
package architecture.ee.web.community.profile;

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

import architecture.common.user.User;
import architecture.common.user.UserManager;
import architecture.common.user.UserNotFoundException;
import architecture.common.util.PlatformHelper.Platform;
import architecture.ee.exception.NotFoundException;
import architecture.ee.exception.SystemException;
import architecture.ee.util.ApplicationHelper;
import architecture.ee.web.community.profile.dao.ProfileDao;

public class DefaultProfileManager implements ProfileManager {

	private Lock lock = new ReentrantLock();
	
	private Log log = LogFactory.getLog(getClass());
	
	private ProfileDao profileDao ;
	
	private Cache userProfileImageIdCache ;
	
	private Cache profileImageCache ;
	
	private File imageDir;
	
	private UserManager userManager;
	
	
	
	/**
	 * @return userProfileImageIdCache
	 */
	public Cache getUserProfileImageIdCache() {
		return userProfileImageIdCache;
	}

	/**
	 * @param userProfileImageIdCache 설정할 userProfileImageIdCache
	 */
	public void setUserProfileImageIdCache(Cache userProfileImageIdCache) {
		this.userProfileImageIdCache = userProfileImageIdCache;
	}

	/**
	 * @return userManager
	 */
	public UserManager getUserManager() {
		return userManager;
	}

	/**
	 * @param userManager 설정할 userManager
	 */
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	/**
	 * @return profileImageCache
	 */
	public Cache getProfileImageCache() {
		return profileImageCache;
	}

	/**
	 * @param profileImageCache 설정할 profileImageCache
	 */
	public void setProfileImageCache(Cache profileImageCache) {
		this.profileImageCache = profileImageCache;
	}

	/**
	 * @return profileDao
	 */
	public ProfileDao getProfileDao() {
		return profileDao;
	}

	/**
	 * @param profileDao 설정할 profileDao
	 */
	public void setProfileDao(ProfileDao profileDao) {
		this.profileDao = profileDao;
	}
	
	public ProfileImage getProfileImage(User user) throws ProfileImageNotFoundException {	
				
		Long profileImageId = -1L;
		if ( userProfileImageIdCache.get(user.getUserId()) == null ){
			profileImageId = profileDao.getPrimaryProfileImageByUser(user.getUserId());
			userProfileImageIdCache.put(new Element(user.getUserId(), profileImageId));
		}else{
			profileImageId = (Long) userProfileImageIdCache.get(user.getUserId()).getValue();
		}
		
		if( profileImageId == -1L )
			throw new ProfileImageNotFoundException();
		
		return getProfileImageById(profileImageId);
	}
	
	public List<ProfileImage> getProfileImages(User user) {
		List<Long> ids = profileDao.getProfileImageIds(user.getUserId());
		List<ProfileImage> list = new ArrayList<ProfileImage>(ids.size());
		for(Long id : ids){
			try {
				list.add(getProfileImageById(id));
			} catch (NotFoundException e) {	}
		}
		return list;
	}

	public Integer getProfileImageCount(User user) {
		return profileDao.getProfileImageCount(user.getUserId());
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW )
	public void addProfileImage(ProfileImage image, File file, User user) {		
		List<Long> list = profileDao.getProfileImageIds(user.getUserId());
		for( Long id : list ){
			profileImageCache.remove(id);
		}
		
		profileDao.addProfileImage(image, file);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW )
	public void removeProfileImage(ProfileImage image) {		
		profileImageCache.remove(image.getProfileImageId());		
		profileDao.removeProfileImage(image);	
		deleteImageFileCache(image);
	}

	public ProfileImage getProfileImageById(Long profileImageId) throws ProfileImageNotFoundException {		
		ProfileImage image ;
		if( profileImageCache.get(profileImageId) == null ){
			 image = profileDao.getProfileImageById(profileImageId);		
			 profileImageCache.put(new Element(profileImageId, image));
		}else{
			image = (ProfileImage)( profileImageCache.get(profileImageId).getValue() );
		}
		return image;
	}
	
	public InputStream getImageInputStream(ProfileImage image) {		
		try {
			File file = getImageFromCacheIfExist(image);
			return FileUtils.openInputStream(file);
		} catch (IOException e) {
			throw new SystemException(e);
		}
	}
		
	public InputStream getImageThumbnailInputStream(ProfileImage image, int width, int height ) {		
		try {			
			File file = getThumbnailFromCacheIfExist(image, width, height);
			return FileUtils.openInputStream(file);
		} catch (IOException e) {
			throw new SystemException(e);
		}finally {
			
		}
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
	
	private void deleteImageFileCache(ProfileImage image ){
		
		Collection<File> files = FileUtils.listFiles(
			getImageCacheDir(), 
			FileFilterUtils.prefixFileFilter(image.getProfileImageId().toString()),
			FileFilterUtils.suffixFileFilter(".profile")
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
	
	
	protected File getThumbnailFromCacheIfExist(ProfileImage image,  int width, int height ) throws IOException{			
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

	/**
	 * 
	 * @param image
	 * @return
	 * @throws IOException
	 */
	protected File getImageFromCacheIfExist(ProfileImage image) throws IOException{
		File dir = getImageCacheDir();		
		StringBuilder sb = new StringBuilder();
		sb.append( image.getProfileImageId() ).append(".profile");		
		File file = new File(dir, sb.toString() );			
		if( file.exists() ){
			long size = FileUtils.sizeOf(file);
			if( size != image.getImageSize() ){
				InputStream inputStream = profileDao.getInputStream(image);
				FileUtils.copyInputStreamToFile(inputStream, file);
			}
		}else{
			// doesn't exist, make new one ..
			InputStream inputStream = profileDao.getInputStream(image);
			FileUtils.copyInputStreamToFile(inputStream, file);
		}		
		return file;
	}	
	
	protected File getTemeFile(){
		UUID uuid = UUID.randomUUID();		
		File tmp = new File(getImageTempDir(), uuid.toString());		
		return  tmp;
	}
	
	protected String toThumbnailFilename(ProfileImage image,  int width, int height){
		StringBuilder sb = new StringBuilder();
		sb.append( image.getProfileImageId() ).append("_").append(width).append("_").append(height).append(".profile");	
		return sb.toString();
	}
	
	public void  initialize() {		
		log.debug( "initializing profile manager" );	
		getImageDir();
	}

	public ProfileImage getProfileImageByUsername(String username) throws ProfileImageNotFoundException, UserNotFoundException {
		User user = userManager.getUser(username);
		return getProfileImage(user);
	}
	
	
}
