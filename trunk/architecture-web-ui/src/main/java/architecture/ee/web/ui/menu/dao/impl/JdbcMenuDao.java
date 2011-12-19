package architecture.ee.web.ui.menu.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import architecture.ee.jdbc.property.dao.ExtendedPropertyDao;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;
import architecture.ee.web.model.impl.MenuItemModelImpl;
import architecture.ee.web.model.impl.MenuModelImpl;
import architecture.ee.web.ui.menu.Menu;
import architecture.ee.web.ui.menu.MenuItem;
import architecture.ee.web.ui.menu.dao.MenuDao;

public class JdbcMenuDao extends ExtendedJdbcDaoSupport  implements MenuDao{

	private final RowMapper<Menu> rowMapper = new RowMapper<Menu>(){
		public Menu mapRow(ResultSet rs, int rowNum) throws SQLException {  			
			MenuModelImpl model = new MenuModelImpl();	
			//MENU_ID, NAME, TITLE, LOCATION, ENABLED, CREATION_DATE, MODIFIED_DATE
			model.setMenuId(rs.getLong(1));
			model.setName(rs.getString(2));
			model.setTitle(rs.getString(3));
			model.setLocation(rs.getString(4));
			model.setEnabled( rs.getInt(5)==1);
			model.setCreationDate(rs.getDate(6));
			model.setModifiedDate(rs.getDate(7));
			return model;
		}};
		
	private final RowMapper<MenuItem> rowMapper2 = new RowMapper<MenuItem>() {
		public MenuItem mapRow(ResultSet rs, int rowNum) throws SQLException {
			MenuItemModelImpl model = new MenuItemModelImpl();
			model.setMenuId(rs.getLong(1));
			model.setMenuItemId(rs.getLong(2));
			model.setParentId(rs.getLong(3));
			model.setName(rs.getString(4));
			model.setTitle(rs.getString(5));
			model.setLocation(rs.getString(6));
			model.setEnabled(rs.getInt(7) == 1);
			model.setIndex(rs.getInt(8));
			model.setCreationDate(rs.getDate(9));
			model.setModifiedDate(rs.getDate(10));			
			return model;
		}
	};
			
			
	private ExtendedPropertyDao extendedPropertyDao;
	
	private String sequencerName = "MENU";
	
	private String menuItemPropertyTableName = "V2_MENU_ITEM_PROPERTY";
	
	private String menuItemPropertyPrimaryColumnName = "MENU_ITEM_ID";

	/**
	 * @param menuItemPropertyTableName
	 */
	public void setMenuItemPropertyTableName(String menuItemPropertyTableName) {
		this.menuItemPropertyTableName = menuItemPropertyTableName;
	}

	/**
	 * @param menuItemPropertyPrimaryColumnName
	 */
	public void setUserPropertyPrimaryColumnName(String menuItemPropertyPrimaryColumnName) {
		this.menuItemPropertyPrimaryColumnName = menuItemPropertyPrimaryColumnName;
	}

	/**
	 * @param extendedPropertyDao
	 */
	public void setExtendedPropertyDao(ExtendedPropertyDao extendedPropertyDao) {
		this.extendedPropertyDao = extendedPropertyDao;
	}
	
	public int getTotalMenuCount(){
		return getExtendedJdbcTemplate().queryForInt(getBoundSql("FRAMEWORK_WEB_UI_V2.COUNT_ALL_MENU").getSql());
	}
	
	public int getEnabledMenuCount(){
		return getExtendedJdbcTemplate().queryForInt(getBoundSql("FRAMEWORK_WEB_UI_V2.COUNT_ENABLED_MENU").getSql());
	}

	public List<Menu> getAllMenus() {
		List<Long> ids = getAllMenuIds();
		List<Menu> list = new ArrayList<Menu>(ids.size());
		
		for(Long id : ids){
			list.add(getMenuById(id));
		}
		
		return list;
	}

	public List<Menu> getEnabledMenus() {
		List<Long> ids = getEnabledMenuIds();
		List<Menu> list = new ArrayList<Menu>(ids.size());
		
		for(Long id : ids){
			list.add(getMenuById(id));
		}
		
		return list;
	}
	
	
	public List<Long> getAllMenuIds () {
		return getExtendedJdbcTemplate().queryForList(
				getBoundSql("FRAMEWORK_WEB_UI_V2.SELECT_ALL_MENU_IDS").getSql(), Long.class );
	}
	
	public List<Long> getEnabledMenuIds () {
		return getExtendedJdbcTemplate().queryForList(
				getBoundSql("FRAMEWORK_WEB_UI_V2.SELECT_ENABLED_MENU_IDS").getSql(), Long.class );
	}
	
	
	public void createMenu(Menu menu) {		
		long id = getNextId(sequencerName);
		if("".equals(menu.getLocation()))
			menu.setLocation(null);	
		Date now = new Date(System.currentTimeMillis());
		menu.setCreationDate(now);
		menu.setModifiedDate(now);
	    
		//MENU_ID, NAME, TITLE, LOCATION, ENABLED, CREATION_DATE, MODIFIED_DATE
		getExtendedJdbcTemplate().update(
			getBoundSql("FRAMEWORK_WEB_UI_V2.CREATE_MENU").getSql(), 
			new Object[]{id, menu.getName(), menu.getTitle(), menu.getLocation(), menu.isEnabled() ? 1 : 0, menu.getCreationDate(), menu.getModifiedDate()}, 
			new int[]{Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.DATE, Types.DATE });
		
		menu.setMenuId(id);
		//setGroupProperties(group.getGroupId(), group.getProperties());
	}

	public void update(Menu menu) {
	    Date now = new Date(System.currentTimeMillis());
	    menu.setModifiedDate(now); 
		getExtendedJdbcTemplate().update(getBoundSql("FRAMEWORK_WEB_UI_V2.UPDATE_MENU").getSql(), 
			new Object[]{ menu.getName(), menu.getTitle(), menu.getLocation(), menu.isEnabled() ? 1 : 0, menu.getModifiedDate(), menu.getMenuId()}, 
			new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.DATE, Types.INTEGER });		
		//setGroupProperties(group.getGroupId(), group.getProperties());
	}
	
	public void deleteMenu(long menuId) {
		getExtendedJdbcTemplate().update(getBoundSql("FRAMEWORK_WEB_UI_V2.DELETE_MENU").getSql(), new Object[]{menuId}, new int[]{Types.INTEGER});
		//deleteGroupProperties(groupId);
	
	}
	
	public Menu getMenuByName(String name) {
		return getMenuByName(name, false);
	}

	public Menu getMenuByName(String name, boolean caseInsensitive) {
		String n = caseInsensitive ? name.toLowerCase() : name;
		Menu menu = getExtendedJdbcTemplate().queryForObject(
			getBoundSqlWithAdditionalParameter("FRAMEWORK_WEB_UI_V2.SELECT_MENU_BY_NAME", caseInsensitive).getSql(), 
			new Object[]{n}, new int[]{Types.VARCHAR}, rowMapper);		
		
		menu.setMenuItems(getMenuItems( menu.getMenuId()));
		
		return menu;
	}
	
	public Menu getMenuById(long menuId) {
		Menu menu = getExtendedJdbcTemplate().queryForObject(
				getBoundSql("FRAMEWORK_WEB_UI_V2.SELECT_MENU_BY_ID").getSql(), 
				new Object[]{menuId}, new int[]{Types.INTEGER}, rowMapper);	
		
		menu.setMenuItems(getMenuItems( menu.getMenuId()));
		
		return menu;
	}	
	
	protected List<MenuItem> getMenuItems(long menuId) {		
		List<MenuItem> list =  getExtendedJdbcTemplate().query(
				getBoundSql("FRAMEWORK_WEB_UI_V2.SELECT_MENU_ITEMS_BY_ID").getSql(), 
				new Object[]{menuId}, new int[]{Types.INTEGER}, rowMapper2);
		
		for(MenuItem item : list){
			Map<String, String> properties = getMenuItemProperties(item.getMenuItemId());
			item.setProperties(properties);
		}		
		return list;
	}		

	public Map<String, String> getMenuItemProperties(long itemId) {
		return extendedPropertyDao.getProperties(menuItemPropertyTableName, menuItemPropertyPrimaryColumnName, itemId);
	}

	public void getMenuItemProperties(long itemId, Map<String, String> props) {
		extendedPropertyDao.updateProperties(menuItemPropertyTableName, menuItemPropertyPrimaryColumnName, itemId, props);
	}

	public void setMenuItemProperties(long itemId, Map<String, String> props) {
		extendedPropertyDao.updateProperties(menuItemPropertyTableName, menuItemPropertyPrimaryColumnName, itemId, props);
	}
	
	public void deleteMenuItemProperties(long itemId) {
		extendedPropertyDao.deleteProperties(menuItemPropertyTableName, menuItemPropertyPrimaryColumnName, itemId);	
	}
	
	
}
