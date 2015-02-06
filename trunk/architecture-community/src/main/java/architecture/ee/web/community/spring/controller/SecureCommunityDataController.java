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
package architecture.ee.web.community.spring.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;

import architecture.common.user.Company;
import architecture.common.user.CompanyManager;
import architecture.common.user.CompanyNotFoundException;
import architecture.common.user.SecurityHelper;
import architecture.common.user.User;
import architecture.ee.exception.NotFoundException;
import architecture.ee.web.attachment.AttachmentManager;
import architecture.ee.web.attachment.ImageManager;
import architecture.ee.web.community.announce.AnnounceManager;
import architecture.ee.web.community.page.PageManager;
import architecture.ee.web.community.streams.PhotoStreamsManager;
import architecture.ee.web.logo.LogoManager;
import architecture.ee.web.navigator.DefaultMenu;
import architecture.ee.web.navigator.Menu;
import architecture.ee.web.navigator.MenuRepository;
import architecture.ee.web.site.WebSite;
import architecture.ee.web.site.WebSiteManager;
import architecture.ee.web.util.WebSiteUtils;
import architecture.user.spring.controller.SecureUserDataController.ItemList;

@Controller ("secure-community-data-controller")
@RequestMapping("/secure/data")
public class SecureCommunityDataController {

	private static final Log log = LogFactory.getLog(SecureCommunityDataController.class);

	
	@Inject
	@Qualifier("photoStreamsManager")
	private PhotoStreamsManager photoStreamsManager ;
	
	@Inject
	@Qualifier("imageManager")
	private ImageManager imageManager ;
	
	@Inject
	@Qualifier("attachmentManager")
	private AttachmentManager attachmentManager;

	@Inject
	@Qualifier("announceManager")
	private AnnounceManager announceManager ;
	
	@Inject
	@Qualifier("pageManager")
	private PageManager pageManager;

	@Inject
	@Qualifier("menuRepository")
	private MenuRepository menuRepository ;

	@Inject
	@Qualifier("webSiteManager")
	private WebSiteManager webSiteManager;
	
	@Inject
	@Qualifier("companyManager")
	private CompanyManager companyManager;
	
	@Inject
	@Qualifier("logoManager")
	private LogoManager logoManager ;
	

	
	public SecureCommunityDataController() {
	}


	@RequestMapping(value="/mgmt/menu/update.json",method={RequestMethod.POST} )
	@ResponseBody
	public Menu  updateMenu(
			@RequestBody DefaultMenu menu, 
			NativeWebRequest request) throws NotFoundException {			
		User user = SecurityHelper.getUser();		
		Menu menuToUse = menuRepository.getMenu(menu.getMenuId());
		menuToUse.setMenuData(menu.getMenuData());
		menuToUse.setName(menu.getName());
		menuToUse.setEnabled(menu.isEnabled());
		menuToUse.setTitle(menu.getTitle());
		menuToUse.setLoaction(menu.getLocation());		
		menuRepository.updateMenu(menuToUse);		
		return menuToUse;
	}

	@RequestMapping(value="/mgmt/website/list.json",method={RequestMethod.POST} )
	@ResponseBody
	public ItemList getWebSiteList(
			@RequestParam(value="company", defaultValue="0", required=false ) Long company,
			NativeWebRequest request) throws CompanyNotFoundException {			
		
		User user = SecurityHelper.getUser();			
		Company companyToUse ;
		if( company > 0 ){
			companyToUse = companyManager.getCompany(company);
		}else{
			companyToUse = user.getCompany();
		}		
		int totalCount = webSiteManager.getWebCount(companyToUse);
		return new ItemList(webSiteManager.getWebSites(companyToUse), totalCount);
	}		
	
	@RequestMapping(value="mgmt/website/get_and_refersh.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public WebSite  getWebSite(
			@RequestParam(value="siteId", defaultValue="0", required=false ) Long siteId,
			@RequestParam(value="refresh", defaultValue="false", required=false ) boolean refersh,
			NativeWebRequest request) throws NotFoundException {				
		WebSite webSite;
		
		if( siteId > 0 )
			webSite = webSiteManager.getWebSiteById(siteId);
		else 
			webSite = WebSiteUtils.getWebSite(request.getNativeRequest(HttpServletRequest.class));
		
		if(refersh){
			long webSiteId = webSite.getWebSiteId();
			webSiteManager.refreshWebSite(webSite);
			webSite = webSiteManager.getWebSiteById(webSiteId);
		}
		return webSite;
	}
}
