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
package architecture.ee.web.attachment.dao;

import java.io.InputStream;
import java.util.List;

import architecture.ee.web.attachment.Image;

public interface ImageDao {

	public abstract Image createImage(Image image);
	
	public abstract Image updateImage(Image image);
	
	public abstract void deleteImage(Image image);
	
	public abstract InputStream getImageInputStream(Image image);
	
	public abstract void saveImageInputStream(Image image , InputStream inputStream);

	public abstract Image getImageById(long imageId);

	public List<Long> getImageIds(int objectType, long objectId, int startIndex, int numResults);
	
	public List<Long> getImageIds(int objectType, long objectId);
	
	public int getImageCount( int objectType, long objectId );
	
	public long getImageUsage( int objectType, long objectId );
}
