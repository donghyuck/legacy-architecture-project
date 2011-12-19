package architecture.ee.web.ui.menu.tree;

import architecture.ee.web.ui.menu.MenuItem;
import architecture.ee.web.util.tree.Tree;

public class MenuTree extends Tree<MenuItem> {

	private long menuId = -1L;
	private String name;
	private String title;
	private String location;
	private boolean enabled;	

/*	public MenuTree(Menu menu) {
		this.menuId = menu.getMenuId();
		this.name = menu.getName();
		this.title = menu.getTitle();
		this.location = menu.getLocation();
		this.enabled = menu.isEnabled();
		
	}*/

	public MenuTree() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	protected long getMenuId() {
		return menuId;
	}

	protected void setMenuId(long menuId) {
		this.menuId = menuId;
	}

}
