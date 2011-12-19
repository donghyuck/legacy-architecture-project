package architecture.ee.web.model.impl;

import java.io.Serializable;
import java.util.Map;

import architecture.ee.web.ui.menu.MenuItem;

public class MenuItemModelImpl extends DefaultModelObject implements MenuItem {
	
	
	private long menuId = -1L;
	
	private long menuItemId = -1L;
	
	private long parentMenuItemId = -1L;
		
	private String name;
	
	private String title;
	
	private String localtion;
	
	private boolean enabled;
	
	private int index = 0;
	/**
	 */
	private Map<String, String> properties;

	public long getMenuId() {
		return menuId;
	}

	public void setMenuId(long menuId) {
		this.menuId = menuId;
	}

	public long getMenuItemId() {
		return menuItemId;
	}

	public void setMenuItemId(long menuItemId) {
		this.menuItemId = menuItemId;
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
		return localtion;
	}

	public void setLocation(String localtion) {
		this.localtion = localtion;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public long getParentId() {
		return parentMenuItemId;
	}

	public void setParentId(long menuItemId) {
		this.parentMenuItemId = menuItemId;
	}

	public String Title() {
		return title;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public Serializable getPrimaryKeyObject() {
		return getMenuItemId();
	}
	
	public String toString() {
		return String.format("{ mentId:%s, name:%s, title=%s, location=%s, enabled=%s, parent=%s, index=%s, properties=%s }",
				this.getMenuId(), getName(), getTitle(), getLocation(), isEnabled(), getParentId(), getIndex(), getProperties());
	}

}