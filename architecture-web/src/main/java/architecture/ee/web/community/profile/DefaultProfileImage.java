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
import java.util.Calendar;
import java.util.Date;

import architecture.common.cache.CacheSizes;
import architecture.common.user.User;

public class DefaultProfileImage implements ProfileImage {

	private Long profileImageId;

	private Long userId;

	private Boolean primary;

	private String imageContentType;

	private Integer imageSize;

	private String thumbnailContentType;

	private Integer thumbnailSize;

	private Date creationDate ;
	
	private Date modifiedDate;
	
	private String filename;
	
	public DefaultProfileImage(User user) {
		this.userId = user.getUserId();
		this.profileImageId = -1L;
		this.primary = false;
		this.imageContentType = null;
		this.imageSize = 0 ;
		this.thumbnailContentType = DEFAULT_THUMBNAIL_CONTENT_TYPE;
		this.thumbnailSize = 0;
		Date now =  Calendar.getInstance().getTime();
		this.creationDate = now;
		this.modifiedDate = now;
	}
	/**
	 * 
	 */
	public DefaultProfileImage() {
		this.profileImageId = -1L;
		this.userId = -1L;
		this.primary = false;
		this.imageContentType = null;
		this.imageSize = 0 ;
		this.thumbnailContentType = DEFAULT_THUMBNAIL_CONTENT_TYPE;
		this.thumbnailSize = 0;
		Date now =  Calendar.getInstance().getTime();
		this.creationDate = now;
		this.modifiedDate = now;
	}

	/**
	 * @return primary
	 */
	public Boolean isPrimary() {
		return primary;
	}

	/**
	 * @param primary
	 *            설정할 primary
	 */
	public void setPrimary(Boolean primary) {
		this.primary = primary;
	}

	/**
	 * @return profileImageId
	 */
	public Long getProfileImageId() {
		return profileImageId;
	}

	/**
	 * @param profileImageId
	 *            설정할 profileImageId
	 */
	public void setProfileImageId(Long profilePhotoId) {
		this.profileImageId = profilePhotoId;
	}

	/**
	 * @return userId
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            설정할 userId
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * @return imageContentType
	 */
	public String getImageContentType() {
		return imageContentType;
	}

	/**
	 * @param imageContentType
	 *            설정할 imageContentType
	 */
	public void setImageContentType(String imageContentType) {
		this.imageContentType = imageContentType;
	}

	/**
	 * @return imageSize
	 */
	public Integer getImageSize() {
		return imageSize;
	}

	/**
	 * @param imageSize
	 *            설정할 imageSize
	 */
	public void setImageSize(Integer imageSize) {
		this.imageSize = imageSize;
	}

	/**
	 * @return thumbnailContentType
	 */
	public String getThumbnailContentType() {
		return thumbnailContentType;
	}

	/**
	 * @param thumbnailContentType
	 *            설정할 thumbnailContentType
	 */
	public void setThumbnailContentType(String thumbnailContentType) {
		this.thumbnailContentType = thumbnailContentType;
	}

	/**
	 * @return creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate 설정할 creationDate
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return modifiedDate
	 */
	public Date getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * @param modifiedDate 설정할 modifiedDate
	 */
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/**
	 * @return thumbnailSize
	 */
	public Integer getThumbnailSize() {
		return thumbnailSize;
	}

	/**
	 * @param thumbnailSize
	 *            설정할 thumbnailSize
	 */
	public void setThumbnailSize(Integer thumbnailSize) {
		this.thumbnailSize = thumbnailSize;
	}

	/**
	 * @return filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename 설정할 filename
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	public int getCachedSize() {
		return CacheSizes.sizeOfBoolean() + CacheSizes.sizeOfLong()
				+ CacheSizes.sizeOfLong() + CacheSizes.sizeOfInt()
				+ CacheSizes.sizeOfInt()
				+ CacheSizes.sizeOfString(imageContentType)
				+ CacheSizes.sizeOfString(imageContentType);
	}

}
