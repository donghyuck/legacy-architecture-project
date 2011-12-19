package architecture.ee.web.ui.menu.impl;

import java.util.List;

import architecture.ee.web.model.impl.MenuItemModelImpl;
import architecture.ee.web.ui.menu.Menu;
import architecture.ee.web.ui.menu.MenuBuilder;
import architecture.ee.web.ui.menu.MenuItem;
import architecture.ee.web.ui.menu.dao.MenuDao;
import architecture.ee.web.ui.menu.tree.MenuItemNode;
import architecture.ee.web.ui.menu.tree.MenuTree;
import architecture.ee.web.util.tree.Node;

public class MenuBuilderImpl implements MenuBuilder {

	private MenuDao menuDao;

	public MenuDao getMenuDao() {
		return menuDao;
	}

	public void setMenuDao(MenuDao menuDao) {
		this.menuDao = menuDao;
	}

	public MenuTree getTreeById(long menuId) {
		Menu menu = menuDao.getMenuById(menuId);
		return getMenuTree(menu);
	}

	public MenuTree getTreeByName(String name) {
		Menu menu = menuDao.getMenuByName(name);
		return getMenuTree(menu);
	}
	
	protected MenuTree getMenuTree(Menu menu) {
		MenuTree tree = new MenuTree();

		MenuItemNode root = new MenuItemNode(getRootMenuItem(menu));
		List<MenuItem> items = menu.getMenuItems();
		
		// depth 1
		for (MenuItem item : items) {
			if (item.getParentId() == -1L)
				root.addChild(new MenuItemNode(item));
			//items.remove(item);
		}

		// depth 2
		for (MenuItem item : items) {
			for (Node<MenuItem> c : root.getChildren()) {
				if (item.getParentId() == c.getData().getMenuItemId()) {
					c.addChild(new MenuItemNode(item));
					//items.remove(item);
				}	
			}
		}
		
		// depth 3
		for (MenuItem item : items) {
			for (Node<MenuItem> c : root.getChildren()) {
				for (Node<MenuItem> cc : c.getChildren()) {
					if (item.getParentId() == cc.getData().getMenuItemId()) {
						cc.addChild(new MenuItemNode(item));
						//items.remove(item);
					}
				}
			}
		}		
		tree.setRootElement(root);		
		return tree;
	}
	
	protected MenuItem getRootMenuItem(Menu menu){
		MenuItem model = new MenuItemModelImpl();
		model.setTitle(menu.getTitle());
		model.setMenuId(menu.getMenuId());
		model.setName(menu.getName());
		model.setLocation(menu.getLocation());
		return model;
	}

	public Menu getMenuById(long menuId) {
		return menuDao.getMenuById(menuId);
	}

	public Menu getMenuByName(String name) {
		return menuDao.getMenuByName(name);
	}

	public List<Menu> getAllMenus() {
		return menuDao.getAllMenus();
	}

	public int getTotalMenuCount() {
		return menuDao.getTotalMenuCount();
	}

	
}
