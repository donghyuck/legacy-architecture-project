package architecture.ee.web.struts2.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import architecture.ee.util.OutputFormat;
import architecture.ee.web.struts2.action.FrameworkActionSupport;
import architecture.ee.web.ui.menu.Menu;
import architecture.ee.web.ui.menu.MenuBuilder;
import architecture.ee.web.ui.menu.tree.MenuTree;

import com.opensymphony.xwork2.Preparable;

public class MainMenuAction  extends FrameworkActionSupport implements Preparable {

	public static final String VIEW_MENUS = "menus";
	
	private MenuBuilder menuBuilder;	
	
	public MenuBuilder getMenuBuilder() {
		return menuBuilder;
	}

	public void setMenuBuilder(MenuBuilder menuBuilder) {
		this.menuBuilder = menuBuilder;
	}

	public void prepare() throws Exception {
		models.clear();
	}

	public List<Menu> getAllMenus(){
		return menuBuilder.getAllMenus();
	}
	
	public MenuTree getMenuTreeById(long menuId){
		return menuBuilder.getTreeById(menuId);
	}

	public MenuTree getMenuTreeByName(String name){
		return menuBuilder.getTreeByName(name);
	}
	
	public String execute()
    {
    	models.clear();
    	if(VIEW_MENUS.equals( getView() )){
    		if( OutputFormat.stingToOutputFormat( getDataType() ) == OutputFormat.JSON ){ 	
    			
    			List<Menu> menus = getAllMenus();
    			List<Map> list = new ArrayList<Map>(menus.size());    
    			for( Menu menu : menus){
    				Map<String, Object> row = new HashMap<String, Object>();
    				row.put("ID", menu.getMenuId());
    				row.put("name", menu.getName());
    				row.put("title", menu.getTitle());    				
    				row.put("location", menu.getLocation());
    				row.put("enabled", menu.isEnabled());
    				row.put("items", menu.getMenuItems());
    				list.add(row);
    			}    			
    			models.put("menus", list );    			
    			return JSON;
    		}else if ( OutputFormat.stingToOutputFormat( getDataType() ) == OutputFormat.XML ){     
    			models.put("menus", getAllMenus());
    			return XML;
    		}
    	}    	
        return SUCCESS;
    }
	
}
