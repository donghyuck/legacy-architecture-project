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
package architecture.ee.web.struts2.action.admin.ajax;

import java.util.ArrayList;
import java.util.List;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Statistics;
import net.sf.ehcache.Status;

import org.apache.commons.lang.StringUtils;

import architecture.ee.component.admin.AdminHelper;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;

public class ViewCacheAction extends FrameworkActionSupport {

	private String targetName;
	
	public ViewCacheAction() {
	}

	/**
	 * @return targetName
	 */
	public String getTargetName() {
		return targetName;
	}


	/**
	 * @param targetName 설정할 targetName
	 */
	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}


	public net.sf.ehcache.Cache getTargetCache(){
		return AdminHelper.getCacheManager().getCache(targetName);
	}
	
	
	public String[] getCacheNames () {		
		return AdminHelper.getCacheManager().getCacheNames();
	}
	
	public CacheStats getTargetCacheStats(){
		return new CacheStats(getTargetCache());
	}
	
	public List<CacheStats> getAllCacheStats () {		
		String[] names = getCacheNames();
		List<CacheStats> infos = new ArrayList<CacheStats>(names.length);
		for(String name : names){
			infos.add(new CacheStats(AdminHelper.getCacheManager().getCache(name)));			
		}
		return infos;
	}
	
    public String execute() throws Exception {  
        return success();
    }    

    public String clearCache() throws Exception {  
    	
    	if( StringUtils.isNotEmpty(targetName) && this.getTargetCache()!=null){
    		getTargetCache().removeAll();
    	}    	
        return success();
    }    

	public static class CacheStats {

		private final String cacheName;
		private final int statisticsAccuracy;
		private final long cacheHits;
		private final long onDiskHits;
		private final long offHeapHits;
		private final long inMemoryHits;
		private final long misses;
		private final long onDiskMisses;
		private final long offHeapMisses;
		private final long inMemoryMisses;
		private final long size;
		private final long memoryStoreSize;
		private final long offHeapStoreSize;
		private final long diskStoreSize;
		private final float averageGetTime;
		private final long evictionCount;
		private final long searchesPerSecond;
		private final long averageSearchTime;
		private long writerQueueLength;
		private long maxEntriesLocalHeap = 0;
		private long maxEntriesLocalDisk = 0;
		private boolean diskPersistent = false;
		public CacheStats(String cacheName, int statisticsAccuracy,
				long cacheHits, long onDiskHits, long offHeapHits,
				long inMemoryHits, long misses, long onDiskMisses,
				long offHeapMisses, long inMemoryMisses, long size,
				float averageGetTime, long evictionCount, long memoryStoreSize,
				long offHeapStoreSize, long diskStoreSize,
				long searchesPerSecond, long averageSearchTime,
				long writerQueueLength) {
			this.cacheName = cacheName;
			this.statisticsAccuracy = statisticsAccuracy;
			this.cacheHits = cacheHits;
			this.onDiskHits = onDiskHits;
			this.offHeapHits = offHeapHits;
			this.inMemoryHits = inMemoryHits;
			this.misses = misses;
			this.onDiskMisses = onDiskMisses;
			this.offHeapMisses = offHeapMisses;
			this.inMemoryMisses = inMemoryMisses;
			this.size = size;
			this.averageGetTime = averageGetTime;
			this.evictionCount = evictionCount;
			this.memoryStoreSize = memoryStoreSize;
			this.offHeapStoreSize = offHeapStoreSize;
			this.diskStoreSize = diskStoreSize;
			this.searchesPerSecond = searchesPerSecond;
			this.averageSearchTime = averageSearchTime;
			this.writerQueueLength = writerQueueLength;
		}

		public CacheStats(net.sf.ehcache.Cache cache) {
			this.maxEntriesLocalHeap = cache.getCacheConfiguration().getMaxEntriesLocalHeap();			
			this.maxEntriesLocalDisk = cache.getCacheConfiguration().getMaxEntriesLocalDisk();		
			this.diskPersistent = cache.getCacheConfiguration().isDiskPersistent();
			this.cacheName = cache.getName();			
			this.statisticsAccuracy = cache.getLiveCacheStatistics().getStatisticsAccuracy();			
			this.cacheHits = cache.getLiveCacheStatistics().getCacheHitCount();
			this.onDiskHits = cache.getLiveCacheStatistics().getOnDiskHitCount();
			this.offHeapHits = cache.getLiveCacheStatistics().getOffHeapHitCount(); 
			this.inMemoryHits = cache.getLiveCacheStatistics().getInMemoryHitCount();
			this.misses = cache.getLiveCacheStatistics().getCacheMissCount();
			this.onDiskMisses = cache.getLiveCacheStatistics().getOnDiskMissCount(); 
			this.offHeapMisses = cache.getLiveCacheStatistics().getOffHeapMissCount(); 
			this.inMemoryMisses = cache.getLiveCacheStatistics().getInMemoryMissCount(); 
			this.size = cache.getSizeBasedOnAccuracy(cache.getLiveCacheStatistics().getStatisticsAccuracy());
			this.averageGetTime = cache.getAverageGetTime();
			this.evictionCount = cache.getLiveCacheStatistics().getEvictedCount();
			this.memoryStoreSize = cache.getMemoryStoreSize();
			this.offHeapStoreSize = cache.getOffHeapStoreSize();
			this.diskStoreSize = cache.getDiskStoreSize();
			this.searchesPerSecond = cache.getSearchesPerSecond(); 
			this.averageSearchTime = cache.getAverageSearchTime(); 
			this.writerQueueLength = cache.getLiveCacheStatistics().getWriterQueueLength();
		}

		/**
		 * @return diskPersistent
		 */
		public boolean isDiskPersistent() {
			return diskPersistent;
		}

		/**
		 * @param diskPersistent 설정할 diskPersistent
		 */
		public void setDiskPersistent(boolean diskPersistent) {
			this.diskPersistent = diskPersistent;
		}

		/**
		 * @return maxEntriesLocalHeap
		 */
		public long getMaxEntriesLocalHeap() {
			return maxEntriesLocalHeap;
		}

		/**
		 * @param maxEntriesLocalHeap 설정할 maxEntriesLocalHeap
		 */
		public void setMaxEntriesLocalHeap(long maxEntriesLocalHeap) {
			this.maxEntriesLocalHeap = maxEntriesLocalHeap;
		}

		/**
		 * @return maxEntriesLocalDisk
		 */
		public long getMaxEntriesLocalDisk() {
			return maxEntriesLocalDisk;
		}

		/**
		 * @param maxEntriesLocalDisk 설정할 maxEntriesLocalDisk
		 */
		public void setMaxEntriesLocalDisk(long maxEntriesLocalDisk) {
			this.maxEntriesLocalDisk = maxEntriesLocalDisk;
		}

		/**
		 * @return writerQueueLength
		 */
		public long getWriterQueueLength() {
			return writerQueueLength;
		}

		/**
		 * @param writerQueueLength 설정할 writerQueueLength
		 */
		public void setWriterQueueLength(long writerQueueLength) {
			this.writerQueueLength = writerQueueLength;
		}

		/**
		 * @return cacheName
		 */
		public String getCacheName() {
			return cacheName;
		}

		/**
		 * @return statisticsAccuracy
		 */
		public int getStatisticsAccuracy() {
			return statisticsAccuracy;
		}

		/**
		 * @return cacheHits
		 */
		public long getCacheHits() {
			return cacheHits;
		}

		/**
		 * @return onDiskHits
		 */
		public long getOnDiskHits() {
			return onDiskHits;
		}

		/**
		 * @return offHeapHits
		 */
		public long getOffHeapHits() {
			return offHeapHits;
		}

		/**
		 * @return inMemoryHits
		 */
		public long getInMemoryHits() {
			return inMemoryHits;
		}

		/**
		 * @return misses
		 */
		public long getMisses() {
			return misses;
		}

		/**
		 * @return onDiskMisses
		 */
		public long getOnDiskMisses() {
			return onDiskMisses;
		}

		/**
		 * @return offHeapMisses
		 */
		public long getOffHeapMisses() {
			return offHeapMisses;
		}

		/**
		 * @return inMemoryMisses
		 */
		public long getInMemoryMisses() {
			return inMemoryMisses;
		}

		/**
		 * @return size
		 */
		public long getSize() {
			return size;
		}

		/**
		 * @return memoryStoreSize
		 */
		public long getMemoryStoreSize() {
			return memoryStoreSize;
		}

		/**
		 * @return offHeapStoreSize
		 */
		public long getOffHeapStoreSize() {
			return offHeapStoreSize;
		}

		/**
		 * @return diskStoreSize
		 */
		public long getDiskStoreSize() {
			return diskStoreSize;
		}

		/**
		 * @return averageGetTime
		 */
		public float getAverageGetTime() {
			return averageGetTime;
		}

		/**
		 * @return evictionCount
		 */
		public long getEvictionCount() {
			return evictionCount;
		}

		/**
		 * @return searchesPerSecond
		 */
		public long getSearchesPerSecond() {
			return searchesPerSecond;
		}

		/**
		 * @return averageSearchTime
		 */
		public long getAverageSearchTime() {
			return averageSearchTime;
		}		
		
		
        
	}
	
}
