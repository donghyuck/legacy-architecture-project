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
package architecture.ee.web.community.template.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.support.SqlLobValue;

import architecture.common.user.User;
import architecture.common.user.UserTemplate;
import architecture.ee.jdbc.property.dao.ExtendedPropertyDao;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;
import architecture.ee.web.community.template.Content;
import architecture.ee.web.community.template.impl.ContentImpl;
import architecture.ee.web.navigator.Menu;
import architecture.ee.web.navigator.impl.MenuImpl;

public class JdbcContentDao extends ExtendedJdbcDaoSupport implements ContentDao {

	private final RowMapper<ContentImpl> contentMapper = new RowMapper<ContentImpl>(){
		public ContentImpl mapRow(ResultSet rs, int rowNum) throws SQLException {
			ContentImpl impl = new ContentImpl();			
			impl.setContentId(rs.getLong("CONTENT_ID"));
			impl.setTitle(rs.getString("TITLE"));
			impl.setContentType(rs.getString("CONTENT_TYPE"));
			impl.setObjectType(rs.getInt("OBJECT_TYPE"));
			impl.setObjectId(rs.getLong("OBJECT_ID"));
			impl.setLocation(rs.getString("LOCATION"));
			impl.setCreator(new UserTemplate(rs.getLong("CREATOR")));
			impl.setModifier(new UserTemplate(rs.getLong("LASTMODIFIER")));
			impl.setCreationDate(rs.getDate("CREATION_DATE"));
			impl.setModifiedDate(rs.getDate("MODIFIED_DATE"));			
			return impl;
		}		
	};
	
	private ExtendedPropertyDao extendedPropertyDao;	
	private String sequencerName = "CONTENT";
	private String contentPropertyTableName = "V2_CONTENT_PROPERTY";
	private String contnetPropertyPrimaryColumnName = "CONTENT_ID";
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
	 * @return contentPropertyTableName
	 */
	public String getContentPropertyTableName() {
		return contentPropertyTableName;
	}
	/**
	 * @param contentPropertyTableName 설정할 contentPropertyTableName
	 */
	public void setContentPropertyTableName(String contentPropertyTableName) {
		this.contentPropertyTableName = contentPropertyTableName;
	}
	/**
	 * @return contnetPropertyPrimaryColumnName
	 */
	public String getContnetPropertyPrimaryColumnName() {
		return contnetPropertyPrimaryColumnName;
	}
	/**
	 * @param contnetPropertyPrimaryColumnName 설정할 contnetPropertyPrimaryColumnName
	 */
	public void setContnetPropertyPrimaryColumnName(
			String contnetPropertyPrimaryColumnName) {
		this.contnetPropertyPrimaryColumnName = contnetPropertyPrimaryColumnName;
	}
	

	public Map<String, String> getContentProperties(long contentId) {
		return extendedPropertyDao.getProperties(contentPropertyTableName, contnetPropertyPrimaryColumnName, contentId);
	}

	public void deleteContentProperties(long contentId) {
		extendedPropertyDao.deleteProperties(contentPropertyTableName, contnetPropertyPrimaryColumnName, contentId);
	}
	
	public void setContentProperties(long contentId, Map<String, String> props) {
		extendedPropertyDao.updateProperties(contentPropertyTableName, contnetPropertyPrimaryColumnName, contentId, props);
	}
	public Content createContent(Content content) {
		
		long contentId = getNextId(sequencerName);		
		content.setContentId(contentId);	
		if("".equals(content.getTitle()))
			content.setTitle(null);
		
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_WEB.CREATE_CONTENT").getSql(), 	
				
				new SqlParameterValue(Types.NUMERIC, content.getObjectType() ),
				new SqlParameterValue(Types.NUMERIC, content.getObjectId() ),
				new SqlParameterValue(Types.NUMERIC, content.getContentId()),
				new SqlParameterValue(Types.VARCHAR, content.getContentType()),
				new SqlParameterValue(Types.VARCHAR, content.getTitle()),
				new SqlParameterValue(Types.VARCHAR, content.getLocation()),
				new SqlParameterValue(Types.NUMERIC, content.getCreator().getUserId() ),
				new SqlParameterValue(Types.NUMERIC, content.getCreator().getUserId() ),				
				new SqlParameterValue(Types.DATE, content.getCreationDate()),
				new SqlParameterValue(Types.DATE, content.getModifiedDate()));		
		
		setContentProperties(content.getContentId(), content.getProperties());
		saveContentBody(content);			
		return content;
	}
	public void deleteContent(Content content) {
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_WEB.DELETE_CONTENT").getSql(), 	new SqlParameterValue(Types.NUMERIC, content.getContentId()));
		deleteContentProperties(content.getContentId());
		removeContentBody(content);
	}
	public void updateContent(Content content) {
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_WEB.UPDATE_CONTENT").getSql(), 	
				new SqlParameterValue (Types.NUMERIC, content.getObjectType() ), 
				new SqlParameterValue (Types.NUMERIC, content.getObjectId() ), 
				new SqlParameterValue (Types.VARCHAR, content.getContentType()), 
				new SqlParameterValue (Types.VARCHAR, content.getTitle()), 
				new SqlParameterValue(Types.VARCHAR, content.getLocation()),
				new SqlParameterValue(Types.DATE, content.getModifier().getUserId()),
				new SqlParameterValue(Types.DATE, content.getModifiedDate()),
				new SqlParameterValue (Types.NUMERIC, content.getContentId()) );			
		setContentProperties(content.getContentId(), content.getProperties());
		removeContentBody(content);
		saveContentBody(content);
	}
	public Content getContent(long contentId) {
		ContentImpl impl = null ;
		try {
			
			impl = getExtendedJdbcTemplate().queryForObject(getBoundSql("ARCHITECTURE_WEB.SELECT_CONTENT_BY_ID").getSql(), contentMapper, new SqlParameterValue(Types.NUMERIC, contentId ) );
			impl.setProperties(getContentProperties( impl.getContentId() ));
			impl.setBody(getContentBody(impl));
			
		} catch (IncorrectResultSizeDataAccessException e) {
			if(e.getActualSize() > 1)
	        {
	            log.warn((new StringBuilder()).append("Multiple occurrances of the same content ID found: ").append(contentId).toString());
	            throw e;
	        }
		} catch (DataAccessException e) {
			 String message = (new StringBuilder()).append("Failure attempting to load content by ID : ").append(contentId).append(".").toString();
			 log.fatal(message, e);
		}	
		return impl;
	}
	
	public List<Long> getContentIds(int objectType, long objectId) {
		return getExtendedJdbcTemplate().query(
				getBoundSql("ARCHITECTURE_WEB.SELECT_CONTENT_IDS_BY_OBJECT_TYPE_AND_OBJECT_ID").getSql(),
				new RowMapper<Long>(){
					public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
						return rs.getLong(1);
					}
				},
				new SqlParameterValue (Types.NUMERIC, objectType ), new SqlParameterValue (Types.NUMERIC, objectId ));		
	}
	
	public List<Long> getContentIds(int objectType, long objectId, int startIndex, int numResults) {
		return getExtendedJdbcTemplate().queryScrollable(
				getBoundSql("ARCHITECTURE_WEB.SELECT_CONTENT_IDS_BY_OBJECT_TYPE_AND_OBJECT_ID").getSql(), 
				startIndex, 
				numResults, 
				new Object[]{objectType, objectId}, 
				new int[]{Types.NUMERIC, Types.NUMERIC}, Long.class);
	}
	
	public int getContentCount(int objectType, long objectId) {
		return getExtendedJdbcTemplate().queryForInt(
				getBoundSql("ARCHITECTURE_WEB.COUNT_CONTENT_BY_OBJECT_TYPE_AND_OBJECT_ID").getSql(), 
				new SqlParameterValue(Types.NUMERIC, objectType ), 
				new SqlParameterValue(Types.NUMERIC, objectId ));
	}
	
	public void removeContentBody(Content content){		
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_WEB.REMOVE_CONTENT_BODY").getSql(), 	new SqlParameterValue(Types.NUMERIC, content.getContentId()));
	}
	
	public void saveContentBody(Content content){		
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_WEB.CREATE_CONTENT_BODY").getSql(), 
				new SqlParameterValue ( Types.NUMERIC, content.getContentId()), 
				new SqlParameterValue ( Types.CLOB,  new SqlLobValue(content.getBody(), getLobHandler() ) ) 
		);		
	}
	
	public String getContentBody(Content content) {		
		return getExtendedJdbcTemplate().queryForObject(getBoundSql("ARCHITECTURE_WEB.SELECT_CONTENT_BODY_BY_ID").getSql(), 
				String.class, new SqlParameterValue (Types.NUMERIC, content.getContentId()));
	}
	
}
