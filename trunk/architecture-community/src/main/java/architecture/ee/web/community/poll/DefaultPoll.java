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
package architecture.ee.web.community.poll;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.common.cache.CacheSizes;
import architecture.common.user.User;
import architecture.common.user.UserTemplate;
import architecture.ee.web.community.model.ContentObject.Status;

public class DefaultPoll implements Poll {

	private long pollId;

	private int objectType;

	private long objectId;

	private User user;

	private String name;

	private String description;

	private Date creationDate;

	private Date modifiedDate;

	private Date startDate;

	private Date endDate;

	private Date expireDate;

	private long mode;

	private int commentStatus;

	private List options;

	private Status status;

	private static final Log log = LogFactory.getLog(DefaultPoll.class);
	
	public DefaultPoll() {
		pollId = -1L;
		objectType = -1;
		objectId = -1L;
		user = new UserTemplate(-1L);
		name = null;
		description = null;
		mode = 0L;
		commentStatus = 2;
		status = Status.PUBLISHED;
		options = new ArrayList();
	}

	/**
	 * @param pollId
	 */
	protected DefaultPoll(long pollId) {
		this();
		this.pollId = pollId;
	}
		

	/**
	 * @param objectType
	 * @param objectId
	 * @param user
	 * @param name
	 */
	protected DefaultPoll(int objectType, long objectId, User user, String name) {		
		this();		
		this.objectType = objectType;
		this.objectId = objectId;
		this.user = user;
		this.name = name;
		if(name == null)
            throw new IllegalArgumentException("Name cannot be null");		
		Calendar cal = Calendar.getInstance();
		Date now = cal.getTime();
		cal.add(Calendar.YEAR, 1);
		this.creationDate = now;
		this.modifiedDate = now;
		this.endDate =  cal.getTime();
		this.expireDate = endDate;		
	}

	
	
	@Override
	public Serializable getPrimaryKeyObject() {
		return pollId;
	}

	@Override
	public int getModelObjectType() {
		return 29;
	}

	@Override
	public int getCachedSize() {
        int size = CacheSizes.sizeOfObject();
        size += CacheSizes.sizeOfLong();
        size += CacheSizes.sizeOfLong();
        size += CacheSizes.sizeOfInt();
        size += CacheSizes.sizeOfLong();
        size += CacheSizes.sizeOfString(name);
        size += CacheSizes.sizeOfString(description);
        size += CacheSizes.sizeOfDate() * 5;
        size += CacheSizes.sizeOfLong();
        size += CacheSizes.sizeOfInt();
        size += CacheSizes.sizeOfCollection(options);
        return size;
	}
	
    public String toString()
    {
    	StringBuffer buf = new StringBuffer();
    	buf.append(pollId).append(" (").append(objectType).append(",").append(objectId).append(")");
    	return buf.toString();
    }

	/**
	 * @return pollId
	 */
	public long getPollId() {
		return pollId;
	}

	/**
	 * @param pollId 설정할 pollId
	 */
	public void setPollId(long pollId) {
		this.pollId = pollId;
	}

	/**
	 * @return objectType
	 */
	public int getObjectType() {
		return objectType;
	}

	/**
	 * @param objectType 설정할 objectType
	 */
	public void setObjectType(int objectType) {
		this.objectType = objectType;
	}

	/**
	 * @return objectId
	 */
	public long getObjectId() {
		return objectId;
	}

	/**
	 * @param objectId 설정할 objectId
	 */
	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}

	/**
	 * @return user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user 설정할 user
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name 설정할 name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description 설정할 description
	 */
	public void setDescription(String description) {
		this.description = description;
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
	 * @return startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate 설정할 startDate
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate 설정할 endDate
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return expireDate
	 */
	public Date getExpireDate() {
		return expireDate;
	}

	/**
	 * @param expireDate 설정할 expireDate
	 */
	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	/**
	 * @return mode
	 */
	public long getMode() {
		return mode;
	}

	/**
	 * @param mode 설정할 mode
	 */
	public void setMode(long mode) {
		this.mode = mode;
	}

	public boolean isModeEnabled(long mode) {
		return (this.mode & mode) != 0L;
	}

	public void setMode(long mode, boolean enabled) {
		if (enabled) {
			this.mode = this.mode | mode;
		} else {
			mode = ~mode;
			this.mode = this.mode & mode;
		}
	}
	/**
	 * @return commentStatus
	 */
	public int getCommentStatus() {
		return commentStatus;
	}

	/**
	 * @param commentStatus 설정할 commentStatus
	 */
	public void setCommentStatus(int commentStatus) {
		this.commentStatus = commentStatus;
	}

	/**
	 * @return options
	 */
	public List getOptions() {
		return options;
	}

	/**
	 * @param options 설정할 options
	 */
	public void setOptions(List options) {
		this.options = options;
	}

	/**
	 * @return status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * @param status 설정할 status
	 */
	public void setStatus(Status status) {
		this.status = status;
	}
    
}