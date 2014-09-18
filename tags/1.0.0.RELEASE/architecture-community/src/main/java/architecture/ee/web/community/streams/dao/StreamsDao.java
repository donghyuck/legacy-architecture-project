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
package architecture.ee.web.community.streams.dao;

import java.util.List;

import architecture.ee.web.attachment.Image;
import architecture.ee.web.community.streams.Photo;
import architecture.ee.web.community.streams.PhotoNotFoundException;

public interface StreamsDao {
	
	public List<String> getPhotoIdsByImage(long imageId);
	
	public List<String> getPhotoIds () ;

	public List<String> getPhotoIds (int objectType) ;
	
	public List<String> getPhotoIds (int objectType, long objectId) ;

	public List<String> getPhotoIds (int startIndex, int maxResults ) ;

	public List<String> getPhotoIds (int objectType, int startIndex, int maxResults ) ;
	
	public List<String> getPhotoIds (int objectType, long objectId, int startIndex, int maxResults ) ;
	
	public Photo getPhotoStream(String photoId ) throws PhotoNotFoundException ;
	
	public void addPhoto(Photo photo) ;
	
	public void removePhoto(Photo photo) ;
	
	public void removePhotos(Image image) ;
	
	public int getPhotoCount();
	
	public int getPhotoCount(int objectType);
	
	public int getPhotoCount(int objectType, long objectId);
	
	
}
