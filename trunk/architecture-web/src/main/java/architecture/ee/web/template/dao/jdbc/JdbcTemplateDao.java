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
package architecture.ee.web.template.dao.jdbc;

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

import architecture.common.user.UserTemplate;
import architecture.ee.jdbc.property.dao.ExtendedPropertyDao;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;
import architecture.ee.web.template.Template;
import architecture.ee.web.template.dao.TemplateDao;
import architecture.ee.web.template.impl.TemplateImpl;

public class JdbcTemplateDao extends ExtendedJdbcDaoSupport implements TemplateDao {

	private final RowMapper<TemplateImpl> templateMapper = new RowMapper<TemplateImpl>(){
		public TemplateImpl mapRow(ResultSet rs, int rowNum) throws SQLException {
			TemplateImpl impl = new TemplateImpl();			
			impl.setTemplateId(rs.getLong("TEMPLATE_ID"));
			impl.setTitle(rs.getString("TITLE"));
			impl.setTemplateType(rs.getString("TEMPLATE_TYPE"));
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
	private String sequencerName = "TEMPLATE";
	private String templatePropertyTableName = "V2_TEMPLATE_PROPERTY";
	private String templatePropertyPrimaryColumnName = "TEMPLATE_ID";
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
	public String getTemplatePropertyTableName() {
		return templatePropertyTableName;
	}
	/**
	 * @param contentPropertyTableName 설정할 contentPropertyTableName
	 */
	public void setTemplatePropertyTableName(String contentPropertyTableName) {
		this.templatePropertyTableName = contentPropertyTableName;
	}
	/**
	 * @return contnetPropertyPrimaryColumnName
	 */
	public String getTemplatePropertyPrimaryColumnName() {
		return templatePropertyPrimaryColumnName;
	}
	/**
	 * @param contnetPropertyPrimaryColumnName 설정할 contnetPropertyPrimaryColumnName
	 */
	public void setTemplatePropertyPrimaryColumnName(
			String contnetPropertyPrimaryColumnName) {
		this.templatePropertyPrimaryColumnName = contnetPropertyPrimaryColumnName;
	}
	

	public Map<String, String> getTemplateProperties(long contentId) {
		return extendedPropertyDao.getProperties(templatePropertyTableName, templatePropertyPrimaryColumnName, contentId);
	}

	public void deleteTemplateProperties(long contentId) {
		extendedPropertyDao.deleteProperties(templatePropertyTableName, templatePropertyPrimaryColumnName, contentId);
	}
	
	public void setTemplateProperties(long contentId, Map<String, String> props) {
		extendedPropertyDao.updateProperties(templatePropertyTableName, templatePropertyPrimaryColumnName, contentId, props);
	}
	public Template createTemplate(Template content) {
		
		long contentId = getNextId(sequencerName);		
		content.setTemplateId(contentId);	
		if("".equals(content.getTitle()))
			content.setTitle(null);
		
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_WEB.CREATE_TEMPLATE").getSql(), 	
				
				new SqlParameterValue(Types.NUMERIC, content.getObjectType() ),
				new SqlParameterValue(Types.NUMERIC, content.getObjectId() ),
				new SqlParameterValue(Types.NUMERIC, content.getTemplateId()),
				new SqlParameterValue(Types.VARCHAR, content.getTemplateType()),
				new SqlParameterValue(Types.VARCHAR, content.getTitle()),
				new SqlParameterValue(Types.VARCHAR, content.getLocation()),
				new SqlParameterValue(Types.NUMERIC, content.getCreator().getUserId() ),
				new SqlParameterValue(Types.NUMERIC, content.getCreator().getUserId() ),				
				new SqlParameterValue(Types.DATE, content.getCreationDate()),
				new SqlParameterValue(Types.DATE, content.getModifiedDate()));		
		
		setTemplateProperties(content.getTemplateId(), content.getProperties());
		saveTemplateBody(content);			
		return content;
	}
	public void deleteTemplate(Template content) {
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_WEB.DELETE_TEMPLATE").getSql(), 	new SqlParameterValue(Types.NUMERIC, content.getTemplateId()));
		deleteTemplateProperties(content.getTemplateId());
		removeTemplateBody(content);
	}
	public void updateTemplate(Template content) {
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_WEB.UPDATE_TEMPLATE").getSql(), 	
				new SqlParameterValue (Types.NUMERIC, content.getObjectType() ), 
				new SqlParameterValue (Types.NUMERIC, content.getObjectId() ), 
				new SqlParameterValue (Types.VARCHAR, content.getTemplateType()), 
				new SqlParameterValue (Types.VARCHAR, content.getTitle()), 
				new SqlParameterValue(Types.VARCHAR, content.getLocation()),
				new SqlParameterValue(Types.DATE, content.getModifier().getUserId()),
				new SqlParameterValue(Types.DATE, content.getModifiedDate()),
				new SqlParameterValue (Types.NUMERIC, content.getTemplateId()) );			
		setTemplateProperties(content.getTemplateId(), content.getProperties());
		removeTemplateBody(content);
		saveTemplateBody(content);
	}
	public Template getTemplate(long contentId) {
		TemplateImpl impl = null ;
		try {
			
			impl = getExtendedJdbcTemplate().queryForObject(getBoundSql("ARCHITECTURE_WEB.SELECT_TEMPLATE_BY_ID").getSql(), templateMapper, new SqlParameterValue(Types.NUMERIC, contentId ) );
			impl.setProperties(getTemplateProperties( impl.getTemplateId() ));
			impl.setBody(getTemplateBody(impl));
			
		} catch (IncorrectResultSizeDataAccessException e) {
			if(e.getActualSize() > 1)
	        {
	            log.warn((new StringBuilder()).append("Multiple occurrances of the same TEMPLATE ID found: ").append(contentId).toString());
	            throw e;
	        }
		} catch (DataAccessException e) {
			 String message = (new StringBuilder()).append("Failure attempting to load TEMPLATE by ID : ").append(contentId).append(".").toString();
			 log.fatal(message, e);
		}	
		return impl;
	}
	
	public List<Long> getTemplateIds(int objectType, long objectId) {
		return getExtendedJdbcTemplate().query(
				getBoundSql("ARCHITECTURE_WEB.SELECT_TEMPLATE_IDS_BY_OBJECT_TYPE_AND_OBJECT_ID").getSql(),
				new RowMapper<Long>(){
					public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
						return rs.getLong(1);
					}
				},
				new SqlParameterValue (Types.NUMERIC, objectType ), new SqlParameterValue (Types.NUMERIC, objectId ));		
	}
	
	public List<Long> getTemplateIds(int objectType, long objectId, int startIndex, int numResults) {
		return getExtendedJdbcTemplate().queryScrollable(
				getBoundSql("ARCHITECTURE_WEB.SELECT_TEMPLATE_IDS_BY_OBJECT_TYPE_AND_OBJECT_ID").getSql(), 
				startIndex, 
				numResults, 
				new Object[]{objectType, objectId}, 
				new int[]{Types.NUMERIC, Types.NUMERIC}, Long.class);
	}
	
	public int getTemplateCount(int objectType, long objectId) {
		return getExtendedJdbcTemplate().queryForInt(
				getBoundSql("ARCHITECTURE_WEB.COUNT_TEMPLATE_BY_OBJECT_TYPE_AND_OBJECT_ID").getSql(), 
				new SqlParameterValue(Types.NUMERIC, objectType ), 
				new SqlParameterValue(Types.NUMERIC, objectId ));
	}
	
	public void removeTemplateBody(Template content){		
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_WEB.REMOVE_TEMPLATE_BODY").getSql(), 	new SqlParameterValue(Types.NUMERIC, content.getTemplateId()));
	}
	
	public void saveTemplateBody(Template content){		
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_WEB.CREATE_TEMPLATE_BODY").getSql(), 
				new SqlParameterValue ( Types.NUMERIC, content.getTemplateId()), 
				new SqlParameterValue ( Types.CLOB,  new SqlLobValue(content.getBody(), getLobHandler() ) ) 
		);		
	}
	
	public String getTemplateBody(Template content) {		
		return getExtendedJdbcTemplate().queryForObject(getBoundSql("ARCHITECTURE_WEB.SELECT_TEMPLATE_BODY_BY_ID").getSql(), 
				String.class, new SqlParameterValue (Types.NUMERIC, content.getTemplateId()));
	}
	
}
