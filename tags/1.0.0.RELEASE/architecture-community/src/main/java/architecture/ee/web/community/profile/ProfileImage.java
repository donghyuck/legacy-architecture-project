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

import java.util.Date;

import architecture.common.cache.Cacheable;

public interface ProfileImage extends Cacheable {
	
	public static final String DEFAULT_THUMBNAIL_CONTENT_TYPE ="image/png" ;
	
	public Long getProfileImageId();
	
	public Long getUserId();
	
	public Boolean isPrimary();
	
	public void setPrimary(Boolean primary) ;
	
	public String getImageContentType();
	
	public void setImageContentType(String imageContentType);
	
	public Integer getImageSize();
	
	public void setImageSize(Integer imageSize);
	
	public String getThumbnailContentType();
	
	public Integer getThumbnailSize();
	
	public void setThumbnailContentType(String thumbnailContentType);
	
	public void setThumbnailSize(Integer thumbnailSize);
	
	public Date getCreationDate();

	/**
	 * @param creationDate 설정할 creationDate
	 */
	public void setCreationDate(Date creationDate);

	/**
	 * @return modifiedDate
	 */
	public Date getModifiedDate();

	/**
	 * @param modifiedDate 설정할 modifiedDate
	 */
	public void setModifiedDate(Date modifiedDate);
	
	public String getFilename();

	public void setFilename(String filename) ;
}
