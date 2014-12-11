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

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import architecture.common.model.factory.ModelTypeFactory;
import architecture.common.user.SecurityHelper;
import architecture.common.user.User;
import architecture.ee.exception.NotFoundException;
import architecture.ee.web.community.page.BodyType;
import architecture.ee.web.community.page.Page;
import architecture.ee.web.community.page.PageMaker;
import architecture.ee.web.community.page.PageManager;
import architecture.ee.web.navigator.MenuComponent;
import architecture.ee.web.navigator.MenuNotFoundException;
import architecture.ee.web.site.WebSite;
import architecture.ee.web.site.WebSiteNotFoundException;
import architecture.ee.web.util.WebSiteUtils;

@Controller ("communityPageController")
@RequestMapping("/pages")
public class PageController {
	
	private static final Log log = LogFactory.getLog(PageController.class);
	
	private static final String DEFAULT_PAGE_TEMPLATE = "/html/community/page";		
	
	@Inject
	@Qualifier("pageManager")
	private PageManager pageManager ;

	@Inject
	@Qualifier("freemarkerConfig")
	private FreeMarkerConfig freeMarkerConfig ;
	
	@Autowired private ServletContext servletContext;
	
	public PageController() {
	}

	


	@RequestMapping(value="/{filename}", method=RequestMethod.GET)
	public String page(@PathVariable String filename, HttpServletRequest request, HttpServletResponse response, Model model) throws NotFoundException, IOException {		
		User user = SecurityHelper.getUser();		
		Page page = pageManager.getPage(filename);		
		
		page.getObjectId();
		page.getObjectType();
		
		WebSite website = getCurrentWebSite(request);
		PageMaker.Builder builder = PageMaker.newBuilder().configuration(freeMarkerConfig.getConfiguration()).servletContext(servletContext).page(page).model(model).request(request);			
		model.addAttribute("action", PageActionAdaptor.newBuilder().webSite(website).builder(builder).user(user).build());
		response.setContentType("text/html;charset=UTF-8");
		return DEFAULT_PAGE_TEMPLATE;
	}
	
	@RequestMapping(value = "/{filename:.+}", method=RequestMethod.GET, params={"source"})
	public String template(@PathVariable String filename, @RequestParam(value="source") String view, HttpServletRequest request, HttpServletResponse response, Model model) throws NotFoundException, IOException {		
		
		User user = SecurityHelper.getUser();		
		WebSite website = getCurrentWebSite(request);
		model.addAttribute("action", PageActionAdaptor.newBuilder().webSite(website).user(user).build());
		
		String restOfTheUrl = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		response.setContentType("text/html;charset=UTF-8");
		log.debug("name:" + filename);
		log.debug("path:" + view 	);
		
		return view;
	}
	
	
	private boolean hasPermissions(Page image, User user){		
		
		 if (image.getObjectType() == ModelTypeFactory.getTypeIdFromCode("COMPANY") && image.getObjectId() != user.getCompanyId() ){
			 return false;			
		}else if (image.getObjectType() == ModelTypeFactory.getTypeIdFromCode("USER") && image.getObjectId() != user.getUserId()){
			return false;			
		}else if (image.getObjectType() == ModelTypeFactory.getTypeIdFromCode("WEBSITE")){
			return true;
		}
		return true;
	}
	
	private WebSite getCurrentWebSite(HttpServletRequest request) throws WebSiteNotFoundException{
		return WebSiteUtils.getWebSite(request);
	}
	
	
	public static class PageActionAdaptor {
		
		public static final String NAVIGATOR_SELECTED_NAME_KEY = "navigator.selected.name";
		
		private User user;
		
		private WebSite webSite;
		
		private PageMaker.Builder builder ;
		
		public Page getPage(){
			return builder.getPage();
		}
		
		public String getPageTitle(){
			return getPage().getTitle();
		}
		
		public String getPageSummary(){
			return getPage().getSummary();
		}
		
		public String getPlainBodyText(){
			return getPage().getBodyText();
		}

		public String getBodyText(){
			Page pageToUse = getPage();
			if( pageToUse.getBodyContent().getBodyType() == BodyType.FREEMARKER ){
				if( StringUtils.isNotEmpty( pageToUse.getBodyText() ) )
					try {
						return builder.buildPageBody();
					} catch (Exception e) {
						log.error(e);
					}
			}
			return getPlainBodyText();
		}
		
		
		public MenuComponent getWebSiteMenu(String name) throws MenuNotFoundException { 
			if( webSite != null ){
				return WebSiteUtils.getMenuComponent( webSite.getMenu(), name);
			}else{
				throw new MenuNotFoundException();
			}
		}
		
		public boolean isSetPage(){
			return this.builder != null;
		}
		
		public boolean isSetNavigator (){
			Page pageToUse = getPage();
			if( pageToUse.getPageId() > 0 ){			
				String current = pageToUse.getProperty(NAVIGATOR_SELECTED_NAME_KEY, null);
				if( StringUtils.isNotEmpty(current)){
					return true;
				}
			}		
			return false;
		}
				
		public MenuComponent getNavigator() throws MenuNotFoundException{
			if(isSetNavigator()){
				Page pageToUse = getPage();
				String menuName = pageToUse.getProperty("page.menu.name", "USER_MENU");
				String current = pageToUse.getProperty(NAVIGATOR_SELECTED_NAME_KEY, null);			
				return WebSiteUtils.getMenuComponent(getWebSiteMenu(menuName), current );			
			}
			throw new MenuNotFoundException();
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
		 * @return webSite
		 */
		public WebSite getWebSite() {
			return webSite;
		}

		/**
		 * @param webSite 설정할 webSite
		 */
		public void setWebSite(WebSite webSite) {
			this.webSite = webSite;
		}

		/**
		 * @return builder
		 */
		public PageMaker.Builder getBuilder() {
			return builder;
		}

		/**
		 * @param builder 설정할 builder
		 */
		public void setBuilder(PageMaker.Builder builder) {
			this.builder = builder;
		}


		public static class Builder{
			PageActionAdaptor pageActionAdaptor = new PageActionAdaptor();
			
			public Builder webSite(WebSite webSite){
				this.pageActionAdaptor.webSite = webSite;
				return this;
			} 
			
			public Builder user(User user){
				this.pageActionAdaptor.user = user;
				return this;
			} 
			
			public Builder builder(PageMaker.Builder builder){
				this.pageActionAdaptor.builder = builder;
				return this;
			} 
			
			public PageActionAdaptor build(){
				return pageActionAdaptor;
			}
		}
		
		public static Builder newBuilder(){
			return new Builder();
		}
	}

}