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
package architecture.ee.web.community.comment.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;

import architecture.common.user.UserTemplate;
import architecture.common.util.LongTree;
import architecture.ee.jdbc.property.dao.ExtendedPropertyDao;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;
import architecture.ee.web.community.comment.Comment;
import architecture.ee.web.community.comment.CommentNotFoundException;
import architecture.ee.web.community.comment.CommentTreeWalker;
import architecture.ee.web.community.comment.DefaultComment;
import architecture.ee.web.community.comment.DefaultCommentTreeWalker;
import architecture.ee.web.community.comment.dao.CommentDao;
import architecture.ee.web.community.model.ContentObject;

public class JdbcCommentDao extends ExtendedJdbcDaoSupport  implements CommentDao  {

	protected static final RowMapper<Comment> commentMapper = new RowMapper<Comment>(){
		public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
			
			DefaultComment comment = new DefaultComment(rs.getLong("COMMENT_ID"));
			long parentCommentId = rs.getLong("PARENT_COMMENT_ID");
			if(!rs.wasNull())
				comment.setParentCommentId(parentCommentId);
			comment.setObjectType(rs.getInt("OBJECT_TYPE"));
			comment.setObjectId(rs.getLong("OBJECT_ID"));
			int objectType = rs.getInt("PARENT_OBJECT_TYPE");
			if (!rs.wasNull())
				comment.setParentObjectType(objectType);
			long objectID = rs.getLong("PARENT_OBJECT_ID");
			if (!rs.wasNull())
				comment.setParentObjectId(objectID);
			long userId = rs.getLong("USER_ID");
			if (!rs.wasNull())
				comment.setUser(new UserTemplate(userId));			
			comment.setName(rs.getString("NAME"));
			comment.setEmail(rs.getString("EMAIL"));
			comment.setURL(rs.getString("URL"));
			comment.setIPAddress(rs.getString("IP"));				
			String dbText = rs.getString("BODY");
			comment.setBody(dbText);
			comment.setCreationDate(rs.getTimestamp("CREATION_DATE"));
			comment.setModifiedDate(rs.getTimestamp("MODIFIED_DATE"));		
			comment.setStatus( 
				ContentObject.Status.valueOf( Integer.valueOf( rs.getInt("STATUS") ) )
			);			
			return comment;
		}		
	};
	
	private ExtendedPropertyDao extendedPropertyDao;	
	private String sequencerName = "COMMENT";
	private String commentPropertyTableName = "V2_COMMENT_PROPERTY";
	private String commentPropertyPrimaryColumnName = "COMMENT_ID";
	
	public JdbcCommentDao() {
	}
	
	

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


	public Map<String, String> getCommentProperties(long commentId) {
		return extendedPropertyDao.getProperties(commentPropertyTableName, commentPropertyPrimaryColumnName, commentId);
	}

	public void setCommentProperties(long commentId, Map<String, String> props) {
		extendedPropertyDao.updateProperties(commentPropertyTableName, commentPropertyPrimaryColumnName, commentId, props);
	}

	public void deleteCommentProperties(long commentId) {
		extendedPropertyDao.deleteProperties(commentPropertyTableName, commentPropertyPrimaryColumnName, commentId);
	}
	

	/**
	 * COMMENT_ID
		OBJECT_TYPE
		OBJECT_ID
		PARENT_COMMENT_ID
		PARENT_OBJECT_TYPE
		PARENT_OBJECT_ID
		USER_ID
		NAME
		EMAIL
		URL
		IP
		BODY
		STATUS
		CREATION_DATE
		MODIFIED_DATE
	 */
	public void create(Comment comment) {
		if( comment.getCommentId() == -1L)	
			comment.setCommentId(getNextId(getSequencerName()));		
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_COMMUNITY.CREATE_COMMENT").getSql(), 	
				new SqlParameterValue (Types.NUMERIC, comment.getCommentId()), 
				new SqlParameterValue(Types.NUMERIC, comment.getObjectType()),
				new SqlParameterValue(Types.NUMERIC, comment.getObjectId()),
				new SqlParameterValue(Types.NUMERIC, comment.getParentCommentId()),
				new SqlParameterValue(Types.NUMERIC, comment.getParentObjectType()),
				new SqlParameterValue(Types.NUMERIC, comment.getParentObjectId()),
				new SqlParameterValue(Types.NUMERIC, comment.getUser().getUserId()),
				new SqlParameterValue(Types.VARCHAR, comment.getName()),
				new SqlParameterValue(Types.VARCHAR, comment.getEmail()),
				new SqlParameterValue(Types.VARCHAR, comment.getURL()),
				new SqlParameterValue(Types.VARCHAR, comment.getIPAddress()),
				new SqlParameterValue(Types.VARCHAR, comment.getBody()),
				new SqlParameterValue(Types.VARCHAR, comment.getStatus().getIntValue()),
				new SqlParameterValue(Types.TIMESTAMP, comment.getCreationDate()),
				new SqlParameterValue(Types.TIMESTAMP, comment.getModifiedDate()));			
		
		 if(comment.getProperties() != null && !comment.getProperties().isEmpty())
			 setCommentProperties(comment.getCommentId(), comment.getProperties());				 	 
	}

	/*
	 * 
	 * 	PARENT_COMMENT_ID = ?,
		NAME = ?,
		EMAIL = ?,
		URL = ?,
		IP = ?,
		BODY = ?,
		STATUS = ?,
		MODIFIED_DATE = ?
		
	 */
	@Override
	public void update(Comment comment) {
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_COMMUNITY.UPDATE_COMMENT").getSql(), 	
				new SqlParameterValue(Types.NUMERIC, comment.getParentCommentId()),
				new SqlParameterValue(Types.NUMERIC, comment.getName()),
				new SqlParameterValue(Types.NUMERIC, comment.getEmail()),
				new SqlParameterValue(Types.NUMERIC, comment.getURL()),
				new SqlParameterValue(Types.VARCHAR, comment.getIPAddress()),
				new SqlParameterValue(Types.VARCHAR, comment.getBody()),
				new SqlParameterValue(Types.VARCHAR, comment.getStatus().getIntValue()),
				new SqlParameterValue(Types.TIMESTAMP, comment.getModifiedDate()));							
		setCommentProperties(comment.getCommentId(), comment.getProperties());				
	}

	@Override
	public void delete(Comment comment) {
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_COMMUNITY.UPDATE_COMMENT").getSql(), 	
			new SqlParameterValue(Types.NUMERIC, comment.getCommentId())
		);
		deleteCommentProperties(comment.getCommentId());		
	}

	@Override
	public Comment getCommentById(long commentId) throws CommentNotFoundException {
		try {
			Comment c = getExtendedJdbcTemplate().queryForObject(
					getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_COMMENT_BY_ID").getSql(), 
					commentMapper, 
					new SqlParameterValue (Types.NUMERIC, commentId ));
			c.setProperties( getCommentProperties(commentId) );
			return c;
		} catch (DataAccessException e) {
			throw new CommentNotFoundException(e);
		}
	}
	
	public CommentTreeWalker getCommentTreeWalker(int objectType, long objectId){
		int numComments = getExtendedJdbcTemplate().queryForObject(
				getBoundSql("ARCHITECTURE_COMMUNITY.COUNT_COMMENT_BY_OBJECT_TYPE_AND_OBJECT_ID").getSql(), 
				Integer.class,
				new SqlParameterValue(Types.NUMERIC, objectType ),
				new SqlParameterValue(Types.NUMERIC, objectId ));
		numComments++;	
		
		final LongTree tree = new LongTree(-1L, numComments);	
		
		getExtendedJdbcTemplate().query(
				getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_ROOT_COMMENT").getSql(), 
				new RowCallbackHandler(){
					public void processRow(ResultSet rs) throws SQLException {					

//						while( rs.next() ){
							long commentId = rs.getLong(1);							
							tree.addChild(-1L, commentId);
							log.debug("tree add : " + commentId );
//						}						
				}},
				new SqlParameterValue(Types.NUMERIC, objectType ),
				new SqlParameterValue(Types.NUMERIC, objectId ));
		
		getExtendedJdbcTemplate().query(
				getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_CHILD_COMMENT").getSql(), 
				new RowCallbackHandler(){
					public void processRow(ResultSet rs) throws SQLException {						
//						while( rs.next() ){
							long commentId = rs.getLong(1);
							long parentCommentId = rs.getLong(2);
							tree.addChild(parentCommentId, commentId);
							log.debug("tree add : " + parentCommentId + "," +  commentId );
//						}						
					}},
				new SqlParameterValue(Types.NUMERIC, objectType ),
				new SqlParameterValue(Types.NUMERIC, objectId ));
		
		log.debug("total:" + tree.getChildCount(-1L));
		StringBuilder sb = new StringBuilder();
		for( long id : tree.getRecursiveChildren(-1L) ){
			sb.append(id).append(", ");
		}
		log.debug( sb.toString());
		
		return new DefaultCommentTreeWalker(objectType, objectId, tree);		
	} 

}