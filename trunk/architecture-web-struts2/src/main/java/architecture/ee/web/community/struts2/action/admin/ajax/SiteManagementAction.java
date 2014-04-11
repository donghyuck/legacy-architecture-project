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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import architecture.common.user.Company;
import architecture.common.user.CompanyManager;
import architecture.common.user.CompanyNotFoundException;
import architecture.ee.web.attachment.Image;
import architecture.ee.web.site.WebSite;
import architecture.ee.web.site.WebSiteAlreadyExistsExcaption;
import architecture.ee.web.site.WebSiteManager;
import architecture.ee.web.site.WebSiteNotFoundException;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;
import architecture.ee.web.util.ParamUtils;
import architecture.ee.web.ws.Property;

public class SiteManagementAction extends FrameworkActionSupport {

	private Long targetCompanyId;
	private Long targetSiteId;
	
	private WebSite targetWebSite ;
	
	private WebSiteManager webSiteManager;
	private CompanyManager companyManager;
	
	
	public SiteManagementAction() {
		targetCompanyId = -1L;
		targetSiteId = -1L;
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
				targetWebSite = webSiteManager.getWebSiteById(targetSiteId);
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


	/**
	 * @return companyManager
	 */
	public CompanyManager getCompanyManager() {
		return companyManager;
	}

	public List<WebSite> getTargetWebSites(){
		try {
			return webSiteManager.getWebSites(getTargetCompany());
		} catch (CompanyNotFoundException e) {
			return Collections.EMPTY_LIST;
		}		
	}

	public int getTargetWebSiteCount(){
		try {
			return webSiteManager.getWebCount(getTargetCompany());
		} catch (CompanyNotFoundException e) {
			return 0;
		}		
	}
	
	/**
	 * @param companyManager 설정할 companyManager
	 */
	public void setCompanyManager(CompanyManager companyManager) {
		this.companyManager = companyManager;
	}

	public List<Property> getTargetWebSiteProperty() throws WebSiteNotFoundException {
		Map<String, String> properties = getTargetWebSite().getProperties();
		List<Property> list = new ArrayList<Property>();
		for (String key : properties.keySet()) {
			String value = properties.get(key);
			list.add(new Property(key, value));
		}
		return list;
	}
	
	public String updateSiteProperties() throws Exception {		
		WebSite site = getTargetWebSite();
		Map<String, String> properties = site.getProperties();
		List<Map> list = ParamUtils.getJsonParameter(request, "items", List.class);		
		for (Map row : list) {
			String n = (String) row.get("name");
			String v = (String) row.get("value");
			properties.put(n, v);
		}		
		updateTargetImageProperties(site, site.getProperties());
		return success();	
	}
	
	public String deleteSiteProperties() throws Exception {
		WebSite site = getTargetWebSite();
		Map<String, String> properties = site.getProperties();
		List<Map> list = ParamUtils.getJsonParameter(request, "items", List.class);
		for (Map row : list) {
			String n = (String) row.get("name");
			String v = (String) row.get("value");
			properties.remove(n);
		}
		updateTargetImageProperties( site, properties );		
		return success();
	}

	protected void updateTargetImageProperties(WebSite image, Map<String, String> properties){
		if (properties.size() > 0) {
			image.setProperties(properties);
			this.targetWebSite = image;			
			try {
				webSiteManager.updateWebSite(targetWebSite);
			} catch (Exception e) {
			}
		}
	}

	public String execute() throws Exception {
		return success();
	}
	
	
}
