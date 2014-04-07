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
package architecture.ee.web.community.site.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;

import architecture.common.user.CompanyTemplate;
import architecture.common.user.UserTemplate;
import architecture.ee.jdbc.property.dao.ExtendedPropertyDao;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;
import architecture.ee.web.community.site.DefaultWebSite;
import architecture.ee.web.site.WebSite;
import architecture.ee.web.site.WebSiteNotFoundException;
import architecture.ee.web.site.dao.WebSiteDao;

public class JdbcWebSiteDao extends ExtendedJdbcDaoSupport implements WebSiteDao  {

	private ExtendedPropertyDao extendedPropertyDao;	
	private String sequencerName = "WEBSITE";
	private String webSitePropertyTableName = "V2_WEBSITE_PROPERTY";
	private String webSitePropertyPrimaryColumnName = "WEBSITE_ID";
		
	
	private final RowMapper<WebSite> siteMapper = new RowMapper<WebSite>(){
		public WebSite mapRow(ResultSet rs, int rowNum) throws SQLException {
			DefaultWebSite site = new DefaultWebSite(rs.getLong("WEBSITE_ID"));			
			site.setName(rs.getString("NAME"));
			site.setDescription(rs.getString("DESCRIPTION"));
			site.setDisplayName(rs.getString("DISPLAY_NAME"));
			site.setAllowAnonymousAccess(rs.getInt("PUBLIC_SHARED") == 1 ? true : false);
			site.setEnabled(rs.getInt("ENABLED") == 1 ? true : false );
			site.setUrl(rs.getString("URL"));						
			site.setCompany( new CompanyTemplate( rs.getLong("COMPANY_ID") ) );
			site.setUser( new UserTemplate( rs.getLong("USER_ID") ) );	
			site.setCreationDate(rs.getTimestamp("CREATION_DATE"));
			site.setModifiedDate(rs.getTimestamp("MODIFIED_DATE"));					
			return site;
		}		
	};
		/**
		WEBSITE_ID	NUMBER(38,0)
		NAME	VARCHAR2(255 BYTE)
		DESCRIPTION	VARCHAR2(1000 BYTE)
		DISPLAY_NAME	VARCHAR2(255 BYTE)
		URL	VARCHAR2(255 BYTE)
		PUBLIC_SHARED	NUMBER(1,0)
		ENABLED	NUMBER(1,0)
		COMPANY_ID	NUMBER(38,0)
		USER_ID	NUMBER(38,0)
		CREATION_DATE	DATE
		MODIFIED_DATE	DATE
		 */
	
	public Long nextId( ){
		return getNextId(sequencerName);
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
	 * @return webSitePropertyTableName
	 */
	public String getWebSitePropertyTableName() {
		return webSitePropertyTableName;
	}

	/**
	 * @param webSitePropertyTableName 설정할 webSitePropertyTableName
	 */
	public void setWebSitePropertyTableName(String webSitePropertyTableName) {
		this.webSitePropertyTableName = webSitePropertyTableName;
	}

	/**
	 * @return webSitePropertyPrimaryColumnName
	 */
	public String getWebSitePropertyPrimaryColumnName() {
		return webSitePropertyPrimaryColumnName;
	}

	/**
	 * @param webSitePropertyPrimaryColumnName 설정할 webSitePropertyPrimaryColumnName
	 */
	public void setWebSitePropertyPrimaryColumnName(
			String webSitePropertyPrimaryColumnName) {
		this.webSitePropertyPrimaryColumnName = webSitePropertyPrimaryColumnName;
	}

	public Map<String, String> getWebSiteProperties(long webSiteId) {
		return extendedPropertyDao.getProperties(webSitePropertyTableName, webSitePropertyPrimaryColumnName, webSiteId);
	}

	public void deleteWebSiteProperties(long webSiteId) {
		extendedPropertyDao.deleteProperties(webSitePropertyTableName, webSitePropertyPrimaryColumnName, webSiteId);
	}
	
	public void setWebSiteProperties(long webSiteId, Map<String, String> props) {
		extendedPropertyDao.updateProperties(webSitePropertyTableName, webSitePropertyPrimaryColumnName, webSiteId, props);
	}

	public void createWebSite(WebSite webSite) {
		long webSiteIdToUse = webSite.getWebSiteId();
		if( webSiteIdToUse < 0 )
			webSiteIdToUse = getNextId(sequencerName);
		((DefaultWebSite)webSite).setWebSiteId(webSiteIdToUse);
		
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_COMMUNITY.CREATE_WEBSITE").getSql(), 	
				new SqlParameterValue (Types.NUMERIC, webSite.getWebSiteId()),
				new SqlParameterValue (Types.VARCHAR, webSite.getName()), 
				new SqlParameterValue (Types.VARCHAR, webSite.getDescription()), 
				new SqlParameterValue(Types.VARCHAR, webSite.getDisplayName()),
				new SqlParameterValue(Types.VARCHAR, webSite.getUrl()),
				new SqlParameterValue(Types.NUMERIC, webSite.isAllowAnonymousAccess() ? 1: 0),
				new SqlParameterValue(Types.NUMERIC, webSite.isEnabled() ? 1 : 0),
				new SqlParameterValue(Types.NUMERIC, webSite.getCompany().getCompanyId()),
				new SqlParameterValue(Types.NUMERIC, webSite.getUser().getUserId()),
				new SqlParameterValue(Types.DATE, webSite.getModifiedDate()),
				new SqlParameterValue(Types.DATE, webSite.getCreationDate())
		);			
		setWebSiteProperties(webSite.getWebSiteId(), webSite.getProperties());					
	}

	public void updateWebSite(WebSite webSite) {
		
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_COMMUNITY.UPDATE_WEBSITE").getSql(), 	
				new SqlParameterValue (Types.VARCHAR, webSite.getName()), 
				new SqlParameterValue (Types.VARCHAR, webSite.getDescription()), 
				new SqlParameterValue(Types.VARCHAR, webSite.getDisplayName()),
				new SqlParameterValue(Types.VARCHAR, webSite.getUrl()),
				new SqlParameterValue(Types.NUMERIC, webSite.isAllowAnonymousAccess() ? 1: 0),
				new SqlParameterValue(Types.NUMERIC, webSite.isEnabled() ? 1 : 0),
				new SqlParameterValue(Types.TIMESTAMP, webSite.getModifiedDate()),
				new SqlParameterValue (Types.NUMERIC, webSite.getWebSiteId() ) );			
		setWebSiteProperties(webSite.getWebSiteId(), webSite.getProperties());				
	}

	public WebSite getWebSiteById(long webSiteId) throws WebSiteNotFoundException {
		try{
			WebSite site=  getExtendedJdbcTemplate().queryForObject(
				getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_WEBSITE_BY_ID").getSql(), 
				siteMapper, 
				new SqlParameterValue (Types.NUMERIC, webSiteId ));			
			site.setProperties(getWebSiteProperties(webSiteId));
			return site;
		} catch (DataAccessException e) {
			throw new WebSiteNotFoundException(e);
		}		
	}

	public WebSite getWebSiteByName(String name) throws WebSiteNotFoundException {
		try{
			return getExtendedJdbcTemplate().queryForObject(
				getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_WEBSITE_BY_NAME").getSql(), 
				siteMapper, 
				new SqlParameterValue (Types.VARCHAR, name ));			
		} catch (DataAccessException e) {
			throw new WebSiteNotFoundException(e);
		}				
	}

	public WebSite getWebSiteByUrl(String url)  throws WebSiteNotFoundException {
		try{
			return getExtendedJdbcTemplate().queryForObject(
				getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_WEBSITE_BY_URL").getSql(), 
				siteMapper, 
				new SqlParameterValue (Types.VARCHAR, url ));			
		} catch (DataAccessException e) {
			throw new WebSiteNotFoundException(e);
		}				
	}

	public int getWebSiteCount(long companyId) {
		return getExtendedJdbcTemplate().queryForInt(
				getBoundSql("ARCHITECTURE_COMMUNITY.COUNT_COMPANY_WEBSITE").getSql(), 
				new SqlParameterValue(Types.NUMERIC, companyId ));
	}

	public List<Long> getWebSiteIds(long companyId) {
		return getExtendedJdbcTemplate().queryForList(getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_COMPANY_WEBSITE_IDS").getSql(), Long.class, new SqlParameterValue(Types.NUMERIC, companyId ));
	}

	public List<Long> findWebSitesByUrl(String url) {
		return getExtendedJdbcTemplate().queryForList(
			getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_COMPANY_WEBSITE_IDS_BY_URL").getSql(), 
			Long.class, new SqlParameterValue(Types.VARCHAR, '%' + url + '%' ));
	}
	
	public List<Long> findWebSitesByName(String name) {
		return getExtendedJdbcTemplate().queryForList(
			getBoundSql("ARCHITECTURE_COMMUNITY.SELECT_COMPANY_WEBSITE_IDS_BY_NAME").getSql(), 
			Long.class, new SqlParameterValue(Types.VARCHAR, '%' + name + '%' ));
	}	
	
}
