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
package architecture.ee.web.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.common.util.StringUtils;
import architecture.common.util.TextUtils;
import architecture.ee.util.ApplicationHelper;
import architecture.ee.web.logo.LogoManager;
import architecture.ee.web.navigator.Menu;
import architecture.ee.web.navigator.MenuComponent;
import architecture.ee.web.navigator.MenuNotFoundException;
import architecture.ee.web.navigator.MenuRepository;
import architecture.ee.web.site.WebSite;
import architecture.ee.web.site.WebSiteManager;
import architecture.ee.web.site.WebSiteNotFoundException;


public class WebSiteUtils {

	public static final String MAIN_PAGE_VIEW_PREFIX = "main.view";
	private static final Log log = LogFactory.getLog(WebSiteUtils.class);
	public WebSiteUtils() {
	}

	public static LogoManager getLogoManager(){
		return ApplicationHelper.getComponent(LogoManager.class);
	}

	public static WebSiteManager getWebSiteManager(){
		return ApplicationHelper.getComponent(WebSiteManager.class);
	}	
	
	public static WebSite getWebSite(HttpServletRequest request) throws WebSiteNotFoundException {
		String localName = request.getLocalName();		
		log.debug("check: " + localName + " - " + ( StringUtils.isNotEmpty(localName) && !TextUtils.isValidIpAddress(localName) && TextUtils.isValidHostname(localName)) );
		
		if( StringUtils.isNotEmpty(localName) && !TextUtils.isValidIpAddress(localName) && TextUtils.isValidHostname(localName)){	
			return getWebSiteManager().getWebSiteByUrl(localName);			
		}
		return getDefaultWebSite();
	}
	
	public static Long getDefaultWebSiteId(){
		return  ApplicationHelper.getApplicationLongProperty("components.website.default.websiteId", 1L);
	}
	
	public static WebSite getDefaultWebSite() throws WebSiteNotFoundException {
		return getWebSiteManager().getWebSiteById(getDefaultWebSiteId());
	}

	public static MenuRepository getMenuRepository(){
		return ApplicationHelper.getComponent(MenuRepository.class);
	}
	

	
	public static Long getDefaultMenuId(){
		return ApplicationHelper.getApplicationLongProperty("components.menu.default.menuId", 1L);
	}
	
	public static Menu getDefaultMenu() throws MenuNotFoundException {
		return getMenuRepository().getMenu(getDefaultMenuId());
	}
	
	public static Menu getMenu(long menuId) throws MenuNotFoundException {
		return getMenuRepository().getMenu(menuId);
	}

	public static MenuComponent getMenuComponent(String name) throws MenuNotFoundException { 
		return getMenuComponent(getDefaultMenu(), name);
	}
	
	public static MenuComponent getMenuComponent(String name, String child) throws MenuNotFoundException { 
		return getMenuComponent(getDefaultMenu(), name, child);
	}
	
	
	public static MenuComponent getMenuComponent(Menu menu, String name) throws MenuNotFoundException { 
		if( menu != null ){
			return getMenuRepository().getMenuComponent(menu, name);
		}else{
			throw new MenuNotFoundException();
		}
	}

	public static MenuComponent getMenuComponent(Menu menu, String name, String child) throws MenuNotFoundException { 
		if( menu != null ){
			MenuComponent parentMenu = getMenuComponent(menu, name);
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
	
	public static boolean isUserAccessAllowed(HttpServletRequest request, MenuComponent menu){
		if(StringUtils.isNotEmpty(menu.getRoles())){			
			for( String role : StringUtils.split(menu.getRoles(), ",")){
				if( request.isUserInRole( role ) )
					return true;
			}			
		}else{
			return true;
		}
		return false;
	}	
					
	public static Menu getWebSiteMenu(WebSite webSite) throws MenuNotFoundException{
		if( webSite.getMenu().getMenuId() < 1 )
			return getDefaultMenu();
		else 
			return webSite.getMenu();
	}	
	
	public static boolean hasLogo(WebSite website){
		return getLogoManager().getLogoImages(website).size() > 0 ? true : false;
	}
	
	public static boolean isAllowedSignIn(WebSite website){
		return website.getBooleanProperty("allowedSignIn", true);
	}

	
	public static boolean isAllowedSignup(WebSite website){
		return website.getBooleanProperty("allowedSignup", true);
	}

	public static boolean isAllowedSocialConnect(WebSite website){
		return website.getBooleanProperty("allowedSocialConnect", true);
	}

	public static void setIsAllowedSignIn(WebSite website, boolean isAllowedSignIn){
		website.getProperties().put("allowedSignIn", Boolean.toString(isAllowedSignIn));
	}
	
	public static void setIsAllowedSignup(WebSite website, boolean isAllowedSignup){
		website.getProperties().put("allowedSignup", Boolean.toString(isAllowedSignup));
	}

	public static void setIsAllowedSocialConnect(WebSite website, boolean isAllowedSocialConnect){
		website.getProperties().put("allowedSocialConnect", Boolean.toString(isAllowedSocialConnect));
	}

	public static void setMainTemplate(WebSite website, String templage){
		if(StringUtils.isNotBlank(templage))
			website.getProperties().put(MAIN_PAGE_VIEW_PREFIX, templage);
	}		

}