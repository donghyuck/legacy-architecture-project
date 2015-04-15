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
package architecture.ee.web.site.dao;

import java.util.List;

import architecture.ee.web.site.WebPageNotFoundException;
import architecture.ee.web.site.WebSite;
import architecture.ee.web.site.WebSiteDomainMapper;
import architecture.ee.web.site.WebSiteNotFoundException;
import architecture.ee.web.site.page.WebPage;

public interface WebSiteDao {
	
	public void createWebSite(WebSite webSite);
	
	public void updateWebSite(WebSite webSite);
		
	public WebSite getWebSiteById(long webSiteId) throws WebSiteNotFoundException;
	
	public WebSite getWebSiteByName(String name) throws WebSiteNotFoundException;
	
	public WebSite getWebSiteByUrl(String url) throws WebSiteNotFoundException;
		
	public List<Long> findWebSitesByUrl(String url);
	
	public List<Long> findWebSitesByName(String name);
	
	public int getWebSiteCount(long companyId );
	
	public List<Long> getWebSiteIds(long companyId);	
	
	public List<WebSiteDomainMapper> getWebSiteDomainMappers();
	
	public WebPage getWebPageByName(long webSiteId, String name) throws WebPageNotFoundException;
	
	public WebPage getWebPageById(long webPageId) throws WebPageNotFoundException;
	
	public int getWebPageCount(long websiteId);
	
	public List<Long> getWebPageIds(long websiteId);
	
	public List<Long> getWebPageIds(long websiteId, int startIndex, int maxResults );
	

	public void createWebPage(WebPage page);
	
	public void updateWebPage(WebPage page);
	
	public void deleteWebPage(WebPage page);
	
}
