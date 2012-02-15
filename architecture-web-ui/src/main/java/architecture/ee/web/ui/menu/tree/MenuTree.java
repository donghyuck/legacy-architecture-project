package architecture.ee.web.ui.menu.tree;

import java.util.ArrayList;
import java.util.List;

import architecture.ee.web.ui.menu.Menu;
import architecture.ee.web.ui.menu.MenuItem;
import architecture.ee.web.ui.menu.MenuItemBuilder;
import architecture.ee.web.util.tree.Node;
import architecture.ee.web.util.tree.Tree;

public class MenuTree extends Tree<MenuItem> {

	private long menuId = -1L;
	private String name;
	private String title;
	private String location;
	private boolean enabled;	
	
	public MenuTree(Menu menu) {
		
		this.menuId = menu.getMenuId();
		this.name = menu.getName();
		this.title = menu.getTitle();
		this.location = menu.getLocation();
		this.enabled = menu.isEnabled();		
				
		MenuItemBuilder builder = new MenuItemBuilder();
		builder.setParentId(-2L).setMenuId(menuId).setName(name).setTitle(title).setLocation(location).setEnabled(enabled);		
		Node<MenuItem> rootElement = toNode(builder.build());
	
		
		setRootElement(rootElement);
	}	
	
	private void processChildNode( Node<MenuItem> element, List <MenuItem> items ){		
		List<Node<MenuItem>> list = new ArrayList<Node<MenuItem>>();		
		long itemId = element.getData().getMenuId();		
		for(MenuItem item : items ){
			if(item.getParentId() == itemId )
			{
				Node<MenuItem> child = toNode(item);
				processChildNode(child, items);
				element.addChild(child);
			}
		}		
	}

	private Node<MenuItem> toNode(MenuItem item){
		return new Node<MenuItem>(item);
	}
	
	
	public Node<MenuItem> getMenuItemByMenuId(long menuId){		
		List<Node<MenuItem>> list = toList();
		for(Node<MenuItem> n : list)
			if(n.getData().getMenuId() == menuId)
				return n;
		return null;
	}
		
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

	public long getMenuId() {
		return menuId;
	}

	public void setMenuId(long menuId) {
		this.menuId = menuId;
	}

}