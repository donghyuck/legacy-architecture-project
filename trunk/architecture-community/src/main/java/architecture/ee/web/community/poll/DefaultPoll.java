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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import architecture.common.cache.CacheSizes;
import architecture.common.model.json.CustomJsonDateDeserializer;
import architecture.common.model.json.CustomJsonDateSerializer;
import architecture.common.model.json.UserDeserializer;
import architecture.common.user.User;
import architecture.common.user.UserTemplate;
import architecture.ee.web.community.model.ContentObject.Status;

public class DefaultPoll implements Poll {

	private static final Log log = LogFactory.getLog(DefaultPoll.class);
	
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

	private List<PollOption> options;

	private Status status;

	
	public DefaultPoll() {
		this.pollId = -1L;
		this.objectType = -1;
		this.objectId = -1L;
		this.user = new UserTemplate(-1L);
		this.name = null;
		this.description = null;
		this.mode = 0L;
		this.commentStatus = 2;
		this.status = Status.PUBLISHED;
		this.options = new ArrayList<PollOption>();
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
		this.mode = 0L;
		this.commentStatus = 2;
		this.status = Status.PUBLISHED;
		this.options =  new ArrayList<PollOption>();
		
		if(name == null)
			throw new IllegalArgumentException("Name cannot be null");
		
		Calendar cal = Calendar.getInstance();
		Date now = cal.getTime();
		
		this.creationDate = now;
		this.modifiedDate = now;
		this.startDate = now;
		
		cal.add(Calendar.YEAR, 1);
		this.endDate =  cal.getTime();
		this.expireDate = endDate;		
	}

	
	
	@JsonIgnore
	public Serializable getPrimaryKeyObject() {
		return pollId;
	}

	@JsonIgnore
	public int getModelObjectType() {
		return 40;
	}

	@JsonIgnore
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
	
	@JsonIgnore
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
	@JsonDeserialize(using = UserDeserializer.class)
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
		if( name == null)
			throw new IllegalArgumentException("name can not be null.");
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
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate 설정할 creationDate
	 */
	
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return modifiedDate
	 */
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	public Date getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * @param modifiedDate 설정할 modifiedDate
	 */
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/**
	 * @return startDate
	 */
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate 설정할 startDate
	 */
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	public void setStartDate(Date startDate) {
		if( startDate == null || this.endDate != null && startDate.compareTo(this.endDate) > 0)
			throw new IllegalArgumentException("Start date can not be null or greater than endDate.");
		this.startDate = startDate;
	}

	/**
	 * @return endDate
	 */
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate 설정할 endDate
	 */
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	public void setEndDate(Date endDate) {
		if( endDate == null || this.startDate != null && this.startDate.compareTo(endDate) > 0)
			throw new IllegalArgumentException("End date can not be null or less than startDate.");
		
		this.endDate = endDate;
	}

	/**
	 * @return expireDate
	 */
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	public Date getExpireDate() {
		return expireDate;
	}

	/**
	 * @param expireDate 설정할 expireDate
	 */
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	public void setExpireDate(Date expireDate) {
		if( expireDate == null || endDate != null && endDate.compareTo(expireDate)>0)
			throw new IllegalArgumentException("Expire date can not be null or less than end Date.");
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
	@JsonIgnore
	public int getCommentStatus() {
		return commentStatus;
	}

	/**
	 * @param commentStatus 설정할 commentStatus
	 */
	@JsonIgnore
	public void setCommentStatus(int commentStatus) {
		this.commentStatus = commentStatus;
	}

	
	public int getOptionCount(){
		return options.size();
	}
	/**
	 * @return options
	 */
	public List<PollOption> getOptions() {
		return options;
	}

	/**
	 * @param options 설정할 options
	 */
	
	public void setOptions(List<PollOption> options) {
		this.options = options;
	}
	
	public void addOption(PollOption option){
		this.options.add(option);
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