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

import java.util.List;

import architecture.common.user.User;
import architecture.ee.exception.NotFoundException;
import architecture.ee.web.attachment.Image;

public interface PhotoStreamsManager {

	
	public void addImage(Image image, User creator);
	
	public void deletePhotos(Image image, User creator);
	
	public List<Photo> getPhotosByImage(Image image);
	
	public Photo getPhotoById(String externalId) throws NotFoundException ;	
	
	public int getPhotoCount(int objectType, long objectId);
	
	public int getPhotoCount(int objectType);
	
	public int getTotalPhotoCount();	
	
	public List<Photo> getPhotos(int objectType, long objectId); 
	
	public List<Photo> getPhotos(int objectType, long objectId, int startIndex, int maxResults );
	
	public List<Photo> getPhotos(int objectType);
	
	public List<Photo> getPhotos(int objectType, int startIndex, int maxResults );
	
	public List<Photo> getPhotos();
	
	public List<Photo> getPhotos(int startIndex, int maxResults );	
	
	public List<Photo> getPhotosByRandom(int objectType, long objectId); 
	
	public List<Photo> getPhotosByRandom(int objectType, long objectId, int startIndex, int maxResults );
	
	public List<Photo> getPhotosByRandom(int objectType);
	
	public List<Photo> getPhotosByRandom(int objectType, int startIndex, int maxResults );
	
	public List<Photo> getPhotosByRandom();
	
	public List<Photo> getPhotosByRandom(int startIndex, int maxResults );	
	
	
}
