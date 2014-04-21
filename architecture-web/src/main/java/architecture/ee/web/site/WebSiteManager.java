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
package architecture.ee.web.site;

import java.util.List;

import architecture.common.user.Company;
import architecture.common.user.User;

public interface WebSiteManager {
	
	public void createWebSite(String name, String description, String displayName, String url, boolean allowAnonymousAccess, Company company, User user) throws WebSiteAlreadyExistsExcaption;
	
	public void createWebSite(WebSite webSite) throws WebSiteAlreadyExistsExcaption;
	
	public void updateWebSite(WebSite webSite) throws WebSiteAlreadyExistsExcaption, WebSiteNotFoundException;
	
	public List<WebSite> getWebSites(Company company);
	
	public int getWebCount(Company company);
	
	public List<WebSite> findWebSitesByName(String name);
	
	public List<WebSite> findWebSitesByUrl(String url);
	
	public WebSite getWebSiteByName(String name) throws WebSiteNotFoundException;
	
	public WebSite getWebSiteByUrl(String url) throws WebSiteNotFoundException;
	
	public WebSite getWebSiteById(long webSiteId) throws WebSiteNotFoundException;
	
	public void refreshWebSite(WebSite webSite);
	
}
