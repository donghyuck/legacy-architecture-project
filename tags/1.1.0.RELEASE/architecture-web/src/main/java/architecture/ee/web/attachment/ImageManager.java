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
import java.io.InputStream;
import java.util.List;

import architecture.ee.exception.NotFoundException;

/**
 * 
 * 
 * 
 * @author donghyuck
 *
 */
public interface ImageManager {

	public abstract boolean isImageEnabled();
	
	public abstract void setImageEnabled(boolean enabled) ;
	
	public abstract Image getImage(long imageId) throws NotFoundException ;

	public abstract int getMaxImageSize();
	
	public abstract void setMaxImageSize(int maxImageSize);
	
	public abstract int getImagePreviewMaxSize();
	
	public abstract void setImagePreviewMaxSize(int imagePreviewMaxSize);
	
	public abstract boolean isForceThumbnailsEnabled();
	
	public abstract void setFourceThumbnailsEnabled(boolean forceThumbnailsEnabled);
	
	public abstract int getImageMaxWidth();
	
	public abstract void setImageMaxWidth(int imageMaxWidth);
	
	public abstract int getImageMaxHeight();
	
	public abstract void setImageMaxHeight( int imageMaxHeight );
	
	public abstract boolean isValidType(String contentType);
	
	public abstract void addAllowedType(String contentType);
	
	public abstract void removeAllowedType(String contentType);
	
	public abstract List<String> getAllowedTypes();
	
	public abstract boolean isAllowAllByDefault();
	
	public abstract void setAllowAllByDefault( boolean allowed );
	
	public abstract List<String> getDisallowedTypes();
	
	public abstract void addDisallowedType(String contentType);
	
	public abstract void removeDisallowedType(String contentType);
	
	public abstract Image createImage(int objectType, long objectId, String name, String contentType, File file);
	
	public abstract Image createImage(int objectType, long objectId, String name, String contentType, InputStream inputStream);
	
	public abstract void saveOrUpdate( Image image );
	
	public abstract Image saveImage( Image image );
	
	public abstract void updateImageProperties( Image image );
	
	public abstract InputStream getImageInputStream(Image image);
	
	public abstract InputStream getImageThumbnailInputStream(Image image, int width, int height ) ;
	
	public List<Image> getImages(int objectyType ,  long objectId );
	
	public List<Image> getImages(int objectyType ,  long objectId, int startIndex, int maxResults );
	
	public int getTotalImageCount(int objectType, long objectId );
		
	public Image getImageByImageLink(String linkId ) throws NotFoundException ;
	
	public ImageLink getImageLink( Image image ) throws NotFoundException ;
	
	public ImageLink getImageLink( Image image , boolean createIfNotExist  ) throws NotFoundException ;
	
	public void removeImageLink( Image image ) ;
	
}
