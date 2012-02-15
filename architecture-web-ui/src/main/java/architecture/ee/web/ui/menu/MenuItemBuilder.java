package architecture.ee.web.ui.menu;

import java.util.Map;

import architecture.ee.web.model.impl.MenuItemModelImpl;

public class MenuItemBuilder {

	
	private MenuItemModelImpl model = new MenuItemModelImpl();
	

	public MenuItemBuilder setMenuId(long menuId){
		model.setMenuId(menuId);
		return this;
	}

	public MenuItemBuilder setMenuItemId(long menuItemId){
		model.setMenuItemId(menuItemId);
		return this;
	}
	
	public MenuItemBuilder setParentId(long menuItemId){
		model.setParentId(menuItemId);
		return this;
	}
	
	public MenuItemBuilder setName(String name){
		model.setName(name);
		return this;
	}
	
	public MenuItemBuilder setTitle(String title){
		model.setTitle(title);
		return this;
	}

	public MenuItemBuilder setEnabled(boolean enabled){
		model.setEnabled(enabled);
		return this;
	}
	
	public MenuItemBuilder setLocation(String localtion){
		model.setLocation(localtion);
		return this;
	}
	
	public MenuItemBuilder setIndex(int index){
		model.setIndex(index);
		return this;
	}
	
	public MenuItemBuilder setProperties(Map<String, String> properties){
		model.setProperties(properties);
		return this;
	}

	public MenuItem build() {
        return model;
	}
}
