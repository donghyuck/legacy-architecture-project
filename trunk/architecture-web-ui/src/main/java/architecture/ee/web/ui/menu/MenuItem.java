package architecture.ee.web.ui.menu;

import java.util.Map;

import architecture.ee.web.model.ModelObject;

public interface MenuItem extends ModelObject {

	public long getMenuId();

	public void setMenuId(long menuId);
	
	public long getMenuItemId();

	public void setMenuItemId(long menuItemId);	
	
	public long getParentId();
	
	public void setParentId(long menuItemId);
	
	public String getName();
	
	public void setName(String name);
	
	public String getTitle ();
	
	public void setTitle(String title);
	
	public boolean isEnabled();

	public void setEnabled(boolean enabled);
	
	public String getLocation();

	public void setLocation(String localtion);
	
	public int getIndex();
	
	public void setIndex(int index);	
	
	public abstract Map<String, String> getProperties();
	
	public abstract void setProperties(Map<String, String> properties);
	
}