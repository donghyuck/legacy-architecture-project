/*
 * Copyright 2012 Donghyuck, Son
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
package architecture.common.cache;

/**
 * @author   donghyuck, son
 */
public interface Cache<K,V> extends java.util.Map<K,V> {

    /**
	 * Returns the name of the cache.
	 * @return   the name of the cache.
	 */
    String getName();

    /**
	 * Sets the name of the cache
	 * @param name   the name of the cache
	 */
    void setName(String name);

    /**
     * Returns the maximum size of the cache in bytes. If the cache grows larger
     * than the max size, the least frequently used items will be removed. If
     * the max cache size is set to -1, there is no size limit.
     *
     * @return the maximum size of the cache in bytes.
     */
    long getMaxCacheSize();

    /**
     * Sets the maximum size of the cache in bytes. If the cache grows larger
     * than the max size, the least frequently used items will be removed. If
     * the max cache size is set to -1, there is no size limit.
     *
     * @param maxSize the maximum size of the cache in bytes.
     */
    void setMaxCacheSize(int maxSize);

    /**
	 * Returns the maximum number of milliseconds that any object can live in cache. Once the specified number of milliseconds passes, the object will be automatically expried from cache. If the max lifetime is set to -1, then objects never expire.
	 * @return   the maximum number of milliseconds before objects are expired.
	 */
    long getMaxLifetime();

    /**
	 * Sets the maximum number of milliseconds that any object can live in cache. Once the specified number of milliseconds passes, the object will be automatically expried from cache. If the max lifetime is set to -1, then objects never expire.
	 * @param maxLifetime   the maximum number of milliseconds before objects are expired.
	 */
    void setMaxLifetime(long maxLifetime);

    /**
     * Returns the size of the cache contents in bytes. This value is only a
     * rough approximation, so cache users should expect that actual VM
     * memory used by the cache could be significantly higher than the value
     * reported by this method.
     *
     * @return the size of the cache contents in bytes.
     */
    int getCacheSize();

    /**
     * Returns the number of cache hits. A cache hit occurs every
     * time the get method is called and the cache contains the requested
     * object.<p>
     *
     * Keeping track of cache hits and misses lets one measure how efficient
     * the cache is; the higher the percentage of hits, the more efficient.
     *
     * @return the number of cache hits.
     */
    long getCacheHits();

    /**
     * Returns the number of cache misses. A cache miss occurs every
     * time the get method is called and the cache does not contain the
     * requested object.<p>
     *
     * Keeping track of cache hits and misses lets one measure how efficient
     * the cache is; the higher the percentage of hits, the more efficient.
     *
     * @return the number of cache hits.
     */
    long getCacheMisses();

}