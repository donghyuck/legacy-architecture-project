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

import java.util.Date;

import architecture.common.cache.Cacheable;

public interface Timeline  extends Cacheable {

	public long getTimelineId();
	
	public int getObjectType();
	
	public long getObjectId();
	
	public String getHeadline();
	
	public String getBody();
	
	public Media getMedia();
	
	public Date getStartDate();
	
	public Date getEndDate();
	
	public boolean hasMedia();
	
	public void setObjectType(int objectType);
	
	public void setObjectId(long objectId);
	
	public void setHeadline(String headline);
	
	public void setBody(String body);
	
	public void setMedia(Media media);
	
	public void setStartDate(Date startDate);
	
	public void setEndDate(Date endDate);
}
