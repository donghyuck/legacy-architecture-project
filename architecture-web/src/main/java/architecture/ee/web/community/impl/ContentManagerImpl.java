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
package architecture.ee.web.community.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.common.event.api.EventPublisher;
import architecture.common.event.api.EventSource;
import architecture.common.user.User;
import architecture.common.user.UserManager;
import architecture.ee.web.community.Content;
import architecture.ee.web.community.ContentManager;
import architecture.ee.web.community.ContentNotFoundException;
import architecture.ee.web.community.dao.ContentDao;

public class ContentManagerImpl  implements ContentManager, EventSource {

	protected Log log = LogFactory.getLog(getClass());	
	
	private EventPublisher eventPublisher;
	
	private ContentDao contentDao ;
	
	private Cache contentCache ;
	
	private UserManager userManager;

	/**
	 * @param eventPublisher 설정할 eventPublisher
	 */
	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	/**
	 * @param contentCache 설정할 contentCache
	 */
	public void setContentCache(Cache contentCache) {
		this.contentCache = contentCache;
	}

	/**
	 * @return contentDao
	 */
	public ContentDao getContentDao() {
		return contentDao;
	}

	/**
	 * @param contentDao 설정할 contentDao
	 */
	public void setContentDao(ContentDao contentDao) {
		this.contentDao = contentDao;
	}

	/**
	 * @return userManager
	 */
	public UserManager getUserManager() {
		return userManager;
	}

	/**
	 * @param userManager 설정할 userManager
	 */
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public int getContentCount(User user) {		
		return contentDao.getContentCountForUser(user.getUserId());
	}

	public List<Content> getContents(User user) {
		List<Long> ids = contentDao.getContentIdsForUser(user.getUserId());
		List<Content> list = new ArrayList<Content>(ids.size());
		for( Long contentId : ids ){
			try {
				list.add(getContent(contentId));
			} catch (Exception e) {}
		}	
		return list;
	}

	public List<Content> getContents(User user, int startIndex, int maxResults) {
		// TODO 자동 생성된 메소드 스텁
		return null;
	}

	public Content getContent(long contentId) throws ContentNotFoundException {
		Content content = null  ;
		if( contentCache.get(contentId) == null){
			try {
				content = getContentById(contentId);				
				if( content.getCreator().getUserId() > 0 )
					content.setCreator( userManager.getUser(content.getCreator().getUserId()));
				
				if( content.getModifier().getUserId() > 0 )
					content.setModifier( userManager.getUser(content.getModifier().getUserId()));								
				contentCache.put(new Element(contentId, content));
				
			} catch (Exception e) {
				 String msg = (new StringBuilder()).append("Unable to find content ").append(contentId).toString();
	             throw new ContentNotFoundException (msg, e);
			}
		}else{
			content =  (Content) contentCache.get( contentId ).getValue();
		}		
		return content;
	}

	public void updateContent(Content content) {
		Content contentToUser = content;
		contentToUser.setModifiedDate(new Date());
		if(contentToUser.getModifier() == null )
			contentToUser.setModifier(contentToUser.getCreator());		
		contentDao.updateContent(contentToUser);
		contentCache.remove(contentToUser.getContentId());		
	}
	
	protected Content getContentById(long contentId) throws ContentNotFoundException{		
		return contentDao.getContent(contentId);
	}
	
}
