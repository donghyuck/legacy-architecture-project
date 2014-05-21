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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import architecture.common.user.Company;
import architecture.common.user.User;
import architecture.ee.web.community.timeline.dao.TimelineDao;
import architecture.ee.web.site.WebSite;
public class DefaultTimelineManager implements TimelineManager{

	private TimelineDao timelineDao;
	private Cache timelineCache;
	
	public DefaultTimelineManager() {
		
	}

	/**
	 * @return timelineDao
	 */
	public TimelineDao getTimelineDao() {
		return timelineDao;
	}

	/**
	 * @param timelineDao 설정할 timelineDao
	 */
	public void setTimelineDao(TimelineDao timelineDao) {
		this.timelineDao = timelineDao;
	}

	/**
	 * @return timelineCache
	 */
	public Cache getTimelineCache() {
		return timelineCache;
	}

	/**
	 * @param timelineCache 설정할 timelineCache
	 */
	public void setTimelineCache(Cache timelineCache) {
		this.timelineCache = timelineCache;
	}

	public Timeline getTimelineById(long timelineId) throws TimelineNotFoundException {
		Timeline timelineToUse = null;
		if( timelineCache.get(timelineId) != null ){
			timelineToUse = (Timeline)timelineCache.get(timelineId).getValue();
		}else{
			timelineToUse = timelineDao.getTimelineById(timelineId);
			timelineCache.put(new Element(timelineToUse.getTimelineId(), timelineToUse));
		}
		return timelineToUse;
	}

	public int getTimelineCount(int objectType, long objectId) {
		if( objectType < 1 || objectId < 1)
			return 0;
		return timelineDao.getTimelineCount(objectType, objectId);
	}

	public List<Timeline> getTimelines(int objectType, long objectId) {
		if( objectType < 1 || objectId < 1)
			return Collections.EMPTY_LIST;
		List<Long> ids = timelineDao.getTimelineIds(objectType, objectId);
		List<Timeline> list = new ArrayList<Timeline>(ids.size());
		for( Long id : ids ){
			try {
				list.add(getTimelineById(id));
			} catch (TimelineNotFoundException e) {
			}
		}
		return list;
	}

	public List<Timeline> getTimelines(WebSite webSite) {
		int objectType = webSite.getModelObjectType();
		long objectId = webSite.getWebSiteId();		
		return getTimelines(objectType, objectId);
	}

	public List<Timeline> getTimelines(Company company) {
		int objectType = company.getModelObjectType();
		long objectId = company.getCompanyId();		
		return getTimelines(objectType, objectId);
	}

	public List<Timeline> getTimelines(User user) {
		int objectType = user.getModelObjectType();
		long objectId = user.getUserId();		
		return getTimelines(objectType, objectId);
	}

	public int getTimelineCount(WebSite webSite) {
		int objectType = webSite.getModelObjectType();
		long objectId = webSite.getWebSiteId();		
		return getTimelineCount(objectType, objectId);
	}

	public int getTimelineCount(Company company) {
		int objectType = company.getModelObjectType();
		long objectId = company.getCompanyId();		
		return getTimelineCount(objectType, objectId);
	}

	public int getTimelineCount(User user) {
		int objectType = user.getModelObjectType();
		long objectId = user.getUserId();		
		return getTimelineCount(objectType, objectId);
	}

	public void updateTimeline(Timeline timeline) {
		if( timeline.getTimelineId() > 0 ){
			timelineCache.remove(timeline.getTimelineId());
		}
		timelineDao.updateTimeline(timeline);		
	}

	public void deleteTimeline(Timeline timeline) {
		if( timeline.getTimelineId() > 0 ){
			timelineCache.remove(timeline.getTimelineId());
		}
		timelineDao.deleteTimeline(timeline);		
	}
	
	

}
