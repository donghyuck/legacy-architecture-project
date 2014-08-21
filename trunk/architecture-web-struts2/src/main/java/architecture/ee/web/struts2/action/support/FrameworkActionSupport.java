package architecture.ee.web.struts2.action.support;

import architecture.ee.web.navigator.Menu;
import architecture.ee.web.navigator.MenuComponent;
import architecture.ee.web.navigator.MenuNotFoundException;
import architecture.ee.web.navigator.MenuRepository;
import architecture.ee.web.site.WebSite;
import architecture.ee.web.site.support.WebSiteAware;
import architecture.ee.web.util.WebSiteUtils;

public class FrameworkActionSupport extends DefaultActionSupport implements WebSiteAware {
   	
	private WebSite webSite;
	
	private Menu webSiteMenu;
	
	private MenuRepository menuRepository ;
	
	
	public void setMenuRepository(MenuRepository menuRepository) {
		this.menuRepository = menuRepository;
	}
	
    protected MenuRepository getMenuRepository() {
		return menuRepository;
	}
    
	/**
	 * @return webSite
	 */
	public WebSite getWebSite() {
		return webSite;
	}

	/**
	 * @param webSite 설정할 webSite
	 */
	public void setWebSite(WebSite webSite) {
		this.webSite = webSite;
		try {
			this.webSiteMenu = WebSiteUtils.getWebSiteMenu(webSite);
		} catch (MenuNotFoundException e) {
			log.error(e);
		}
	}

	public MenuComponent getWebSiteMenu(String name) throws MenuNotFoundException { 
		if( webSiteMenu != null ){
			return menuRepository.getMenuComponent(webSiteMenu, name);
		}else{
			throw new MenuNotFoundException();
		}
	}

	public MenuComponent getWebSiteMenu(String name, String child) throws MenuNotFoundException { 
		if( webSiteMenu != null ){
			MenuComponent parentMenu = getWebSiteMenu(name);
			MenuComponent selectedMenu = null;
			for( MenuComponent childMenu : parentMenu.getComponents() )
			{
				if( child.equals( childMenu.getName() ) ){
					selectedMenu = childMenu;		
					break;
				}
				if( childMenu.getComponents().size() > 0 ){
					for( MenuComponent childMenu2 : childMenu.getComponents() ){
						if( child.equals( childMenu2.getName() ) ){
							selectedMenu = childMenu2;		
							break;
						}
					}
				}
			}
			return selectedMenu;		
		}else{
			throw new MenuNotFoundException();
		}
	}
	}