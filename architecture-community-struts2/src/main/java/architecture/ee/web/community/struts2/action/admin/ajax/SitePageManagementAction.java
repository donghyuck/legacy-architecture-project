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

import org.apache.commons.lang.StringUtils;

import architecture.common.user.Company;
import architecture.common.user.CompanyManager;
import architecture.common.user.CompanyNotFoundException;
import architecture.ee.web.community.page.DefaultBodyContent;
import architecture.ee.web.community.page.DefaultPage;
import architecture.ee.web.community.page.Page;
import architecture.ee.web.community.page.PageManager;
import architecture.ee.web.community.page.PageNotFoundException;
import architecture.ee.web.community.page.PageState;
import architecture.ee.web.site.WebSite;
import architecture.ee.web.site.WebSiteManager;
import architecture.ee.web.site.WebSiteNotFoundException;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;
import architecture.ee.web.util.ParamUtils;
import architecture.ee.web.ws.Property;

public class SitePageManagementAction extends FrameworkActionSupport {

	private Long targetCompanyId;
	
	private Long targetSiteId;
	
	private Long targetPageId;
	
	private WebSite targetWebSite ;
		
	private Page targetPage;
	
	private PageManager pageManager ;
	
	private WebSiteManager webSiteManager;
	
	private CompanyManager companyManager;
		
	public SitePageManagementAction() {
		this.targetCompanyId = -1L;
		this.targetSiteId = -1L;
		this.targetPageId = -1L;
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
	
	/**
	 * @return targetPageId
	 */
	public Long getTargetPageId() {
		return targetPageId;
	}

	/**
	 * @param targetPageId 설정할 targetPageId
	 */
	public void setTargetPageId(Long targetPageId) {
		this.targetPageId = targetPageId;
	}

	/**
	 * @return targetPage
	 */
	public Page getTargetPage() throws PageNotFoundException {
		if( targetPageId < 1 ){
			WebSite webSiteToUse;
			try {
				webSiteToUse = getTargetWebSite();
				return new DefaultPage(webSiteToUse.getModelObjectType(), webSiteToUse.getWebSiteId());
			} catch (WebSiteNotFoundException e) {}		
			return new DefaultPage();
		}
		if( targetPage == null){
			targetPage = pageManager.getPage(targetPageId);
		}
		return targetPage;
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

	/**
	 * @return companyManager
	 */
	public CompanyManager getCompanyManager() {
		return companyManager;
	}

	/**
	 * @param pageManager 설정할 pageManager
	 */
	public void setPageManager(PageManager pageManager) {
		this.pageManager = pageManager;
	}

	/**
	 * @param companyManager 설정할 companyManager
	 */
	public void setCompanyManager(CompanyManager companyManager) {
		this.companyManager = companyManager;
	}

	public String execute() throws Exception {
		return success();
	}
	
	public int getTargetPageCount(){
		WebSite webSiteToUse;
		try {
			webSiteToUse = getTargetWebSite();
			log.debug("objectId=" + webSiteToUse.getModelObjectType() + ", objectId=" + webSiteToUse.getWebSiteId() );
			return pageManager.getPageCount(webSiteToUse.getModelObjectType(), webSiteToUse.getWebSiteId());
		} catch (WebSiteNotFoundException e) {
			return 0;
		}				
	}
	
	public List<Page> getTargetPages(){
		WebSite webSiteToUse;
		try {
			webSiteToUse = getTargetWebSite();
			return pageManager.getPages(webSiteToUse.getModelObjectType(), webSiteToUse.getWebSiteId());
		} catch (WebSiteNotFoundException e) {
			return Collections.EMPTY_LIST;
		}		
	}

	public String updatePage() throws Exception {
		
		Map map = ParamUtils.getJsonParameter(request, "item", Map.class);		
		Integer pageId = (Integer)map.get("pageId");
		Integer objectType = (Integer)map.get("objectType");
		Integer objectId = (Integer)map.get("objectId");
		Integer versionId = (Integer)map.get("versionId");		
		String title = (String)map.get("title");
		String summary =(String)map.get("summary");
		String bodyText = (String)map.get("bodyText");
		String pageState = (String)map.get("pageState");
		boolean doPublish = false;
		this.targetPageId = pageId.longValue();
		this.targetSiteId = objectId.longValue();		
		
		Page pageToUse = getTargetPage();
		
		if( pageToUse.getPageId() < 1 )
		{
			pageToUse.setUser(getUser());
		}else{
			PageState newPageState = PageState.valueOf(pageState.toUpperCase());
			doPublish = ( newPageState != null && pageToUse.getPageState() != newPageState && newPageState == PageState.PUBLISHED );
			log.debug("state chaged: " + doPublish );
			if( doPublish  ){
				pageToUse.setPageState(newPageState);
			}
		}			
		if( !doPublish ){
			if( StringUtils.isNotEmpty(title)){
				pageToUse.setTitle(title);
			}
			if(StringUtils.isNotEmpty(summary)){
				pageToUse.setSummary(summary);
			}
			if(StringUtils.isNotEmpty(bodyText)){
				if( pageToUse.getBodyContent() == null ){
					pageToUse.setBodyContent(new DefaultBodyContent( pageToUse.getPageId(), bodyText ));
				}else{
					pageToUse.setBodyText(bodyText);
				}
			}
		}
		
		pageManager.updatePage(pageToUse);		
		this.targetPageId = pageToUse.getPageId();				
		return success();
	}
	
	public List<Property> getTargetPageProperty() throws PageNotFoundException {
		if( this.getTargetPageId() < 1 )
			return Collections.EMPTY_LIST;
		
		Map<String, String> properties = getTargetPage().getProperties();
		List<Property> list = new ArrayList<Property>();
		for (String key : properties.keySet()) {
			String value = properties.get(key);
			list.add(new Property(key, value));
		}
		return list;
	}
	
	public String updatePageProperties() throws Exception {		
		if( this.getTargetPageId() > 0 ){
			Page pageToUse = getTargetPage();
			Map<String, String> properties = pageToUse.getProperties();
			List<Map> list = ParamUtils.getJsonParameter(request, "items", List.class);		
			for (Map row : list) {
				String n = (String) row.get("name");
				String v = (String) row.get("value");
				properties.put(n, v);
			}		
			updateTargetPageProperties(pageToUse, pageToUse.getProperties());
		}
		return success();	
	}
	
	public String deletePageProperties() throws Exception {
		if( this.getTargetPageId() > 0 ){
			Page pageToUse = getTargetPage();
			Map<String, String> properties = pageToUse.getProperties();
			List<Map> list = ParamUtils.getJsonParameter(request, "items", List.class);
			for (Map row : list) {
				String n = (String) row.get("name");
				String v = (String) row.get("value");
				properties.remove(n);
			}
			updateTargetPageProperties( pageToUse, properties );		
		}
		return success();
	}
	
	protected void updateTargetPageProperties(Page pageToUse, Map<String, String> properties){
		if ( properties.size() > 0 ||  ( pageToUse.getProperties().size() > 0 &&  properties.size() == 0 ) ){
			pageToUse.setProperties(properties);
			this.targetPage = pageToUse;			
			try {
				pageManager.updatePage(targetPage);
			} catch (Exception e) {
			}
		}
	}
}
