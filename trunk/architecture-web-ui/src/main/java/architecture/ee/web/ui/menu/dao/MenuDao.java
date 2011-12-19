package architecture.ee.web.ui.menu.dao;

import java.util.List;

import architecture.ee.web.ui.menu.Menu;

public interface MenuDao {

	public int getTotalMenuCount();
	
	public int getEnabledMenuCount();
	
	public void createMenu(Menu menu);
	
	public void update(Menu menu);
	
	public List<Menu> getAllMenus();
	
	public List<Menu> getEnabledMenus();
	
	public Menu getMenuByName(String name);
	
	public Menu getMenuById(long menuId);
	
	public List<Long> getAllMenuIds ();
	
}
