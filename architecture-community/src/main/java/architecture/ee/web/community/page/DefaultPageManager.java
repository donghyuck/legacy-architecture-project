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
package architecture.ee.web.community.page;

import java.util.ArrayList;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import architecture.common.user.User;
import architecture.common.user.UserManager;
import architecture.common.user.UserNotFoundException;
import architecture.ee.web.community.page.dao.PageDao;
import architecture.ee.web.community.page.dao.PageVersionDao;

public class DefaultPageManager implements PageManager  {

	private UserManager userManager;
	
	//private PageVersionManager pageVersionManager;
		
	private PageDao pageDao ;
	
	private PageVersionDao pageVersionDao ;
	
	private Cache pageCache;
	
	private Cache pageIdCache;
	
	private Cache versionCache;
	
	private Cache pageVersionsCache;
	
	public DefaultPageManager() {
		
	}
	
	
	/**
	 * @return pageVersionDao
	 */
	public PageVersionDao getPageVersionDao() {
		return pageVersionDao;
	}


	/**
	 * @param pageVersionDao 설정할 pageVersionDao
	 */
	public void setPageVersionDao(PageVersionDao pageVersionDao) {
		this.pageVersionDao = pageVersionDao;
	}


	/**
	 * @return pageVersionsCache
	 */
	public Cache getPageVersionsCache() {
		return pageVersionsCache;
	}


	/**
	 * @param pageVersionsCache 설정할 pageVersionsCache
	 */
	public void setPageVersionsCache(Cache pageVersionsCache) {
		this.pageVersionsCache = pageVersionsCache;
	}


	public Page createPage(User user, BodyType bodyType, String name, String title, String body) {		
		if(bodyType == null)
			throw new IllegalArgumentException("A page content type is required to create a page.");		
		DefaultPage page = new DefaultPage();
		
		page.setName(name);
		page.setBodyContent(new DefaultBodyContent(-1L, -1L, bodyType, body));
		page.setTitle(title);
		page.setUser(user);
		return page ;	
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW )
	public void updatePage(Page page) {
		updatePage(page, false);		
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW )
	public void updatePage(Page page, boolean forceNewVersion) {
		boolean isNewPage = page.getPageId() == -1L;
		boolean isNewVersionRequired = isNewVersionRequired( forceNewVersion, isNewPage );
		if(isNewPage){
			pageDao.create(page);			
		}else{
			if( isNewVersionRequired ){
				pageDao.update(page, isNewVersionRequired);
			}
		}		
	}
	
	private boolean isNewVersionRequired( boolean forceNewVersion, boolean isNewPage ){
		boolean isNewVersionRequired = false ;
		if(isNewPage)
			isNewVersionRequired = false;
		else if (forceNewVersion)
			isNewVersionRequired = true;		
		return isNewVersionRequired;		
	}

	public Page getPage(long pageId) {
		Page page;
		if( pageCache.get(pageId) != null ){
			page = (Page)pageCache.get(pageId).getObjectValue();
		}else{
			
			page = pageDao.getPageById(pageId);
			long userId = page.getUser().getUserId();
			try {
				page.setUser(userManager.getUser(userId));
			} catch (UserNotFoundException e) {
			}
			pageCache.put(new Element( pageId, page));
		}		
		return page;
	}

	public Page getPage(long pageId, int versionId) {
		
		
		return null;
	}

	public Page getPage(String name) {
		// TODO 자동 생성된 메소드 스텁
		return null;
	}

	public Page getPage(String name, int versionId) {
		// TODO 자동 생성된 메소드 스텁
		return null;
	}

	public List<Page> getPages(int objectType) {
		// TODO 자동 생성된 메소드 스텁
		return null;
	}

	public List<Page> getPages(int objectType, long objectId) {
		List<Long> ids = pageDao.getPageIds(objectType, objectId);
		ArrayList<Page> list = new ArrayList<Page>(ids.size());
		for( Long pageId : ids ){
			list.add(getPage(pageId));		
		}
		return list;
	}

	public int getPageCount(int objectType) {
		return 0;
	}

	public int getPageCount(int objectType, long objectId) {
		return pageDao.getPageCount(objectType, objectId);
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
	 * @return pageDao
	 */
	public PageDao getPageDao() {
		return pageDao;
	}

	/**
	 * @param pageDao 설정할 pageDao
	 */
	public void setPageDao(PageDao pageDao) {
		this.pageDao = pageDao;
	}

	/**
	 * @return pageCache
	 */
	public Cache getPageCache() {
		return pageCache;
	}

	/**
	 * @param pageCache 설정할 pageCache
	 */
	public void setPageCache(Cache pageCache) {
		this.pageCache = pageCache;
	}

	/**
	 * @return pageIdCache
	 */
	public Cache getPageIdCache() {
		return pageIdCache;
	}

	/**
	 * @param pageIdCache 설정할 pageIdCache
	 */
	public void setPageIdCache(Cache pageIdCache) {
		this.pageIdCache = pageIdCache;
	}

	/**
	 * @return versionCache
	 */
	public Cache getVersionCache() {
		return versionCache;
	}

	/**
	 * @param versionCache 설정할 versionCache
	 */
	public void setVersionCache(Cache versionCache) {
		this.versionCache = versionCache;
	}


	public List<PageVersion> getPageVersions(long pageId) {
		String key = getVersionListCacheKey(pageId);
		List<Integer> versions ;
		if( pageVersionsCache.get(key) != null ){
			versions = (List<Integer>)pageVersionsCache.get(key).getValue();	
		}else{			
			versions = this.pageVersionDao.getPageVersionIds(pageId);
			pageVersionsCache.put(new Element(key, versions));
		}
		List<PageVersion> list = new ArrayList<PageVersion>(versions.size());
		for( Integer version : versions){
			list.add(getPageVersion(pageId, version));
		}
		return list;
	}
	
	private String getVersionCacheKey(long pageId, int versionId){
		return (new StringBuilder()).append("version-").append(pageId).append("-").append(versionId).toString();
	}
	
	private String getVersionListCacheKey(long pageId){
		return (new StringBuilder()).append("versions-").append(pageId).toString();
	}

	protected PageVersion getPageVersion(long pageId, int versionNumber){
		String key = getVersionCacheKey(pageId, versionNumber);
		PageVersion pv ;
		if( versionCache.get(key) != null ){
			pv = (PageVersion)versionCache.get(key).getObjectValue();
		}else{
			pv = pageVersionDao.getPageVersion(pageId, versionNumber);
			versionCache.put(new Element(key, pv));
		}
		return pv;
	}
	
}
