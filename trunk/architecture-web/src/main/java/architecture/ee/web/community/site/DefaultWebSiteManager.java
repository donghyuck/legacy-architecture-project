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

import java.util.ArrayList;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.lang.StringUtils;

import architecture.common.user.Company;
import architecture.common.user.CompanyManager;
import architecture.common.user.CompanyNotFoundException;
import architecture.common.user.User;
import architecture.common.user.UserManager;
import architecture.common.user.UserNotFoundException;
import architecture.ee.web.navigator.MenuNotFoundException;
import architecture.ee.web.site.WebSite;
import architecture.ee.web.site.WebSiteAlreadyExistsExcaption;
import architecture.ee.web.site.WebSiteManager;
import architecture.ee.web.site.WebSiteNotFoundException;
import architecture.ee.web.site.dao.WebSiteDao;
import architecture.ee.web.util.WebSiteUtils;

public class DefaultWebSiteManager implements WebSiteManager {
	
	/**
	 * @return webSiteUrlCache
	 */
	public Cache getWebSiteUrlCache() {
		return webSiteUrlCache;
	}

	/**
	 * @param webSiteUrlCache 설정할 webSiteUrlCache
	 */
	public void setWebSiteUrlCache(Cache webSiteUrlCache) {
		this.webSiteUrlCache = webSiteUrlCache;
	}

	private UserManager userManager;
	
	private CompanyManager companyManager;
	
	private WebSiteDao webSiteDao ;
	
	private Cache webSiteCache ;
	
	private Cache webSiteIdCache;
	
	private Cache webSiteUrlCache;
	
	public DefaultWebSiteManager() {
		
	}

	/**
	 * @return userManager
	 */
	public UserManager getUserManager() {
		return userManager;
	}

	/**
	 * @param userManager 설정할 userManager
	 */
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	/**
	 * @return companyManager
	 */
	public CompanyManager getCompanyManager() {
		return companyManager;
	}

	/**
	 * @param companyManager 설정할 companyManager
	 */
	public void setCompanyManager(CompanyManager companyManager) {
		this.companyManager = companyManager;
	}

	/**
	 * @return webSiteDao
	 */
	public WebSiteDao getWebSiteDao() {
		return webSiteDao;
	}

	/**
	 * @param webSiteDao 설정할 webSiteDao
	 */
	public void setWebSiteDao(WebSiteDao webSiteDao) {
		this.webSiteDao = webSiteDao;
	}

	/**
	 * @return webSiteCache
	 */
	public Cache getWebSiteCache() {
		return webSiteCache;
	}

	/**
	 * @param webSiteCache 설정할 webSiteCache
	 */
	public void setWebSiteCache(Cache webSiteCache) {
		this.webSiteCache = webSiteCache;
	}

	/**
	 * @return webSiteIdCache
	 */
	public Cache getWebSiteIdCache() {
		return webSiteIdCache;
	}

	/**
	 * @param webSiteIdCache 설정할 webSiteIdCache
	 */
	public void setWebSiteIdCache(Cache webSiteIdCache) {
		this.webSiteIdCache = webSiteIdCache;
	}

	/**
	 * @return webSiteUrlCache
	 */
	public Cache getWebSiteUrllCache() {
		return webSiteUrlCache;
	}

	/**
	 * @param webSiteUrlCache 설정할 webSiteUrlCache
	 */
	public void setWebSiteUrllCache(Cache webSiteUrllCache) {
		this.webSiteUrlCache = webSiteUrllCache;
	}
	
	
    private WebSite getWebSiteFromCacheById(long id)
    {    	
        if(webSiteCache.get(id) != null)
            return (WebSite)webSiteCache.get(id).getValue();
        else
            return null;
    }

	public void createWebSite(String name, String description, String displayName, String url, boolean allowAnonymousAccess, Company company, User user) throws WebSiteAlreadyExistsExcaption {		
		DefaultWebSite site = new DefaultWebSite( name, description, displayName, url, allowAnonymousAccess, true, company, user);		
		createWebSite(site);		
	}

	public void createWebSite(WebSite webSite) throws WebSiteAlreadyExistsExcaption {		
		
		if( StringUtils.isNotEmpty(webSite.getName()) &&  webSiteDao.findWebSitesByName(webSite.getName()).size() > 0 ){
			throw new WebSiteAlreadyExistsExcaption();
		}		
		
		if( StringUtils.isNotEmpty(webSite.getUrl()) && webSiteDao.findWebSitesByUrl(webSite.getUrl()).size() > 0 ){
			throw new WebSiteAlreadyExistsExcaption();
		}
		
		webSiteDao.createWebSite(webSite);
	}

	public void updateWebSite(WebSite webSite) throws WebSiteAlreadyExistsExcaption, WebSiteNotFoundException {		
		WebSite old = getWebSiteById(webSite.getWebSiteId());
		if( !StringUtils.equals( old.getUrl(), webSite.getUrl())){
			if( webSiteDao.findWebSitesByUrl(webSite.getUrl()).size() > 0){
				throw new WebSiteAlreadyExistsExcaption();
			}else{
				webSiteUrlCache.remove(old.getUrl());				
			}			
		}
		if( !StringUtils.equals( old.getName(), webSite.getName())){
			if( webSiteDao.findWebSitesByName(webSite.getName()).size() > 0){
				throw new WebSiteAlreadyExistsExcaption();
			}else{
				webSiteIdCache.remove(old.getName());
			}
		}		
		webSiteDao.updateWebSite(webSite);		
	}

	public List<WebSite> getWebSites(Company company) {
		List<Long> ids = webSiteDao.getWebSiteIds(company.getCompanyId());
		List<WebSite> sites = new ArrayList<WebSite>(ids.size());
		for(Long id : ids){
			try {
				sites.add( getWebSiteById(id));
			} catch (WebSiteNotFoundException e) {
			}
		}		
		return sites;
	}

	public int getWebCount(Company company) {
		return webSiteDao.getWebSiteCount(company.getCompanyId());
	}

	public List<WebSite> findWebSitesByName(String name) {
		List<Long> ids = webSiteDao.findWebSitesByName(name);
		List<WebSite> sites = new ArrayList<WebSite>(ids.size());
		for(Long id : ids){
			try {
				sites.add( getWebSiteById(id));
			} catch (WebSiteNotFoundException e) {
			}
		}		
		return sites;
	}

	public List<WebSite> findWebSitesByUrl(String url) {
		List<Long> ids = webSiteDao.findWebSitesByUrl(url);
		List<WebSite> sites = new ArrayList<WebSite>(ids.size());
		for(Long id : ids){
			try {
				sites.add( getWebSiteById(id));
			} catch (WebSiteNotFoundException e) {
			}
		}		
		return sites;
	}

	public WebSite getWebSiteByName(String name) throws WebSiteNotFoundException {
		Long webSiteId = -1L;
		if(webSiteIdCache.get(name) != null ) {
			webSiteId = (Long)webSiteIdCache.get(name).getValue();
		}			
		if( webSiteId < 0 ){
			webSiteId = webSiteDao.getWebSiteByName(name).getWebSiteId();
			webSiteIdCache.put(new Element(name, webSiteId ));			
		}		
		return getWebSiteById(webSiteId);
	}

	public WebSite getWebSiteByUrl(String url) throws WebSiteNotFoundException {
		Long webSiteId = -1L;
		if(webSiteUrlCache.get(url) != null ) {
			webSiteId = (Long)webSiteUrlCache.get(url).getValue();
		}			
		if( webSiteId < 0 ){
			webSiteId = webSiteDao.getWebSiteByUrl(url).getWebSiteId();
			webSiteUrlCache.put(new Element(url, webSiteId ));
		}		
		return getWebSiteById(webSiteId);
	}

	public WebSite getWebSiteById(long webSiteId) throws WebSiteNotFoundException {
		WebSite webSite ;
		if(webSiteCache.get(webSiteId) != null ) {
			webSite = 	(WebSite)webSiteCache.get(webSiteId).getValue();
		}else{
			webSite = webSiteDao.getWebSiteById(webSiteId);		
			try {
				((DefaultWebSite)webSite).setUser( userManager.getUser(webSite.getUser().getUserId()));
				((DefaultWebSite)webSite).setCompany( companyManager.getCompany(webSite.getCompany().getCompanyId()));
				if( webSite.getMenu().getMenuId() > 0 )
					((DefaultWebSite)webSite).setMenu(WebSiteUtils.getMenu(webSite.getMenu().getMenuId()));
				else
					((DefaultWebSite)webSite).setMenu(WebSiteUtils.getDefaultMenu());				
			} catch (MenuNotFoundException e){	
			} catch (UserNotFoundException e) {
			} catch (CompanyNotFoundException e) {
			}
			webSiteCache.put(new Element( webSiteId, webSite ));			
		}		
		return webSite;
	}

	public void refreshWebSite(WebSite webSite) {
		if( webSite.getWebSiteId() > 0 ){
			webSiteCache.remove(webSite.getWebSiteId());
		}		
	}

}
