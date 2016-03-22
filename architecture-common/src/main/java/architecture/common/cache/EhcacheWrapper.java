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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

public class EhcacheWrapper<K, V> implements Cache<K, V> {

    private final Ehcache targetCache;

    public EhcacheWrapper(Ehcache targetCache) {
	this.targetCache = targetCache;
    }

    public int size() {
	return targetCache.getSize();
    }

    public boolean isEmpty() {
	return size() == 0;
    }

    public boolean containsKey(Object key) {
	return targetCache.get(key) != null;
    }

    public boolean containsValue(Object value) {
	for (Object key : targetCache.getKeys()) {
	    Element element = targetCache.get(key);
	    if (element != null && element.getValue() != null && element.getValue().equals(value))
		return true;
	}
	return false;
    }

    public V get(Object key) {
	Element element = targetCache.get(key);
	if (element != null)
	    return (V) element.getValue();
	else
	    return null;
    }

    public V put(K key, V value) {
	V previousValue = get(key);
	targetCache.put(new Element(key, value));
	return previousValue;
    }

    public V remove(Object key) {
	V previousValue = get(key);
	targetCache.remove(key);
	return previousValue;
    }

    public void putAll(Map<? extends K, ? extends V> m) {
	for (Map.Entry<? extends K, ? extends V> entry : m.entrySet())
	    put(entry.getKey(), entry.getValue());
    }

    public void clear() {
	targetCache.removeAll();
    }

    public Set<K> keySet() {
	return new HashSet<K>(targetCache.getKeys());
    }

    public Collection<V> values() {
	final ArrayList<V> values = new ArrayList<V>();
	for (Object key : targetCache.getKeys()) {
	    Element element = targetCache.get(key);
	    if (element != null)
		values.add((V) element.getValue());
	}
	return values;
    }

    public Set<Entry<K, V>> entrySet() {
	final Set<Entry<K, V>> values = new HashSet<Entry<K, V>>();
	for (Object key : targetCache.getKeys()) {
	    Element element = targetCache.get(key);
	    if (element != null)
		values.add(new EhcacheEntry<K, V>((K) key));
	}
	return values;
    }

    /**
     * @author donghyuck
     */
    private class EhcacheEntry<K, V> implements Entry<K, V> {

	private final K key;

	public EhcacheEntry(K key) {
	    this.key = key;
	}

	public K getKey() {
	    return key;
	}

	public V getValue() {
	    Element element = targetCache.get(key);
	    return element != null ? (V) element.getValue() : null;
	}

	public V setValue(V value) {
	    Element element = targetCache.get(key);
	    V previousValue = element != null ? (V) element.getValue() : null;
	    targetCache.put(new Element(key, value));
	    return previousValue;
	}
    }

    public String getName() {
	return targetCache.getName();
    }

    public void setName(String name) {
	targetCache.setName(name);
    }

    public long getMaxCacheSize() {
	return targetCache.getCacheConfiguration().getMaxElementsInMemory();
    }

    public void setMaxCacheSize(int maxSize) {
	targetCache.getCacheConfiguration().setMaxElementsInMemory(maxSize);
    }

    public long getMaxLifetime() {
	return targetCache.getCacheConfiguration().getTimeToLiveSeconds();
    }

    public void setMaxLifetime(long maxLifetime) {
	targetCache.getCacheConfiguration().setTimeToLiveSeconds(maxLifetime);
    }

    public int getCacheSize() {
	return targetCache.getSize();
    }

    public long getCacheHits() {
	return targetCache.getStatistics().getCacheHits();
    }

    public long getCacheMisses() {
	return targetCache.getStatistics().getCacheMisses();
    }
}