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
package architecture.ee.web.community.tag;

import java.util.Date;

import architecture.common.cache.CacheSizes;
import architecture.common.cache.Cacheable;

public class DefaultContentTag implements ContentTag, Cacheable {
    private long id;
    private String name;
    private Date creationDate;
    private String filteredName;

    public DefaultContentTag() {

    }

    /**
     * @param tagId
     * @param name
     * @param creationDate
     */
    public DefaultContentTag(long tagId, String name, Date creationDate) {
	this.id = tagId;
	this.name = name;
	this.creationDate = creationDate;
    }

    /**
     * @return id
     */
    public long getTagId() {
	return id;
    }

    /**
     * @param id
     *            설정할 id
     */
    public void setTagId(long id) {
	this.id = id;
    }

    public String getUnfilteredName() {
	return name;
    }

    /**
     * @return name
     */
    public String getName() {
	return name;
    }

    /**
     * @param name
     *            설정할 name
     */
    public void setName(String name) {
	this.name = name;
    }

    /**
     * @return creationDate
     */
    public Date getCreationDate() {
	return creationDate;
    }

    /**
     * @param creationDate
     *            설정할 creationDate
     */
    public void setCreationDate(Date creationDate) {
	this.creationDate = creationDate;
    }

    /**
     * @return filteredName
     */
    public String getFilteredName() {
	return filteredName;
    }

    /**
     * @param filteredName
     *            설정할 filteredName
     */
    public void setFilteredName(String filteredName) {
	this.filteredName = filteredName;
    }

    public int getCachedSize() {
	int size = CacheSizes.sizeOfObject();
	size += CacheSizes.sizeOfLong();
	size += CacheSizes.sizeOfString(name);
	size += CacheSizes.sizeOfString(filteredName);
	size += CacheSizes.sizeOfDate();
	return size;
    }

    public String toString() {
	return (new StringBuilder()).append("[").append(id).append("] ").append(name).toString();
    }

}
