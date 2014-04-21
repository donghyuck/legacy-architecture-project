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

import architecture.common.model.BaseModelObject;
import architecture.common.user.Company;
import architecture.common.user.User;
import architecture.ee.web.navigator.Menu;

public interface WebSite extends BaseModelObject {
	
	public String getDisplayName() ;

	public void setDisplayName(String displayName) ;

	public boolean isAllowAnonymousAccess();
	
	public void setAllowAnonymousAccess(boolean allowAnonymousAccess);
	
	public long getWebSiteId() ;

	public User getUser();
	
	public Company getCompany();
		
	public boolean isEnabled();
	
	public void setEnabled(boolean enabled);
	
	public String getUrl();
	
	public void setUrl(String url);
	
	public void setMenu(Menu menu);
	
	public Menu getMenu();
		
}
