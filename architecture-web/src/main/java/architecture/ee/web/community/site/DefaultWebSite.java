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
package architecture.ee.web.community.site;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import architecture.common.model.factory.ModelTypeFactory;
import architecture.common.model.support.BaseModelObjectSupport;
import architecture.common.user.Company;
import architecture.common.user.User;
import architecture.ee.web.navigator.DefaultMenu;
import architecture.ee.web.navigator.Menu;
import architecture.ee.web.site.WebSite;

public class DefaultWebSite extends BaseModelObjectSupport  implements WebSite {

	private String displayName ;
	
	private Company company;
	
	private long webSiteId ;
	
	private boolean enabled ;
	
	private boolean allowAnonymousAccess ;
	
	private User user;
	
	private String url;
		
	private Menu menu;
	
	/**
	 * 
	 * @param name
	 * @param description 
	 * @param displayName
	 * @param enabled
	 * @param allowAnonymousAccess
	 * @param user
	 * @param url
	 */
	public DefaultWebSite(String name, String description, String displayName, String url, boolean allowAnonymousAccess, boolean enabled, Company company, User user) {
		this.webSiteId = -1L;		
		this.displayName = displayName;
		this.enabled = enabled;
		this.allowAnonymousAccess = allowAnonymousAccess;
		this.user = user;
		this.menu = new DefaultMenu();
		this.company = company;
		this.url = url;
		this.setName(name);
		this.setDescription(description);		
		Date now = Calendar.getInstance().getTime();
		this.setCreationDate(now);
		this.setModifiedDate(now);
	}

	public DefaultWebSite(String name, String description, String displayName,  String url, boolean allowAnonymousAccess, boolean enabled,  User user) {
		this.webSiteId = -1L;		
		this.displayName = displayName;
		this.enabled = enabled;
		this.allowAnonymousAccess = allowAnonymousAccess;
		this.user = user;
		this.menu = new DefaultMenu();
		this.company = user.getCompany();
		this.url = url;
		this.setName(name);
		this.setDescription(description);		
		Date now = Calendar.getInstance().getTime();
		this.setCreationDate(now);
		this.setModifiedDate(now);
	}
	
	public DefaultWebSite( long webSiteId ) {
		this.webSiteId = webSiteId ;	
	}
	
	/**
	 * @return company
	 */
	public Company getCompany() {
		return company;
	}

	/**
	 * @param company 설정할 company
	 */
	public void setCompany(Company company) {
		this.company = company;
	}

	/**
	 * @return url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url 설정할 url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return allowAnonymousAccess
	 */
	public boolean isAllowAnonymousAccess() {
		return allowAnonymousAccess;
	}

	/**
	 * @param allowAnonymousAccess 설정할 allowAnonymousAccess
	 */
	public void setAllowAnonymousAccess(boolean allowAnonymousAccess) {
		this.allowAnonymousAccess = allowAnonymousAccess;
	}

	/**
	 * @return user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user 설정할 user
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled 설정할 enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Serializable getPrimaryKeyObject() {
		return webSiteId;
	}

	/**
	 * @return displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName 설정할 displayName
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return webSiteId
	 */
	public long getWebSiteId() {
		return webSiteId;
	}

	/**
	 * @param webSiteId 설정할 webSiteId
	 */
	public void setWebSiteId(long webSiteId) {
		this.webSiteId = webSiteId;
	}

	
	
	/**
	 * @return menu
	 */
	public Menu getMenu() {
		return menu;
	}

	/**
	 * @param menu 설정할 menu
	 */
	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public int getModelObjectType() {
		return ModelTypeFactory.getTypeIdFromCode("WEBSITE");
	}

	public int getCachedSize() {
		return 0;
	}

}