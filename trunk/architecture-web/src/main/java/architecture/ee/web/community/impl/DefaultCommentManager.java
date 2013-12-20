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
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import architecture.common.event.api.EventPublisher;
import architecture.common.event.api.EventSource;
import architecture.common.user.UserManager;
import architecture.common.user.UserNotFoundException;
import architecture.common.user.authentication.AnonymousUser;
import architecture.ee.web.community.Comment;
import architecture.ee.web.community.CommentNotFoundException;
import architecture.ee.web.community.dao.CommentDao;

public class DefaultCommentManager implements ExtendedCommentManager, EventSource {

	private Cache treeWalkerCache;
	private Cache commentCache;
	private Cache userCommentCountCache;
	private EventPublisher eventPublisher;
	private CommentDao commentDao;
	private UserManager userManager;
	
	

	/**
	 * @param userManager 설정할 userManager
	 */
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	/**
	 * @return commentDao
	 */
	public CommentDao getCommentDao() {
		return commentDao;
	}

	/**
	 * @return treeWalkerCache
	 */
	public Cache getTreeWalkerCache() {
		return treeWalkerCache;
	}

	/**
	 * @param treeWalkerCache 설정할 treeWalkerCache
	 */
	public void setTreeWalkerCache(Cache treeWalkerCache) {
		this.treeWalkerCache = treeWalkerCache;
	}

	/**
	 * @return commentCache
	 */
	public Cache getCommentCache() {
		return commentCache;
	}

	/**
	 * @param commentCache 설정할 commentCache
	 */
	public void setCommentCache(Cache commentCache) {
		this.commentCache = commentCache;
	}

	/**
	 * @return userCommentCountCache
	 */
	public Cache getUserCommentCountCache() {
		return userCommentCountCache;
	}

	/**
	 * @param userCommentCountCache 설정할 userCommentCountCache
	 */
	public void setUserCommentCountCache(Cache userCommentCountCache) {
		this.userCommentCountCache = userCommentCountCache;
	}

	/**
	 * @param commentDao 설정할 commentDao
	 */
	public void setCommentDao(CommentDao commentDao) {
		this.commentDao = commentDao;
	}

	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	private static String treeWalkerCacheKey(int objectType, long objectId){
		return new StringBuilder("commentTreeWalker-").append(objectType).append("-").append(objectId).toString();
	}

	private static String countCacheKey(int objectType, long objectId){
		return new StringBuilder("commentCount-").append(objectType).append("-").append(objectId).toString();
	}
	
	public int getCommentCount(int objectType, long objectId){		
		String key = treeWalkerCacheKey(objectType, objectId);
		Integer count = 0 ;
		if( userCommentCountCache.get(key)!=null ){
			count = (Integer) userCommentCountCache.get(key).getValue();	
		}else{
			count = commentDao.getCommentCount(objectType, objectId);
			userCommentCountCache.put(new Element( key, count));
		}				
		return 0;
	}
	
	public List<Comment> getComments(int objectType, long objectId){	
		String key = treeWalkerCacheKey(objectType, objectId);
		List<Long> ids ;
		if( treeWalkerCache.get(key)!=null ){
			ids = (List<Long>) treeWalkerCache.get(key).getValue();	
		}else{
			ids = commentDao.getCommentIds(objectType, objectId);
			treeWalkerCache.put(new Element( key, ids));
		}
		List<Comment> comments = new ArrayList<Comment>(ids.size());
		for( Long id : ids ){
			try {
				Comment comment = getCommentById(id);
				comments.add(comment);
			} catch (CommentNotFoundException e) {
			}
		}
		return comments;	
	}
	
	public List<Comment> getComments(int objectType, long objectId, int startIndex, int maxResults ){
		
		List<Long> ids = commentDao.getCommentIds(objectType, objectId, startIndex, maxResults);
		List<Comment> comments = new ArrayList<Comment>(ids.size());
		for( Long id : ids ){
			try {
				Comment comment = getCommentById(id);
				comments.add(comment);
			} catch (CommentNotFoundException e) {
			}
		}
		return comments;	
	}
	
	public Comment getCommentById(Long commentId) throws CommentNotFoundException {
		if(commentId < 1L)
			throw new CommentNotFoundException();
		Comment comment ;
		if( commentCache.get(commentId)!=null ){
			comment = (Comment) commentCache.get(commentId).getValue();	
		}else{
			comment = commentDao.getCommentById(commentId);
			if(comment == null)
				throw new CommentNotFoundException((new StringBuilder()).append("Comment ").append(commentId).append(" could not be loaded from the database.").toString());
			
			if( comment.getUserId() > 1 ){
				try {
					comment.setUser( userManager.getUser(comment.getUserId()) );
				} catch (UserNotFoundException e) {
					comment.setUser(new AnonymousUser());
				}
			}else{
				comment.setUser(new AnonymousUser());
			}
			commentCache.put(new Element( comment.getCommentId(), comment ));
		}
		return comment;
	}


	public Comment createComment() {
		return new CommentImpl();
	}

	public void addComment(Comment comment) {
		commentDao.createComment(comment);	
		clearCache(comment);
	}
	
	public void updateComment(Comment comment) {
		commentDao.updateComment(comment);
		commentCache.remove(comment.getCommentId());	
	}

	public void removeComment(Comment comment) {
		commentDao.deleteComment(comment);		
		// clear comment cache ;
		clearCache(comment);
	}
	
	private void clearCache( Comment comment ){
		commentCache.remove(comment.getCommentId());
		userCommentCountCache.remove(treeWalkerCacheKey(comment.getObjectType(), comment.getObjectId()));
		userCommentCountCache.remove(countCacheKey(comment.getObjectType(), comment.getObjectId()));
	}
}