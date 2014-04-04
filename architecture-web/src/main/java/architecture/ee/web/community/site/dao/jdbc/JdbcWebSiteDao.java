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
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import architecture.common.user.UserTemplate;
import architecture.user.impl.ComapanyImpl;
import architecture.ee.jdbc.property.dao.ExtendedPropertyDao;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;
import architecture.ee.web.community.site.DefaultWebSite;
import architecture.ee.web.community.site.WebSite;
import architecture.ee.web.community.site.dao.WebSiteDao;

public class JdbcWebSiteDao extends ExtendedJdbcDaoSupport implements WebSiteDao  {

	private ExtendedPropertyDao extendedPropertyDao;	
	private String sequencerName = "WEBSITE";
	private String webSitePropertyTableName = "V2_WEBSITE_PROPERTY";
	private String webSitePropertyPrimaryColumnName = "WEBSITE_ID";
		
	
	private final RowMapper<WebSite> announceMapper = new RowMapper<WebSite>(){
		public WebSite mapRow(ResultSet rs, int rowNum) throws SQLException {
			DefaultWebSite site = new DefaultWebSite(rs.getLong("WEBSITE_ID"));
			
			site.setName(rs.getString("NAME"));
			site.setDescription(rs.getString("DESCRIPTION"));
			site.setDisplayName(rs.getString("DISPLAY_NAME"));
			site.setAllowAnonymousAccess(rs.getInt("PUBLIC_SHARED") == 1 ? true : false);
			site.setEnabled(rs.getInt("ENABLED") == 1 ? true : false );
			site.setUrl(rs.getString("URL"));
						
			site.setCompany( new CompanyImpl( rs.getLong("COMPANY_ID") ) );
			site.setUser( new UserTemplate( rs.getLong("USER_ID") ) );
	
			site.setCreationDate(rs.getDate("CREATION_DATE"));
			site.setModifiedDate(rs.getDate("MODIFIED_DATE"));					
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
	
	
}
