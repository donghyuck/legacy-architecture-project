package architecture.ee.web.ui.menu;

import java.util.List;

import architecture.ee.web.ui.menu.tree.MenuTree;


public interface MenuBuilder {

	public MenuTree getTreeById(long menuId);
	
	public MenuTree getTreeByName(String name);
	
	public Menu getMenuById(long menuId);
	
	public Menu getMenuByName(String name);
	
	public List<Menu> getAllMenus();
	
	public int getTotalMenuCount();
		
}
