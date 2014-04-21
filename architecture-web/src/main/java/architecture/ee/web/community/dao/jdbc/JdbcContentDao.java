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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.support.SqlLobValue;

import architecture.common.user.UserTemplate;
import architecture.ee.jdbc.property.dao.ExtendedPropertyDao;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;
import architecture.ee.web.community.ContentNotFoundException;
import architecture.ee.web.community.content.Content;
import architecture.ee.web.community.dao.ContentDao;
import architecture.ee.web.community.impl.ContentImpl;

public class JdbcContentDao extends ExtendedJdbcDaoSupport implements ContentDao {

	private ExtendedPropertyDao extendedPropertyDao;	
	private String sequencerName = "CONTENT";
	private String contentPropertyTableName = "V2_CONTENT_PROPERTY";
	private String contentPropertyPrimaryColumnName = "CONTENT_ID";
	

	
	private final RowMapper<Content> contentMapper = new RowMapper<Content>(){
		public Content mapRow(ResultSet rs, int rowNum) throws SQLException {
			ContentImpl content = new ContentImpl();
			content.setContentId( rs.getLong("CONTENT_ID"));
			content.setContentType(rs.getString("CONTENT_TYPE"));
			content.setSubject(rs.getString("SUBJECT"));
			content.setBody(rs.getString("BODY"));
			content.setSummary(rs.getString("SUMMARY"));
			content.setCreator( new UserTemplate( rs.getLong("CREATOR")) );
			content.setModifier( new UserTemplate( rs.getLong("LASTMODIFIER")) );
			content.setCreationDate(rs.getDate("CREATION_DATE"));
			content.setModifiedDate(rs.getDate("MODIFIED_DATE"));					
			return content;
		}		
	};
	
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
	 * @return contentPropertyPrimaryColumnName
	 */
	public String getContentPropertyPrimaryColumnName() {
		return contentPropertyPrimaryColumnName;
	}
	/**
	 * @param contentPropertyPrimaryColumnName 설정할 contentPropertyPrimaryColumnName
	 */
	public void setContentPropertyPrimaryColumnName(
			String contentPropertyPrimaryColumnName) {
		this.contentPropertyPrimaryColumnName = contentPropertyPrimaryColumnName;
	}
	

	public Long nextId( ){
		return getNextId(sequencerName);
	}
	
	public Map<String, String> getContentProperties(long contentId) {
		return extendedPropertyDao.getProperties(contentPropertyTableName, contentPropertyPrimaryColumnName, contentId);
	}

	public void deleteContentProperties(long contentId) {
		extendedPropertyDao.deleteProperties(contentPropertyTableName, contentPropertyPrimaryColumnName, contentId);
	}
	
	public void setContentProperties(long contentId, Map<String, String> props) {
		extendedPropertyDao.updateProperties(contentPropertyTableName, contentPropertyPrimaryColumnName, contentId, props);
	}	
	
	public List<Long> getContentIdsForUser(long userId) {
		return getExtendedJdbcTemplate().query(
				getBoundSql("ARCHITECTURE_WEB.SELECT_CONTENT_IDS_BY_USER_ID").getSql(),
				new RowMapper<Long>(){
					public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
						return rs.getLong(1);
					}
				},
				new SqlParameterValue (Types.NUMERIC, userId ));		
	}
	
	public int getContentCountForUser(long userId) {
		return getExtendedJdbcTemplate().queryForInt(
				getBoundSql("ARCHITECTURE_WEB.COUNT_CONTENT_BY_USER_ID").getSql(), 
				new SqlParameterValue(Types.NUMERIC, userId ));
	}
	public Content getContent(long contentId) throws ContentNotFoundException  {
		try {
			Content content = getExtendedJdbcTemplate().queryForObject(
					getBoundSql("ARCHITECTURE_WEB.SELECT_CONTENT_BY_ID").getSql(), 
					contentMapper, 
					new SqlParameterValue (Types.NUMERIC, contentId ));
			content.setProperties( getContentProperties(contentId) );
			return content;
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ContentNotFoundException(e);
		}
	}
	
	public void updateContent(Content content) {
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_WEB.UPDATE_CONTENT").getSql(), 					
				new SqlParameterValue(Types.VARCHAR, content.getContentType()),
				new SqlParameterValue(Types.VARCHAR, content.getSubject()),
				new SqlParameterValue(Types.VARCHAR, content.getSummary()),
				new SqlParameterValue ( Types.CLOB,  new SqlLobValue(content.getBody(), getLobHandler() ) ),
				new SqlParameterValue(Types.NUMERIC, content.getModifier().getUserId() ),				
				new SqlParameterValue(Types.DATE, content.getModifiedDate()),
				new SqlParameterValue(Types.NUMERIC, content.getContentId()));
		
		setContentProperties(content.getContentId(), content.getProperties());
		//saveContentBody(architecture.ee.web.community.content);		
	}
	public void insertContent(Content content) {
		long contentId = nextId();
		content.setContentId(contentId);	
		if("".equals(content.getSummary()))
			content.setSummary(null);		
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_WEB.INSERT_CONTENT").getSql(), 					
				new SqlParameterValue(Types.NUMERIC, content.getContentId()),
				new SqlParameterValue(Types.VARCHAR, content.getContentType()),
				new SqlParameterValue(Types.VARCHAR, content.getSubject()),
				new SqlParameterValue(Types.VARCHAR, content.getSummary()),
				new SqlParameterValue ( Types.CLOB,  new SqlLobValue(content.getBody(), getLobHandler() ) ),
				new SqlParameterValue(Types.NUMERIC, content.getCreator().getUserId() ),
				new SqlParameterValue(Types.NUMERIC, content.getModifier().getUserId() ),				
				new SqlParameterValue(Types.DATE, content.getCreationDate()),
				new SqlParameterValue(Types.DATE, content.getModifiedDate()));		
		
		setContentProperties(content.getContentId(), content.getProperties());
		//saveContentBody(architecture.ee.web.community.content);
	}
	
	
	public void saveContentBody(Content content){		
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_WEB.UPDATE_CONTENT_BODY").getSql(), 
				new SqlParameterValue ( Types.NUMERIC, content.getContentId()), 
				new SqlParameterValue ( Types.CLOB,  new SqlLobValue(content.getBody(), getLobHandler() ) ) 
		);		
	}
	
}
