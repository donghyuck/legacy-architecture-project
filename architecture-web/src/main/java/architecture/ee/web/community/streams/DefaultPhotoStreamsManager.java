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
package architecture.ee.web.community.streams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import architecture.common.user.User;
import architecture.common.user.UserManager;
import architecture.common.user.UserNotFoundException;
import architecture.common.user.authentication.AnonymousUser;
import architecture.ee.web.community.PhotoNotFoundException;
import architecture.ee.web.community.streams.dao.StreamsDao;
import architecture.ee.web.community.streams.impl.PhotoImpl;

public class DefaultPhotoStreamsManager implements PhotoStreamsManager {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 자동 생성된 메소드 스텁
UUID.randomUUID().toString();
	}
	
	private Cache photoStreamCache;
	
	private StreamsDao streamsDao;		
	
	private UserManager userManager;




	protected void clearPhotoInCache( Photo photo ){
		photoStreamCache.remove(photo.getExternalId());
	}




	public Photo getPhotoById(String externalId) throws PhotoNotFoundException {
		Photo photoToUse = getPhotoFromCache(externalId);
		if( photoToUse == null ){
			photoToUse = getStreamsDao().getPhotoStream(externalId);
			User u;
			try {
				u = userManager.getUser(((PhotoImpl)photoToUse).getCreatorId());
				((PhotoImpl)photoToUse).setCreator(u);
			} catch (UserNotFoundException e) {
				((PhotoImpl)photoToUse).setCreator(new AnonymousUser());
			}			
			updatePhotoInCache(photoToUse);
		}
		return photoToUse;
	}




	public int getPhotoCount(int objectType) {
		return getStreamsDao().getPhotoCount(objectType);
	}




	public int getPhotoCount(int objectType, long objectId) {
		return getStreamsDao().getPhotoCount(objectType, objectId);
	}




	protected Photo getPhotoFromCache(String externalId){
		if( photoStreamCache.get(externalId) != null)
			return (Photo) photoStreamCache.get( externalId ).getValue();
		return null;
	}


	public List<Photo> getPhotos() {
		List<String> ids = getStreamsDao().getPhotoIds();
		return toPhotoList(ids);
	}
	
	public List<Photo> getPhotos(int objectType) {
		List<String> ids = getStreamsDao().getPhotoIds(objectType);		
		return toPhotoList(ids);
	}

	public List<Photo> getPhotos(int startIndex, int maxResults) {
		
		List<String> ids = getStreamsDao().getPhotoIds(startIndex, maxResults);		
		
		return toPhotoList(ids);
	}
	
	public List<Photo> getPhotos(int objectType, int startIndex, int maxResults) {
		List<String> ids = getStreamsDao().getPhotoIds(objectType, startIndex, maxResults);		
		return toPhotoList(ids);
	}




	public List<Photo> getPhotos(int objectType, long objectId) {
		List<String> ids = getStreamsDao().getPhotoIds(objectType, objectId);		
		return toPhotoList(ids);
	}

	public List<Photo> getPhotos(int objectType, long objectId, int startIndex, int maxResults) {
		List<String> ids = getStreamsDao().getPhotoIds(objectType, objectId, startIndex, maxResults);	
		return toPhotoList(ids);
	}

	/**
	 * @return photoStreamCache
	 */
	public Cache getPhotoStreamCache() {
		return photoStreamCache;
	}

	/**
	 * @return streamsDao
	 */
	public StreamsDao getStreamsDao() {
		return streamsDao;
	}

	public int getTotalPhotoCount() {		
		return getStreamsDao().getPhotoCount();
	}

	/**
	 * @return userManager
	 */
	public UserManager getUserManager() {
		return userManager;
	}

	/**
	 * @param photoStreamCache 설정할 photoStreamCache
	 */
	public void setPhotoStreamCache(Cache photoStreamCache) {
		this.photoStreamCache = photoStreamCache;
	}
	
	/**
	 * @param streamsDao 설정할 streamsDao
	 */
	public void setStreamsDao(StreamsDao streamsDao) {
		this.streamsDao = streamsDao;
	}

	/**
	 * @param userManager 설정할 userManager
	 */
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	protected List<Photo> toPhotoList(List<String> ids ){
		if( ids.size() == 0 )
			return Collections.EMPTY_LIST;
		
		List<Photo> photos = new ArrayList<Photo>(ids.size());
		for( String id : ids){
			Photo p;
			try {
				p = getPhotoById(id);
				photos.add(p);
			} catch (PhotoNotFoundException e) {
			}			
		}
		return photos;
	}

	protected void updatePhotoInCache( Photo photo ){
		photoStreamCache.put(new Element(photo.getExternalId(), photo));		
	}

}
