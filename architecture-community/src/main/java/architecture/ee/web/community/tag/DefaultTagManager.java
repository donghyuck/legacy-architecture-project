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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.common.event.api.EventListener;
import architecture.common.event.api.EventPublisher;
import architecture.common.user.authentication.UnAuthorizedException;
import architecture.common.util.LockUtils;
import architecture.ee.web.community.page.PageManager;
import architecture.ee.web.community.page.event.PageEvent;
import architecture.ee.web.community.tag.dao.TagDao;

import com.google.common.collect.ImmutableList;

public class DefaultTagManager implements TagManager {
	
	private Log log = LogFactory.getLog(getClass());
    private TagDao tagDao;
    private Cache tagIdCache;
    private Cache tagCache;
    private Cache tagContentCache;
    private PageManager pageManager;
    private TagSetManager tagSetManager;
    private EventPublisher eventPublisher;
    private final TagManagerHelper tagManagerHelper = new TagManagerHelper(this);
    
	public DefaultTagManager() {
		// TODO 자동 생성된 생성자 스텁
	}


	/**
	 * @return tagDao
	 */
	public TagDao getTagDao() {
		return tagDao;
	}


	/**
	 * @param tagDao 설정할 tagDao
	 */
	public void setTagDao(TagDao tagDao) {
		this.tagDao = tagDao;
	}


	/**
	 * @return tagIdCache
	 */
	public Cache getTagIdCache() {
		return tagIdCache;
	}


	/**
	 * @param tagIdCache 설정할 tagIdCache
	 */
	public void setTagIdCache(Cache tagIdCache) {
		this.tagIdCache = tagIdCache;
	}


	/**
	 * @return tagCache
	 */
	public Cache getTagCache() {
		return tagCache;
	}


	/**
	 * @param tagCache 설정할 tagCache
	 */
	public void setTagCache(Cache tagCache) {
		this.tagCache = tagCache;
	}


	/**
	 * @return tagContentCache
	 */
	public Cache getTagContentCache() {
		return tagContentCache;
	}


	/**
	 * @param tagContentCache 설정할 tagContentCache
	 */
	public void setTagContentCache(Cache tagContentCache) {
		this.tagContentCache = tagContentCache;
	}


	/**
	 * @return pageManager
	 */
	public PageManager getPageManager() {
		return pageManager;
	}


	/**
	 * @param pageManager 설정할 pageManager
	 */
	public void setPageManager(PageManager pageManager) {
		this.pageManager = pageManager;
	}


	/**
	 * @return tagSetManager
	 */
	public TagSetManager getTagSetManager() {
		return tagSetManager;
	}


	/**
	 * @param tagSetManager 설정할 tagSetManager
	 */
	public void setTagSetManager(TagSetManager tagSetManager) {
		this.tagSetManager = tagSetManager;
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

	public void initialize(){
		this.eventPublisher.register(this);		
	}
	 public void destroy() {
		 this.eventPublisher.unregister(this);
	 }
	 
	 
	@Override
	public ContentTag createTag(String name) {		
		try {
			return getTag(name);
		} catch (TagNotFoundException e) {
			DefaultContentTag newTag = new DefaultContentTag(-1L, name.toLowerCase(), new Date());
			tagDao.createContentTag(newTag);
			if( newTag.getTagId()>0){
				tagCache.put(new Element(Long.valueOf(newTag.getTagId()), newTag));
				tagIdCache.put(new Element(newTag.getName().toLowerCase(), Long.valueOf(newTag.getTagId())));
			}
			// fire event;
			return newTag;
		}		
	}


	@Override
	public ContentTag getTag(String name) throws TagNotFoundException {
		if( StringUtils.isEmpty(name)){
			throw new TagNotFoundException("Tag with null value is not valid.");		
		}
		return getTag(getTagId(name));
	}

	private long getTagId(String name) throws TagNotFoundException {		
		if( tagIdCache.get(name) != null){
			Long tagId = (Long)tagIdCache.get(name).getValue();			
			return tagId;
		}else{
			ContentTag tag = tagDao.getContentTagByName(name);
			if( tag == null)
			{	
				throw new TagNotFoundException(new StringBuilder().append("No tag with name '").append(name).append("' exists.").toString());
			}else{
				tagCache.put(new Element(Long.valueOf(tag.getTagId()), tag));
				tagIdCache.put(new Element(tag.getName().toLowerCase(), Long.valueOf(tag.getTagId())));
				return tag.getTagId();
			}
		}		
	}

	@Override
	public ContentTag getTag(long tagId) throws TagNotFoundException {
		ContentTag tag;
		if( tagCache.get(tagId) != null){
			tag = (ContentTag)tagCache.get(tagId).getValue();
		}else{
			tag = tagDao.getContentTagById(tagId);
			log.debug(tag);
			if( tag == null )
				throw new TagNotFoundException();
			tagCache.put(new Element(Long.valueOf(tag.getTagId()), tag));
			tagIdCache.put(new Element(tag.getName().toLowerCase(), Long.valueOf(tag.getTagId())));			
		}
		return tag;
	}


	@Override
	public void addTag(ContentTag tag, int objectType, long objectId)
			throws UnAuthorizedException {
		if( objectType < 0 || objectId < 0L)
			throw new IllegalStateException();
		synchronized(getLock(objectType, objectId)){
			List<Long> tags = getTagIds(objectType, objectId);
			int index = tags.indexOf(Long.valueOf(tag.getTagId()));
			if(index < 0){
				tags.add(Long.valueOf(tag.getTagId()));
				tagContentCache.put(new Element(getCacheKey(objectType, objectId), tags));
			}
		}
		tagDao.addTag(tag.getTagId(), objectType, objectId);
		// event.. fire
		
	}

	private Object getLock(int objectType, long objectId) {
		return LockUtils.intern((new StringBuilder()).append("tagmgr-")
				.append(objectType).append(",").append(objectId).toString());
	}

	public String getCacheKey(int objectType, long objectId) {
		return LockUtils.intern((new StringBuilder()).append("t-")
				.append(objectType).append("-").append(objectId).toString());
	}

    private List<Long> getTagIds(int objectType, long objectId){
    	List<Long> tagIds;
    	if( objectType < 0 || objectId < 0L)
    		tagIds = Collections.emptyList();
		synchronized(getLock(objectType, objectId)){
			String cacheKey = getCacheKey(objectType, objectId);
			if(tagContentCache.get(cacheKey) != null ){
				tagIds = (List<Long>) tagContentCache.get(cacheKey).getValue();
			}else{
				tagIds = tagDao.getTagIds(objectType, objectId);
				tagContentCache.put(new Element(cacheKey, tagIds));				
			}
		}
		return tagIds;
    }
    
    
	@Override
	public void setTags(String tags, int objectType, long objectId) {
		tagManagerHelper.setTags(tags, objectType, objectId );
	}


	@Override
	public List<ContentTag> getTags(int objectType, long objectId) {
		List<Long> tagIds = getTagIds(objectType, objectId);
		 List<ContentTag> tags = new ArrayList<ContentTag>(tagIds.size());

		 for( Long tagId : tagIds ){
			 log.debug("tag:" + tagId);	
			try {
				tags.add( getTag(tagId) );
			} catch (TagNotFoundException e) {
				log.error(e);
			}
		}		
		return tags;
	}


	@Override
	public String getTagsAsString(int objectType, long objectId) {
		return tagManagerHelper.getTagsAsString(objectType, objectId);
	}


	@Override
	public int getTagCount(int objectType, long objectId) {
		return getTagIds(objectType, objectId ).size();
	}


	@Override
	public Map getTagMap(int objectType) {
		return null;
	}


	@Override
	public void removeTag(ContentTag tag, int objectType, long objectId)
			throws UnAuthorizedException {
		if( objectType < 0 || objectId < 0L)
			throw new IllegalStateException();
		synchronized(getLock(objectType, objectId)){
			List<Long> tags = getTagIds(objectType, objectId);
			int index = tags.indexOf(Long.valueOf(tag.getTagId()));
			if(index >= 0)
			{
				tags.remove(index);
				tagContentCache.put(new Element( getCacheKey(objectType, objectId), tags ));
			} else {
				throw new IllegalArgumentException(
						"Tag is not associated with this object");
			}
		}
		
		 // fire event..
		removeTagFromDb(tag, objectType, objectId);
	}
	
	private void removeTagFromDb(ContentTag tag, int objectType, long objectId){
		tagDao.removeTag(tag.getTagId(), objectType, objectId);
		if(tagContentCache.get(getCacheKey(objectType, objectId)) != null)
			((List<Long>)tagContentCache.get(getCacheKey(objectType, objectId)).getValue()).remove(Long.valueOf(tag.getTagId()));
/*		if(tagDao.countTags(tag.getTagId()) <= 0 && !tagSetManager.getTagSetsTagBelongsTo(tag).hasNext())
        {
			tagDao.deleteContentTag(tag.getTagId());
			tagCache.remove(Long.valueOf(tag.getTagId()));
			tagIdCache.remove(tag.getName().toLowerCase());
			
        }*/
	}

	@Override
	public void removeAllTags(int objectType, long objectId)
			throws UnAuthorizedException {
		if( objectType < 0 || objectId < 0L)
			throw new IllegalStateException();
		synchronized(getLock(objectType, objectId)){			
			List<ContentTag> contentTags = new ImmutableList.Builder<ContentTag>().addAll( getTags(objectType, objectId)).build();
			for( ContentTag tag :  contentTags ){
				List<Long> tags = getTagIds(objectType, objectId);
				int index = tags.indexOf(Long.valueOf(tag.getTagId()));
				if (index >= 0)
					tags.remove(index);
				else
					throw new IllegalArgumentException(	"Tag is not associated with this object");
			}
			 tagContentCache.remove(getCacheKey(objectType, objectId));
		}
	}

	@EventListener
	public void onEvent(PageEvent event) {
		log.debug("page event : " + event.getType().name());
	}
}
