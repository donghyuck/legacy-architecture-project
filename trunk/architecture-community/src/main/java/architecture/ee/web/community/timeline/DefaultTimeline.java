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
package architecture.ee.web.community.timeline;

import java.util.Calendar;
import java.util.Date;

import architecture.common.cache.CacheSizes;

public class DefaultTimeline implements Timeline {

	private long timelineId;
	
	private int objectType;
	
	private long objectId;
	
	private String headline;
	
	private String body;
	
	private Media media;

	private Date startDate ;
	
	private Date endDate;
	
	public DefaultTimeline() {
		this.timelineId = -1L;
		this.objectType = 0;
		this.objectId = -1L;
		this.headline = null;
		this.body = null;
		this.media = null;
		Date now = Calendar.getInstance().getTime();
		this.startDate = now;
		this.endDate = now;
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
	 * @return timelineId
	 */
	public long getTimelineId() {
		return timelineId;
	}

	/**
	 * @param timelineId 설정할 timelineId
	 */
	public void setTimelineId(long timelineId) {
		this.timelineId = timelineId;
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
	 * @return headline
	 */
	public String getHeadline() {
		return headline;
	}

	/**
	 * @param headline 설정할 headline
	 */
	public void setHeadline(String headline) {
		this.headline = headline;
	}

	/**
	 * @return body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param body 설정할 body
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * @return media
	 */
	public Media getMedia() {
		return media;
	}

	/**
	 * @param media 설정할 media
	 */
	public void setMedia(Media media) {
		this.media = media;
	}

	public boolean hasMedia() {
		if( this.media == null)
			return false;
		return true;
	}


	public int getCachedSize() {
		return CacheSizes.sizeOfLong() + CacheSizes.sizeOfInt()
				+ CacheSizes.sizeOfLong()
				+ CacheSizes.sizeOfString(this.headline)
				+ CacheSizes.sizeOfString(this.body) + CacheSizes.sizeOfDate()
				+ CacheSizes.sizeOfDate() 
				+ ( this.hasMedia() ? this.media.getCachedSize() : 0 );
	}
	
}
