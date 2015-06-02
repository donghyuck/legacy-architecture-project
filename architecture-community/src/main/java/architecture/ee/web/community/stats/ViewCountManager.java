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
package architecture.ee.web.community.stats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.common.event.api.EventListener;
import architecture.common.event.api.EventPublisher;
import architecture.common.model.factory.ModelTypeFactory;
import architecture.common.task.TaskEngine;
import architecture.common.user.authentication.UnAuthorizedException;
import architecture.ee.util.ApplicationHelper;
import architecture.ee.web.community.page.Page;
import architecture.ee.web.community.page.event.PageEvent;
import architecture.ee.web.community.stats.dao.ViewCountDao;

public class ViewCountManager {
	
	public static final Long DEFAULT_PERIOD_TIME = 180000L;
	
	private static final Log log = LogFactory.getLog(ViewCountManager.class);
	
	private Lock lock = new ReentrantLock();
	private boolean viewCountsEnabled = false;
    private static Map<String, ViewCountInfo> queue;
    private static PersistenceTask task;
    private Cache pageCountCache;
    private ViewCountDao viewCountDao;
    private EventPublisher eventPublisher;
    
	public ViewCountManager(TaskEngine taskEngine){
		this.viewCountsEnabled = ApplicationHelper.getApplicationBooleanProperty("components.viewCounts.enabled", true);
		log.debug("view count enabled : " +viewCountsEnabled );		
		if( viewCountsEnabled ){
			queue = initQueue();
			task = new PersistenceTask();
			taskEngine.schedule(task, DEFAULT_PERIOD_TIME, DEFAULT_PERIOD_TIME);
		}
	}
	
	public void initialize(){
		this.eventPublisher.register(this);		
	}
	
	
	/**
	 * @return pageCountCache
	 */
	public Cache getPageCountCache() {
		return pageCountCache;
	}


	/**
	 * @param pageCountCache 설정할 pageCountCache
	 */
	public void setPageCountCache(Cache pageCountCache) {
		this.pageCountCache = pageCountCache;
	}


	/**
	 * @return eventPublisher
	 */
	public EventPublisher getEventPublisher() {
		return eventPublisher;
	}


	/**
	 * @param eventPublisher 설정할 eventPublisher
	 */
	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}


	public void addPageCount(Page page){
		if(viewCountsEnabled){			
			addCount(ModelTypeFactory.getTypeIdFromCode("PAGE"), page.getPageId(), -1, pageCountCache, 1);
		}
	}
	
	public int getPageCount(Page page){
		if(viewCountsEnabled){
			return getCachedCount(page.getPageId(), ModelTypeFactory.getTypeIdFromCode("PAGE"), -1L);
		}else{
			return -1;
		}
	}	
	
	public void clearCount(Page page)throws UnAuthorizedException{
		if(viewCountsEnabled){
			String key = getCacheKey(ModelTypeFactory.getTypeIdFromCode("PAGE"), page.getPageId());
			queue.remove(key);
			clearCount(1, page.getPageId());
		}
	}
	
	protected void updateViewCounts(List<ViewCountInfo> views){
		viewCountDao.updateViewCounts(views);
	}
	
	/**
	 * @return viewCountDao
	 */
	public ViewCountDao getViewCountDao() {
		return viewCountDao;
	}

	/**
	 * @param viewCountDao 설정할 viewCountDao
	 */
	public void setViewCountDao(ViewCountDao viewCountDao) {
		this.viewCountDao = viewCountDao;
	}

	private synchronized void clearCount(int objectType, long objectId){		
		viewCountDao.deleteViewCount(objectType, objectId);
	}
	
	
	private void addCount( int objectType, long objectId, long parentObjectId, Cache cache, int amount ){
		
		int count = -1;	
		String cacheKey = getCacheKey(objectType, objectId);
		
		if( cache.get( cacheKey )!= null)
			count = (Integer)cache.get(cacheKey).getValue();
		else
			count = viewCountDao.getViewCount( objectType, objectId , parentObjectId );		
		count += amount;		
		cache.put(new Element(cacheKey, Integer.valueOf(count)));
		Map<String, ViewCountInfo> queueRef = queue;
		synchronized(queueRef){
			queueRef.put(cacheKey, new ViewCountInfo(objectType, objectId, parentObjectId, count));
		}
	}
	
	
	private int getCachedCount(Long objectId, Integer objectType, Long parentObjectId){
		Cache cache ;
		switch(objectType){
		case 31: // page
			cache = pageCountCache;
			break;
		default:
			return -1;
		}
		Integer cachedCount ;
		String cacheKey = getCacheKey(objectType, objectId);
		if( cache.get(getCacheKey(objectType, objectId)) != null ){
			 cachedCount =(Integer) cache.get(cacheKey).getValue();
		}else{
			lock.lock();
			try{
				cachedCount = viewCountDao.getViewCount( objectType, objectId , parentObjectId );
				cache.put(new Element(cacheKey, cachedCount));
			}finally{
				lock.unlock();
			}
		}	
		return cachedCount;
	}
	
	
	@EventListener
	public void onEvent(PageEvent event) {
		log.debug("page event : " + event.getType().name());
		if(viewCountsEnabled){
			Page page = (Page)event.getSource();
			int objectType = ModelTypeFactory.getTypeIdFromCode("PAGE");
			String key = getCacheKey(objectType, page.getPageId());
			if( event.getType() == PageEvent.Type.CREATED ){
				if( pageCountCache.get(key)== null){
					viewCountDao.insertInitialViewCount(objectType, page.getPageId(), -1L, 0);
					pageCountCache.put(new Element( key, Integer.valueOf(0)));
				}
			}else if ( event.getType() == PageEvent.Type.DELETED ){
				queue.remove(key);
				viewCountDao.deleteViewCount(objectType, page.getPageId());
				pageCountCache.remove(key);
			}else{
				
			}
		}
	}	
	
	public void destroy() throws Exception
	{
		eventPublisher.unregister(this);
	}
	
	/**
	 * @return viewCountsEnabled
	 */
	public boolean isViewCountsEnabled() {
		return viewCountsEnabled;
	}

	private static Map initQueue() {
		return Collections.synchronizedMap(new HashMap());
	}

	private static String getCacheKey(int objectType, long objectId) {
		StringBuffer buf = new StringBuffer();
		buf.append(objectType).append(",").append(objectId);
		return buf.toString();
	}
	
	 static class PersistenceTask extends TimerTask
     {
		public void run() {						
			log.debug((new StringBuilder()).append("Starting a save of view counts to the database. Thread: ").append(Thread.currentThread().getName()).toString());			
			Map<String, ViewCountInfo> localQueue = ViewCountManager.queue;			
			log.debug("queue: " + localQueue.size() );			
			ViewCountManager.queue = ViewCountManager.initQueue();			
			if( localQueue.size() > 0 ){
			    // batch update. ..
				// update V2_VIEW_COUNT set view_count = ? where objectType = ? and objectId = ?
				List<ViewCountInfo> list = new ArrayList<ViewCountInfo>(localQueue.values());
				ApplicationHelper.getComponent(ViewCountManager.class).updateViewCounts(list);
				log.debug((new StringBuilder()).append("Saving ").append(localQueue.size()).append(" view counts to the db...").toString());
			}			
		}
     }
     
	public static class ViewCountInfo {
		private int objectType;
		private long objectId;
		private long parentObjectId;
		private int count;

		ViewCountInfo(int objectType, long objectId, long parentObjectId,
				int totalCount) {
			count = 0;
			this.objectType = objectType;
			this.objectId = objectId;
			this.parentObjectId = parentObjectId;
			count = totalCount;
		}

		public int getObjectType() {
			return objectType;
		}

		public long getObjectId() {
			return objectId;
		}

		public long getParentObjectID() {
			return parentObjectId;
		}

		public int getCount() {
			return count;
		}

		public void incrementCount() {
			count++;
		}

		public void incrementCount(int amount) {
			count += amount;
		}

		public String toString() {
			return (new StringBuilder()).append("ViewCountInfo(type: ")
					.append(String.valueOf(objectType)).append(", id: ")
					.append(String.valueOf(objectId)).append(", parent id: ")
					.append(String.valueOf(parentObjectId)).append(", count: ")
					.append(count).append(")").toString();
		}
	}
}
