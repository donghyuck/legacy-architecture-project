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
package architecture.ee.web.community.struts2.action.admin.ajax;

import architecture.common.user.Company;
import architecture.common.user.CompanyManager;
import architecture.common.user.CompanyNotFoundException;
import architecture.ee.web.site.WebSite;
import architecture.ee.web.site.WebSiteManager;
import architecture.ee.web.site.WebSiteNotFoundException;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;

public class WebsitePagesManagementAction extends FrameworkActionSupport {

	private Long targetCompanyId;
	
	private Long targetSiteId;
	
	private WebSite targetWebSite ;
	
	private WebSiteManager webSiteManager;
	
	private CompanyManager companyManager;
	
	
	public WebsitePagesManagementAction() {
		targetSiteId = -1L;
		targetCompanyId = -1L;
	}


	/**
	 * @return targetCompanyId
	 */
	public Long getTargetCompanyId() {
		return targetCompanyId;
	}

	/**
	 * @param targetCompanyId 설정할 targetCompanyId
	 */
	public void setTargetCompanyId(Long targetCompanyId) {
		this.targetCompanyId = targetCompanyId;
	}	
	
	public Company getTargetCompany() throws CompanyNotFoundException{
		if( targetCompanyId < 1 ){
			return getUser().getCompany();
		}
		return companyManager.getCompany(targetCompanyId);
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
	 * @return targetSiteId
	 */
	public Long getTargetSiteId() {
		return targetSiteId;
	}

	/**
	 * @param targetSiteId 설정할 targetSiteId
	 */
	public void setTargetSiteId(Long targetSiteId) {
		this.targetSiteId = targetSiteId;
	}

	public WebSite getTargetWebSite() throws WebSiteNotFoundException{		

		if( targetWebSite == null )	{
			if( targetSiteId < 1 )
				targetWebSite = getWebSite();			
			else
				this.targetWebSite = webSiteManager.getWebSiteById(targetSiteId);
		}
		return targetWebSite;
	}
	
	/**
	 * @return webSiteManager
	 */
	public WebSiteManager getWebSiteManager() {
		return webSiteManager;
	}

	/**
	 * @param webSiteManager 설정할 webSiteManager
	 */
	public void setWebSiteManager(WebSiteManager webSiteManager) {
		this.webSiteManager = webSiteManager;
	}


	public String execute() throws Exception {
		return success();
	}

}
