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
package architecture.ee.web.navigator.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.support.SqlLobValue;

import architecture.ee.jdbc.property.dao.ExtendedPropertyDao;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;
import architecture.ee.web.navigator.DefaultMenu;
import architecture.ee.web.navigator.Menu;
import architecture.ee.web.navigator.dao.MenuDao;

public class JdbcMenuDao extends ExtendedJdbcDaoSupport implements MenuDao {
	
	private final RowMapper<Menu> menuMapper = new RowMapper<Menu>(){
		public Menu mapRow(ResultSet rs, int rowNum) throws SQLException {
			DefaultMenu menu = new DefaultMenu();			
			menu.setMenuId(rs.getLong("MENU_ID"));
			menu.setName(rs.getString("NAME"));
			menu.setTitle(rs.getString("TITLE"));
			menu.setLocation(rs.getString("LOCATION"));
			menu.setEnabled( rs.getInt("MENU_ENABLED") == 1 ); 
			menu.setCreationDate(rs.getDate("CREATION_DATE"));
			menu.setModifiedDate(rs.getDate("MODIFIED_DATE"));			
			return menu;
		}		
	};
	

	private ExtendedPropertyDao extendedPropertyDao;	
	private String sequencerName = "MENU";
	private String menuPropertyTableName = "V2_MENU_PROPERTY";
	private String menuPropertyPrimaryColumnName = "MENU_ID";
	

	public void setExtendedPropertyDao(ExtendedPropertyDao extendedPropertyDao) {
		this.extendedPropertyDao = extendedPropertyDao;
	}
	
	
	public String getSequencerName() {
		return sequencerName;
	}


	public void setSequencerName(String sequencerName) {
		this.sequencerName = sequencerName;
	}

	public ExtendedPropertyDao getExtendedPropertyDao() {
		return extendedPropertyDao;
	}


	public String getMenuPropertyTableName() {
		return menuPropertyTableName;
	}


	public void setMenuPropertyTableName(String menuPropertyTableName) {
		this.menuPropertyTableName = menuPropertyTableName;
	}


	public String getMenuPropertyPrimaryColumnName() {
		return menuPropertyPrimaryColumnName;
	}


	public void setMenuPropertyPrimaryColumnName(
			String menuPropertyPrimaryColumnName) {
		this.menuPropertyPrimaryColumnName = menuPropertyPrimaryColumnName;
	}


	public Map<String, String> getMenuProperties(long menuId) {
		return extendedPropertyDao.getProperties(menuPropertyTableName, menuPropertyPrimaryColumnName, menuId);
	}

	public void deleteMenuProperties(long menuId) {
		extendedPropertyDao.deleteProperties(menuPropertyTableName, menuPropertyPrimaryColumnName, menuId);
	}
	
	public void setMenuProperties(long menuId, Map<String, String> props) {
		extendedPropertyDao.updateProperties(menuPropertyTableName, menuPropertyPrimaryColumnName, menuId, props);
	}

	
	public Menu getMenuByName(String name) {
		
		if( StringUtils.isEmpty(name)){
			return null;
		}
		
		Menu menu = null ;
		try {
			menu = getExtendedJdbcTemplate().queryForObject(getBoundSql("ARCHITECTURE_WEB.SELECT_MENU_BY_NAME").getSql(), menuMapper, new SqlParameterValue(Types.VARCHAR, name ) );
			menu.setProperties(getMenuProperties( menu.getMenuId() ));
			menu.setMenuData(getMenuXmlData(menu));
		} catch (IncorrectResultSizeDataAccessException e) {
			if(e.getActualSize() > 1)
	        {
	            log.warn((new StringBuilder()).append("Multiple occurrances of the same menu name found: ").append(menu).toString());
	            throw e;
	        }
		} catch (DataAccessException e) {
			 String message = (new StringBuilder()).append("Failure attempting to load menu by name : ").append(menu).append(".").toString();
			 log.fatal(message, e);
		}	
		return menu;
	}

	public Menu getMenuById(Long menuId) {
		Menu menu = null ;
		try {
			menu = getExtendedJdbcTemplate().queryForObject(getBoundSql("ARCHITECTURE_WEB.SELECT_MENU_BY_ID").getSql(), menuMapper, new SqlParameterValue(Types.NUMERIC, menuId ) );
			menu.setProperties(getMenuProperties( menu.getMenuId() ));
			menu.setMenuData(getMenuXmlData(menu));
		} catch (IncorrectResultSizeDataAccessException e) {
			if(e.getActualSize() > 1)
	        {
	            log.warn((new StringBuilder()).append("Multiple occurrances of the same menu ID found: ").append(menuId).toString());
	            throw e;
	        }
		} catch (DataAccessException e) {
			 String message = (new StringBuilder()).append("Failure attempting to load menu by ID : ").append(menuId).append(".").toString();
			 log.fatal(message, e);
		}	
		return menu;
	}

	public List<Long> getAllMenuIds() {
		return getExtendedJdbcTemplate().queryForList(getBoundSql("ARCHITECTURE_WEB.SELECT_ALL_MENU_ID").getSql(), Long.class);
	}
	
	public List<Long> getMenuIds(int startIndex, int numResults) {
		return getExtendedJdbcTemplate().queryScrollable(
				getBoundSql("ARCHITECTURE_WEB.SELECT_ALL_MENU_ID").getSql(), 
				startIndex, 
				numResults, 
				new Object[0], 
				new int[0], 
				Long.class);
	}
	
	/**
	 * 	
	 * 	
		CREATE TABLE V2_MENU (
	    	MENU_ID			          INTEGER NOT NULL,
		   	NAME                         VARCHAR2(255) NOT NULL,
		   	TITLE                           VARCHAR2(255) NOT NULL,
		   	LOCATION                   VARCHAR2(255) NOT NULL,
		    MENU_ENABLED           NUMBER(1, 0)  DEFAULT 1,
			CREATION_DATE            DATE DEFAULT  SYSDATE NOT NULL,
			MODIFIED_DATE            DATE DEFAULT  SYSDATE NOT NULL,	
	    	CONSTRAINT V2_MENU_PK PRIMARY KEY (MENU_ID)	    
	    );		
		
	 */
	public Menu updateMenu(Menu menu) {		
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_WEB.UPDATE_MENU").getSql(), 	
				new SqlParameterValue(Types.VARCHAR, menu.getName()),
				new SqlParameterValue(Types.VARCHAR, menu.getTitle()),
				new SqlParameterValue(Types.VARCHAR, menu.getLocation()),
				new SqlParameterValue(Types.NUMERIC, menu.isEnabled() ? 1 : 0 ),
				new SqlParameterValue(Types.DATE, menu.getModifiedDate()),
				new SqlParameterValue(Types.NUMERIC, menu.getMenuId()));		
		setMenuProperties(menu.getMenuId(), menu.getProperties());		
		removeMenuXmlData(menu);
		saveMenuXmlData(menu);
		return menu;
	}

	public Menu createMenu(Menu menu) {
		
		long menuId = getNextId(sequencerName);
		((DefaultMenu)menu).setMenuId(menuId);
		if("".equals(menu.getTitle()))
            menu.setTitle(null);
		if("".equals(menu.getLocation()))
            menu.setLoaction(null);
		
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_WEB.CREATE_MENU").getSql(), 	
				new SqlParameterValue(Types.NUMERIC, menu.getMenuId()),
				new SqlParameterValue(Types.VARCHAR, menu.getName()),
				new SqlParameterValue(Types.VARCHAR, menu.getTitle()),
				new SqlParameterValue(Types.VARCHAR, menu.getLocation()),
				new SqlParameterValue(Types.NUMERIC, menu.isEnabled() ? 1 : 0 ),
				new SqlParameterValue(Types.DATE, menu.getModifiedDate()),
				new SqlParameterValue(Types.DATE, menu.getCreationDate()));		
		setMenuProperties(menu.getMenuId(), menu.getProperties());	
		saveMenuXmlData(menu);		
		
		return menu;
	}


	public void removeMenu(Menu menu) {
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_WEB.REMOVE_MENU").getSql(), 	new SqlParameterValue(Types.NUMERIC, menu.getMenuId()));
		deleteMenuProperties(menu.getMenuId());		
		removeMenuXmlData(menu);
	}
	
	public void removeMenuXmlData(Menu menu){		
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_WEB.REMOVE_MENU_XML").getSql(), 	new SqlParameterValue(Types.NUMERIC, menu.getMenuId()));
	}
	
	public void saveMenuXmlData(Menu menu){		
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_WEB.CREATE_MENU_XML").getSql(), 
				new SqlParameterValue ( Types.NUMERIC, menu.getMenuId()), 
				new SqlParameterValue ( Types.CLOB,  new SqlLobValue(menu.getMenuData(), getLobHandler() ) ) 
		);		
	}
	
	public String getMenuXmlData(Menu menu) {		
		return getExtendedJdbcTemplate().queryForObject(getBoundSql("ARCHITECTURE_WEB.SELECT_MENU_XML_BY_ID").getSql(), 
				String.class, new SqlParameterValue (Types.NUMERIC, menu.getMenuId()));		
	}


	public int getTotalMenuCount() {
		return getExtendedJdbcTemplate().queryForInt(getBoundSql("ARCHITECTURE_WEB.COUNT_ALL_MENUS").getSql());
	}
	
}
