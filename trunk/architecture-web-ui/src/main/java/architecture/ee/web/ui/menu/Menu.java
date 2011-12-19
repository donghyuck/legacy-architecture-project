package architecture.ee.web.ui.menu;

import java.util.List;

import architecture.ee.web.model.ModelObject;

public interface Menu extends ModelObject {

	public long getMenuId();

	public void setMenuId(long menuId);

	public String getName();

	public void setName(String name);

	public String getTitle();

	public void setTitle(String title);

	public String getLocation();

	public void setLocation(String location);

	public boolean isEnabled();

	public void setEnabled(boolean enabled);
	
	public List<MenuItem> getMenuItems();
	
	public void setMenuItems(List<MenuItem> items); 

}
