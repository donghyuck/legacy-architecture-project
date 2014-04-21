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
package architecture.ee.web.community.dao.jdbc;

import java.util.Date;
import java.util.List;
import java.util.Map;

import architecture.ee.jdbc.property.dao.ExtendedPropertyDao;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;
import architecture.ee.web.community.Comment;
import architecture.ee.web.community.CommentNotFoundException;
import architecture.ee.web.community.dao.CommentDao;

public class JdbcCommentDao extends ExtendedJdbcDaoSupport implements CommentDao {
	
	private ExtendedPropertyDao extendedPropertyDao;	
	private String sequencerName = "COMMENT";
	private String commentPropertyTableName = "V2_COMMENT_PROPERTY";
	private String commentPropertyPrimaryColumnName = "COMMENT_ID";
	/**
	 * @return extendedPropertyDao
	 */
	public ExtendedPropertyDao getExtendedPropertyDao() {
		return extendedPropertyDao;
	}
	/**
	 * @param extendedPropertyDao 설정할 extendedPropertyDao
	 */
	public void setExtendedPropertyDao(ExtendedPropertyDao extendedPropertyDao) {
		this.extendedPropertyDao = extendedPropertyDao;
	}
	/**
	 * @return sequencerName
	 */
	public String getSequencerName() {
		return sequencerName;
	}
	/**
	 * @param sequencerName 설정할 sequencerName
	 */
	public void setSequencerName(String sequencerName) {
		this.sequencerName = sequencerName;
	}
	/**
	 * @return commentPropertyTableName
	 */
	public String getCommentPropertyTableName() {
		return commentPropertyTableName;
	}
	/**
	 * @param commentPropertyTableName 설정할 commentPropertyTableName
	 */
	public void setCommentPropertyTableName(String commentPropertyTableName) {
		this.commentPropertyTableName = commentPropertyTableName;
	}
	/**
	 * @return commentPropertyPrimaryColumnName
	 */
	public String getCommentPropertyPrimaryColumnName() {
		return commentPropertyPrimaryColumnName;
	}
	/**
	 * @param commentPropertyPrimaryColumnName 설정할 commentPropertyPrimaryColumnName
	 */
	public void setCommentPropertyPrimaryColumnName(
			String commentPropertyPrimaryColumnName) {
		this.commentPropertyPrimaryColumnName = commentPropertyPrimaryColumnName;
	}
	
	public Long nextId( ){
		return getNextId(sequencerName);
	}
	

	public Map<String, String> getCommentProperties(long commentId) {
		return extendedPropertyDao.getProperties(commentPropertyTableName, commentPropertyPrimaryColumnName, commentId);
	}

	public void deleteCommentProperties(long commentId) {
		extendedPropertyDao.deleteProperties(commentPropertyTableName, commentPropertyPrimaryColumnName, commentId);
	}
	
	public void setCommentProperties(long commentId, Map<String, String> props) {
		extendedPropertyDao.updateProperties(commentPropertyTableName, commentPropertyPrimaryColumnName, commentId, props);
	}
	public void createComment(Comment commnet) {
		// TODO 자동 생성된 메소드 스텁
		
	}
	public void updateComment(Comment commnet) {
		// TODO 자동 생성된 메소드 스텁
		
	}
	public void deleteComment(Comment commnet) {
		// TODO 자동 생성된 메소드 스텁
		
	}
	public Comment getCommentById(Long id) {
		// TODO 자동 생성된 메소드 스텁
		return null;
	}
	public int getCommentCount(int objectType, long objectId) {
		// TODO 자동 생성된 메소드 스텁
		return 0;
	}
	public List<Long> getCommentIds(int objectType, long objectId) {
		// TODO 자동 생성된 메소드 스텁
		return null;
	}
	public List<Long> getCommentIds(int objectType, long objectId,
			int startIndex, int maxResults) {
		// TODO 자동 생성된 메소드 스텁
		return null;
	}
	public List<Comment> getUserComments(Long id)
			throws CommentNotFoundException {
		// TODO 자동 생성된 메소드 스텁
		return null;
	}
	public List<Comment> getCommentsUpdatedAfter(Date date) {
		// TODO 자동 생성된 메소드 스텁
		return null;
	}
	
	
}
