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
package architecture.ee.web.community.streams.impl;

import java.util.Date;

import architecture.common.user.User;
import architecture.ee.web.community.streams.Photo;

public class PhotoImpl implements Photo {

	private Date creationDate = null;
	
	private Date modifiedDate = null;
	
	private long creatorId;
	
	private long imageId;
	
	private String externalId ;
	
	private boolean publicShared;
		
	private User creator;
	
	
	
	/**
	 * @param creatorId
	 * @param imageId
	 * @param publicShared
	 * @param creator
	 */
	public PhotoImpl(String externalId, long imageId, boolean publicShared, User creator) {
		this.externalId = externalId;
		this.imageId = imageId;
		this.publicShared = publicShared;
		this.creator = creator;
		this.creatorId = creator.getUserId();
	}

	public PhotoImpl(String externalId, long imageId, boolean publicShared, long creatorId) {
		this.externalId = externalId;
		this.imageId = imageId;
		this.publicShared = publicShared;
		this.creatorId = creatorId ;
	}

	
	/**
	 * @return creator
	 */
	public User getCreator() {
		return creator;
	}

	/**
	 * @param creator 설정할 creator
	 */
	public void setCreator(User creator) {
		this.creator = creator;
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
	 * @return creatorId
	 */
	public long getCreatorId() {
		return creatorId;
	}

	/**
	 * @param creatorId 설정할 creatorId
	 */
	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}

	/**
	 * @return imageId
	 */
	public long getImageId() {
		return imageId;
	}

	/**
	 * @param imageId 설정할 imageId
	 */
	public void setImageId(long imageId) {
		this.imageId = imageId;
	}

	/**
	 * @return externalId
	 */
	public String getExternalId() {
		return externalId;
	}

	/**
	 * @param externalId 설정할 externalId
	 */
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	/**
	 * @return publicShared
	 */
	public boolean isPublicShared() {
		return publicShared;
	}

	/**
	 * @param publicShared 설정할 publicShared
	 */
	public void setPublicShared(boolean publicShared) {
		this.publicShared = publicShared;
	}


	public int getCachedSize() {
		return 0;
	}

}
