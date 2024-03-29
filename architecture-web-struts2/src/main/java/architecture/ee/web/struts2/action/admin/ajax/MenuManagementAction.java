/*
 * Copyright 2012, 2013 Donghyuck, Son
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package architecture.ee.web.struts2.action.admin.ajax;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import architecture.common.util.StringUtils;
import architecture.ee.web.navigator.DefaultMenu;
import architecture.ee.web.navigator.Menu;
import architecture.ee.web.navigator.MenuComponent;
import architecture.ee.web.navigator.MenuNotFoundException;
import architecture.ee.web.site.WebSite;
import architecture.ee.web.site.WebSiteNotFoundException;
import architecture.ee.web.struts2.action.support.WebSiteActionSupport;
import architecture.ee.web.util.ParamUtils;
import architecture.ee.web.util.WebSiteUtils;

public class MenuManagementAction  extends WebSiteActionSupport {

	private int pageSize = 0 ;
	private int startIndex = 0 ;    
	private Long menuId = -1L;
	private Long targetWebSiteId = -1L;
	private WebSite targetWebSite = null;
		
	//private CompanyManager companyManager ;
	
	
	
	//private Long companyId = 1L ;
/*
	private Company targetCompany;
*/	
	
	private String menuName ;
	

	
	
	public int getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	
	
	
	/*
	public void setTargetCompany(Company targetCompany) {
		this.targetCompany = targetCompany;
	}*/
	
	
	
	/**
	 * @return targetWebSiteId
	 */
	public Long getTargetWebSiteId() {
		return targetWebSiteId;
	}
	/**
	 * @param targetWebSiteId 설정할 targetWebSiteId
	 */
	public void setTargetWebSiteId(Long targetWebSiteId) {
		this.targetWebSiteId = targetWebSiteId;
	}
	/*
	public CompanyManager getCompanyManager() {
		return companyManager;
	}
	public void setCompanyManager(CompanyManager companyManager) {
		this.companyManager = companyManager;
	}
	*/
    public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
/*
	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	*/
	public int getTotalMenuCount(){    	
		return getMenuRepository().getTotalMenuCount();
	}

	public List<Menu> getTargetMenus(){
		if( pageSize > 0 ){  
			return getMenuRepository().getMenus(startIndex, pageSize);    		
		}	
		return getMenuRepository().getMenus();
	}
	
	public Menu getTargetMenu() throws MenuNotFoundException {
		return getMenuRepository().getMenu(menuId);
	}

	public MenuComponent getTargetMenuComponent( ) throws MenuNotFoundException {
		
		return getMenuRepository().getMenuComponent(getTargetMenu(), menuName);
	
	}
	
	public WebSite getTargetWebSite() throws WebSiteNotFoundException {		
		if( targetWebSite == null ){
			if( targetWebSiteId > 0 ){
				targetWebSite = WebSiteUtils.getWebSiteManager().getWebSiteById(targetWebSiteId);	
			}else{
				targetWebSite = getWebSite();
				targetWebSiteId = targetWebSite.getWebSiteId();
			}
				
		}
		return targetWebSite;
	}
		
	public Menu getTargetWebSiteMenu() throws WebSiteNotFoundException, MenuNotFoundException {		
		WebSite webSiteToUse = getTargetWebSite();
		return WebSiteUtils.getWebSiteMenu(webSiteToUse);
	}
	
	public MenuComponent getTargetWebSiteMenuComponent() throws MenuNotFoundException, WebSiteNotFoundException {
		MenuComponent comp = getMenuRepository().getMenuComponent(getTargetWebSiteMenu(), menuName);		
		if( StringUtils.isNotEmpty( comp.getRoles()  )){
			for( String role : StringUtils.split( comp.getRoles(), ',' ) ){
				if(! request.isUserInRole(role) ){
					comp.removeChildren();					
				}					
			}
		}
		checkRequiredRoles( comp.getComponents() );
		return comp;
	}
	
	private void checkRequiredRoles (List<MenuComponent> items){		
		List<MenuComponent> list = new ArrayList<MenuComponent>();
		for(MenuComponent item : items){
			if( StringUtils.isNotEmpty( item.getRoles()) ){
				for( String role : StringUtils.split( item.getRoles(), ',' ) ){
					if(! request.isUserInRole(role) ){
						list.add(item);	
					}else{	
						checkRequiredRoles(item.getComponents());
					}
				}
			}
		}
		items.removeAll(list);
	}

	
	
	
    /*
    public Company getTargetCompany() {		
    	if (companyId == null){
    		this.companyId = getUser().getCompanyId();
    	}
		if(targetCompany == null){
			try {
				targetCompany = companyManager.getCompany( (companyId).longValue() );
			} catch (CompanyNotFoundException e) {
				log.warn((new StringBuilder()).append("Could not load company object for id: ").append(companyId).toString());
				return null;
			}
		}		
		return targetCompany ;
	}*/
    

    
    
/*	public Menu getTargetCompanyMenu() {
		Company company = getTargetCompany();    	
		
		this.getWebSiteMenu(name);
		String menuIdStr =StringUtils.defaultIfEmpty( company.getProperties().get("menuId"), "1" );	
		long menuId = Long.parseLong( menuIdStr );    	
		try {
			return getMenuRepository().getMenu(menuId);
		} catch (MenuNotFoundException e) {
			return null;
		}
    }*/
	
/*	
	public MenuComponent getTargeMenuComponent(){
		try {
			return getMenuRepository().getMenuComponent(getTargetMenu(), menuName);
		} catch (MenuNotFoundException e) {
			return null;
		}
	}
	
	public MenuComponent getTargetCompanyMenuComponent(){
		try {
			return getMenuRepository().getMenuComponent(getTargetCompanyMenu(), menuName);
		} catch (MenuNotFoundException e) {
			return null;
		}
	}
	
	public List<Property> getTargetGroupProperty() {
		Map<String, String> properties = getTargetMenu().getProperties();
		List<Property> list = new ArrayList<Property>();
		for (String key : properties.keySet()) {
			String value = properties.get(key);
			list.add(new Property(key, value));
		}
		return list;
	}
	

		
	public String updateGroupProperties() throws Exception {		
		Menu menu = getTargetMenu();
		Map<String, String> properties = menu.getProperties();
		List<Map> list = ParamUtils.getJsonParameter(request, "items", List.class);		
		for (Map row : list) {
			String n = (String) row.get("name");
			String v = (String) row.get("value");
			properties.put(n, v);
		}		
		updateTargetMenuProperties(menu, menu.getProperties());
		return success();	
	}
		*/
	
	public String deleteMenuProperties() throws Exception {
		Menu menuToUse = getTargetMenu();
		Map<String, String> properties = menuToUse.getProperties();
		List<Map> list = ParamUtils.getJsonParameter(request, "items", List.class);
		for (Map row : list) {
			String n = (String) row.get("name");
			String v = (String) row.get("value");
			properties.remove(n);
		}
		updateTargetMenuProperties( menuToUse, properties );		
		return success();
	}

	protected void updateTargetMenuProperties(Menu menu, Map<String, String> properties) {
		if (properties.size() > 0) {
			menu.setProperties(properties);		
			getMenuRepository().updateMenu(menu);
		}
	}
	
	/*	
	public String createMenu() throws Exception {		
		
		Menu form = ParamUtils.getJsonParameter(request, "item", DefaultMenu.class);
	
		
		String name = (String)map.get("name");
		String title = (String)map.get("title");
		String description = (String)map.get("description");	
		Boolean enabled = (Boolean)map.get("enabled");
		String location = (String)map.get("location");
		String menuData = (String)map.get("menuData");
		Company company = getTargetCompany();		
		if( !name.startsWith( company.getName() )){
			StringBuilder sb = new StringBuilder();
			sb.append(company.getName());
			sb.append('_');
			sb.append(name);
			name = sb.toString();
		}
		getMenuRepository().createMenu(name, title, location, menuData);		
		this.menuId = getMenuRepository().getMenu(menuName).getMenuId();		
		
		
		return success();	
	}
	*/
	
	
	public String updateMenu() throws Exception {	
		try {
			
			Menu form = ParamUtils.getJsonParameter(request, "item", DefaultMenu.class);
			if( form.getMenuId() > 0 ){
				this.menuId = form.getMenuId();				
				Menu menuToUse = getTargetMenu();	
				menuToUse.setName(form.getName());
				menuToUse.setTitle(form.getTitle());
				menuToUse.setLoaction(form.getLocation());
				menuToUse.setEnabled(form.isEnabled());
				menuToUse.setMenuData(form.getMenuData());				
				getMenuRepository().updateMenu(menuToUse);				
			}else{				
				getMenuRepository().createMenu(form.getName(), form.getTitle(), form.getLocation(), form.getMenuData());
				this.menuId = getMenuRepository().getMenu(form.getName()).getMenuId();
			};
			
			
			
			
/*			Map map = ParamUtils.getJsonParameter(request, "item", Map.class);						
			String name = (String)map.get("name");
			String title = (String)map.get("title");
			Boolean enabled = (Boolean)map.get("enabled");
			String location = (String)map.get("location");
			String menuData = (String)map.get("menuData");			
			if( menuId == null || menuId <= 0){
				Integer  selectedMenuId = (Integer)map.get("menuId");	
				menuId = selectedMenuId.longValue();
			}			
			Menu menu = getTargetMenu();					
			if(!StringUtils.isEmpty(name))
				menu.setName(name);
			if(!StringUtils.isEmpty(title))
				menu.setTitle(title);	
			if(!StringUtils.isEmpty(location))
				menu.setLoaction(location);	
			if(enabled!=null)
				menu.setEnabled(enabled);
			if(!StringUtils.isEmpty(menuData))
				menu.setMenuData(menuData);	
			
			getMenuRepository().updateMenu(menu);
			*/
			return success();
		} catch (Throwable e) {
			throw new Exception(e);
		}		
	}
	
    public String execute() throws Exception {  
        return success();
    }  
}