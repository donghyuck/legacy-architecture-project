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
package architecture.ee.web.community.comment;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.common.event.api.EventListener;
import architecture.common.event.api.EventPublisher;
import architecture.common.model.factory.ModelTypeFactory;
import architecture.common.user.User;
import architecture.common.user.UserManager;
import architecture.common.user.UserNotFoundException;
import architecture.common.user.authentication.UnAuthorizedException;
import architecture.common.util.LockUtils;
import architecture.ee.web.community.comment.dao.CommentDao;
import architecture.ee.web.community.comment.event.CommentEvent;
import architecture.ee.web.community.page.Page;
import architecture.ee.web.community.page.PageManager;
import architecture.ee.web.community.page.event.PageEvent;

public class DefaultCommentManager implements CommentManager  {

	
	private static final Log log = LogFactory.getLog(DefaultCommentManager.class);
	private CommentDao commentDao;
	private EventPublisher eventPublisher;
	private Cache commentCache;
	private Cache treeWalkerCache;
	private PageManager pageManager;
	private UserManager userManager;
	private Comment rootParent = new DefaultComment();
	
	public DefaultCommentManager() {
	}
	
	public void initialize(){
		this.eventPublisher.register(this);		
	}
	

	/**
	 * @return commentDao
	 */
	public CommentDao getCommentDao() {
		return commentDao;
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

	/**
	 * @return eventPublisher
	 */
	public EventPublisher getEventPublisher() {
		return eventPublisher;
	}

	/**
	 * @return commentCache
	 */
	public Cache getCommentCache() {
		return commentCache;
	}

	/**
	 * @return treeWalkerCache
	 */
	public Cache getTreeWalkerCache() {
		return treeWalkerCache;
	}

	/**
	 * @return pageManager
	 */
	public PageManager getPageManager() {
		return pageManager;
	}

	/**
	 * @param commentCache 설정할 commentCache
	 */
	public void setCommentCache(Cache commentCache) {
		this.commentCache = commentCache;
	}

	/**
	 * @param treeWalkerCache 설정할 treeWalkerCache
	 */
	public void setTreeWalkerCache(Cache treeWalkerCache) {
		this.treeWalkerCache = treeWalkerCache;
	}

	/**
	 * @param commentDao 설정할 commentDao
	 */
	public void setCommentDao(CommentDao commentDao) {
		this.commentDao = commentDao;
	}



	/**
	 * @param pageManager 설정할 pageManager
	 */
	public void setPageManager(PageManager pageManager) {
		this.pageManager = pageManager;
	}

	/**
	 * @param eventPublisher 설정할 eventPublisher
	 */
	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}
	
	@EventListener
	public void onEvent(PageEvent event) {
		log.debug("page event : " + event.getType().name());
		Page page = (Page)event.getSource();
		if( event.getType() == PageEvent.Type.CREATED ){

		}else if ( event.getType() == PageEvent.Type.DELETED ){

		}
	}
	

	@Override
	public Comment getComment(long commentId) throws CommentNotFoundException {
        if(commentId < 1L)
            throw new CommentNotFoundException();
        Comment comment;
		if( commentCache.get(commentId)!= null){
			comment = (Comment)commentCache.get(commentId).getValue();
		}else{
			comment = commentDao.getCommentById(commentId);
			setUserInComment(comment);
			commentCache.put(new Element(comment.getCommentId(), comment));
		}		
		return comment;
	}

	protected void setUserInComment(Comment comment){
		long userId = comment.getUser().getUserId();
		try {
			comment.setUser(userManager.getUser(userId));
		} catch (UserNotFoundException e) {
		}		
	}
	
	protected void clearCache(Comment comment){
		 String key = getTreeWalkerCacheKey( comment.getObjectType(), comment.getObjectId());
		commentCache.remove(comment.getCommentId());
		synchronized(key){
			treeWalkerCache.remove(key);
		}
	}
	
	@Override
	public void update(Comment comment) throws UnAuthorizedException {
		Date now = Calendar.getInstance().getTime();
		comment.setModifiedDate(now);
		commentDao.update(comment);		
		clearCache(comment);
		Map params = new HashMap();
		eventPublisher.publish(new CommentEvent(comment, CommentEvent.Type.UPDATED, params));		
	}

	@Override
	public void setBody(Comment comment, String text) {
		if(text == null)
			throw new IllegalArgumentException("Body cannot be null");	
		try {
			Comment c = getComment(comment.getCommentId());
			Date now = Calendar.getInstance().getTime();
			c.setBody(text);
			c.setModifiedDate(now);
			commentDao.update(c);			
			clearCache(comment);			
			Map params = new HashMap();
			params.put("Type", "bodyModify");
			params.put("originalValue", comment.getBody());
			eventPublisher.publish(new CommentEvent(c, CommentEvent.Type.UPDATED, params));
		} catch (CommentNotFoundException e) {
		}
		
	}

	@Override
	public void addComment(Comment comment) throws UnAuthorizedException {
		addComment(null, comment);
	}

	public void addComment(Comment parentComment, Comment newComment) {
		if (newComment == null)
			throw new IllegalStateException("Comment cannot be null");		
		
		int objectType = newComment.getObjectType();
		long objectId = newComment.getObjectId();
		boolean isAuthor = false;				
	
		if(newComment.getCommentId() != -1L){
			throw new IllegalStateException("Comment cannot be attached to this object since it is already attached to another object");			
		}		
		 if(parentComment != null){
			 DefaultComment bean= (DefaultComment)newComment;
			 bean.setParentCommentId(parentComment.getCommentId());
			 bean.setParentObjectType(parentComment.getObjectType());
			 bean.setParentObjectId(parentComment.getObjectId());
		}		 
		
		 // source post checking
		commentDao.create(newComment);	 
		String key = getTreeWalkerCacheKey(objectType, objectId);
		synchronized (key) {
			treeWalkerCache.remove(key);
		}	
		Map paramMap = Collections.emptyMap();
		eventPublisher.publish(new CommentEvent(newComment, CommentEvent.Type.CREATED, paramMap));
	}
	
	
	@Override
	public Comment createComment(int objectType, long objectId, User user, String text) throws UnAuthorizedException {
		
		if( !(ModelTypeFactory.getTypeIdFromCode("PAGE") == objectType) ){
			throw new IllegalStateException("Comment not allowed for objectType[" + objectType + "]");		
		}		
		DefaultComment comment = new DefaultComment();
		comment.setObjectType(objectType);
		comment.setObjectId(objectId);
		comment.setBody(text);
		comment.setUser(user);
		
		return comment;
	}

	public CommentTreeWalker getCommentTreeWalker(int objectType, long objectId)
	{
		String key = getTreeWalkerCacheKey(objectType, objectId);
		CommentTreeWalker treeWalker ;
		if(treeWalkerCache.get(key) != null ){
			treeWalker = (CommentTreeWalker)treeWalkerCache.get(key).getValue();
		}else{
			synchronized(key){
				treeWalker = commentDao.getCommentTreeWalker(objectType, objectId);
				treeWalkerCache.put(new Element(key, treeWalker));
			}
		}				
		return treeWalker;
	}
	
	 private static String getTreeWalkerCacheKey( int objectType, long objectId) {
		 return LockUtils.intern((new StringBuilder("commentTreeWalker-")).append(objectType).append("-").append(objectId).toString());
	 }

	@Override
	public Comment getRootParent() {
		return rootParent;
	}
}
