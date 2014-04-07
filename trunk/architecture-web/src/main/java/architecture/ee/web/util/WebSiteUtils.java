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

import org.apache.commons.lang.StringUtils;

import architecture.common.util.TextUtils;
import architecture.ee.util.ApplicationHelper;
import architecture.ee.web.navigator.Menu;
import architecture.ee.web.navigator.MenuNotFoundException;
import architecture.ee.web.navigator.MenuRepository;
import architecture.ee.web.site.WebSite;
import architecture.ee.web.site.WebSiteManager;
import architecture.ee.web.site.WebSiteNotFoundException;

public class WebSiteUtils {

	public WebSiteUtils() {
	}


	public static WebSiteManager getWebSiteManager(){
		return ApplicationHelper.getComponent(WebSiteManager.class);
	}
	
	public static WebSite getWebSite(HttpServletRequest request) throws WebSiteNotFoundException {
		String localName = request.getLocalName();		
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

	
	public static Menu getWebSiteMenu(WebSite webSite) throws MenuNotFoundException{
		return getMenu(webSite.getLongProperty("menuId", getDefaultMenuId()));		
	}	

	public static boolean isallowedSignIn(WebSite website){
		return website.getBooleanProperty("allowedSignIn", true);
	}
	
	public static boolean isAllowedSignup(WebSite website){
		return website.getBooleanProperty("allowedSignup", true);
	}
	

}
