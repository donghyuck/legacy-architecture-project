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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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
import architecture.common.user.Company;
import architecture.common.user.CompanyNotFoundException;
import architecture.common.user.SecurityHelper;
import architecture.common.user.User;
import architecture.ee.exception.NotFoundException;
import architecture.ee.web.community.page.BodyType;
import architecture.ee.web.community.page.Page;
import architecture.ee.web.community.page.PageMaker;
import architecture.ee.web.community.page.PageManager;
import architecture.ee.web.community.page.PageNotFoundException;
import architecture.ee.web.community.page.PageAdaptor;
import architecture.ee.web.navigator.MenuComponent;
import architecture.ee.web.navigator.MenuNotFoundException;
import architecture.ee.web.site.WebPageNotFoundException;
import architecture.ee.web.site.WebSite;
import architecture.ee.web.site.WebSiteManager;
import architecture.ee.web.site.WebSiteNotFoundException;
import architecture.ee.web.site.page.WebPage;
import architecture.ee.web.util.WebSiteUtils;

@Controller ("community-display-controller")
@RequestMapping("/display")
public class DisplayController {
	
		
	private static final Log log = LogFactory.getLog(DisplayController.class);
	
	private static final String DEFAULT_PAGE_TEMPLATE = "/html/community/page.ftl";		
	
	private static final String DEFAULT_CONTENT_TYPE = "text/html;charset=UTF-8";
	
	@Inject
	@Qualifier("pageManager")
	private PageManager pageManager ;

	@Inject
	@Qualifier("freemarkerConfig")
	private FreeMarkerConfig freeMarkerConfig ;

	@Inject
	@Qualifier("webSiteManager")
	private WebSiteManager webSiteManager;
	
	@Autowired private ServletContext servletContext;
	
	public DisplayController() {
	}
	
	
	/**
	 *  display/{objectType}/{objectId}/{filename}
	 *  
	 * 
	 * @param objectType
	 * @param objectId
	 * @param filename
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws IOException 
	 * @throws PageNotFoundException
	 * @throws WebSiteNotFoundException
	 */
	@RequestMapping(value="/{objectType}/{objectId}/{filename:.+}", method=RequestMethod.GET)
	public String page (
			@PathVariable Integer objectType, 
			@PathVariable Long objectId, 
			@PathVariable String filename, 
			@RequestParam(value="siteId", defaultValue="0", required=false ) int siteId,
			@RequestParam(value="version", defaultValue="0", required=false ) int version,
			HttpServletRequest request, 
			HttpServletResponse response, 
			Model model ) throws IOException{		
		User user = SecurityHelper.getUser();				
		String restOfTheUrl = (String)request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		log.debug(filename  + ":" + restOfTheUrl);		
		Page page = null;
		WebSite website = null;
		try {			
			page = getPage( filename, version );
			website = getWebSite(siteId, request);		
			
			setPageActionAdaptor(page, website, user, model, request, response);			
		} catch (NotFoundException e) {
			response.sendError(404);
		}
		setContentType(response);	
		String template = page.getProperty("template", DEFAULT_PAGE_TEMPLATE );		
		return getFreemarkerView(template);
	}
	
	/**
	 *   
	 *   /display/aa.html
	 *   
	 *   
	 * @todo 존재하지 않는 페이지를 반복하여 처리하는 것을 방지하는  기능이 요구됨
	 *    
	 * @param filename
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws NotFoundException
	 * @throws IOException
	 */
	@RequestMapping(value="/{filename:.+}", method=RequestMethod.GET)
	public String page(
			@PathVariable String filename, 
			@RequestParam(value="siteId", defaultValue="0", required=false ) int siteId,
			@RequestParam(value="version", defaultValue="0", required=false ) int version,
			HttpServletRequest request, 
			HttpServletResponse response, 
			Model model) throws IOException {		
		
		boolean notFound = false;
		User user = SecurityHelper.getUser();		
		Page page = null;
		WebSite website = null;		
		String template = null;		
		try {
			website = getWebSite(siteId, request);		
			page = getPage( filename, version );			
			setPageActionAdaptor(page, website, user, model, request, response);
			template = page.getProperty("template", DEFAULT_PAGE_TEMPLATE );	
			setContentType(response);		
		} catch (NotFoundException e) {
			notFound = true;
		}
		
		if( page == null && website != null ){
			try{
				WebPage webpage = webSiteManager.getWebPageByName(website, filename);			
				setPageActionAdaptor(new PageAdaptor(webpage), website, user, model, request, response);
				setContentType(webpage.getContentType(), response);		
				template = webpage.getTemplate();
			} catch (NotFoundException e) {
				response.sendError(404);
			}				
		}
		return getFreemarkerView(template);
	}
	
	protected void setPageActionAdaptor(
			Page page,			
			WebSite site,
			User user,
			Model model,
			HttpServletRequest request, 
			HttpServletResponse response			
			){
		PageMaker.Builder builder = PageMaker.newBuilder().configuration(freeMarkerConfig.getConfiguration()).servletContext(servletContext).page(page).model(model).request(request);			
		model.addAttribute("action", PageActionAdaptor.newBuilder().webSite(site).builder(builder).user(user).build());	
	}
	
	protected Page getPage(String filename, int version) throws PageNotFoundException{
		Page page;
		if(version != 0){
			page = pageManager.getPage(filename, version);	
		}else{
			page = pageManager.getPage(filename);	
		} 	
		return page;
	}
	
	protected WebSite getWebSite(long webSiteId, HttpServletRequest request) throws WebSiteNotFoundException{
		WebSite website; 
		if( webSiteId == 0 )
			website = WebSiteUtils.getWebSite(request);
		else
			website = webSiteManager.getWebSiteById(webSiteId);		
		return website;
	}	
	
	/**
	 * display/{siteId}/{filename}
	 * 
	 * @param siteId 디폴트 0 값은 현재 접근 도에인에 따른 사이트를 의미
	 * @param filename
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/{siteId:[\\p{Digit}]+}/{filename:.+}", method=RequestMethod.GET)
	public String webpage (
		@PathVariable Long siteId, 
		@PathVariable String filename, 
		HttpServletRequest request, 
		HttpServletResponse response, Model model) throws IOException {			
		
		User user = SecurityHelper.getUser();			
		String restOfTheUrl = (String)request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		log.debug(filename  + ":" + restOfTheUrl);				
		WebSite website = null;
		WebPage page = null;
		try{
			website = getWebSite(siteId, request) ;
			page = webSiteManager.getWebPageByName(website, filename);			
			setPageActionAdaptor(new PageAdaptor(page), website, user, model, request, response);
		} catch (NotFoundException e) {
			response.sendError(404);
		}			
		setContentType(page.getContentType(), response);
		return getFreemarkerView(page.getTemplate(), DEFAULT_PAGE_TEMPLATE );
	}

	@RequestMapping(value = "/", method=RequestMethod.GET, params={"source"})
	public String page(
			@RequestParam(value="source") String source, 
			@RequestParam(value="siteId", defaultValue="0", required=false ) int siteId,
			HttpServletRequest request, 
			HttpServletResponse response, 
			Model model) throws IOException {		
		
		User user = SecurityHelper.getUser();	
		WebSite website = null;			
		try {
			website = getWebSite(siteId, request) ;
			setPageActionAdaptor(null, website, user, model, request, response);
		} catch (NotFoundException e) {
			response.sendError(404);
		}
		setContentType(response);
		return getFreemarkerView(source);
	}	
	
	
		
	protected String getFreemarkerView(String view){	
		return getFreemarkerView(view, DEFAULT_PAGE_TEMPLATE);
	}
	
	protected String getFreemarkerView(String view, String defaultView ){		
		String viewToUse =StringUtils.defaultString(view, defaultView );
		if( StringUtils.endsWithAny(viewToUse, "ftl") ){
			viewToUse = StringUtils.removeEndIgnoreCase(viewToUse, ".ftl");			
		}		
		return viewToUse;
	}
	

	private void setContentType(String contentType, HttpServletResponse response){
		String contentTypeToUse = StringUtils.defaultString(contentType, DEFAULT_CONTENT_TYPE );		
		response.setContentType(contentTypeToUse);
	}
	
	private void setContentType(HttpServletResponse response){
		response.setContentType(DEFAULT_CONTENT_TYPE);
	}
	
	private boolean hasPermissions(Page page, User user){				
		 if (page.getObjectType() == ModelTypeFactory.getTypeIdFromCode("COMPANY") && page.getObjectId() != user.getCompanyId() ){
			 return false;			
		}else if (page.getObjectType() == ModelTypeFactory.getTypeIdFromCode("USER") && page.getObjectId() != user.getUserId()){
			return false;			
		}else if (page.getObjectType() == ModelTypeFactory.getTypeIdFromCode("WEBSITE")){
			return true;
		}
		return true;
	}
	

	
	public static class PageActionAdaptor {
		
		public static final String DEFAULT_PAGE_CONTENT_TYPE = "text/html;charset=UTF-8";
		public static final String DEFAULT_PAGE_MENU_NAME = "USER_MENU";
		public static final String NAVIGATOR_SELECTED_NAME_KEY = "navigator.selected.name";
		public static final String PAGE_MENU_NAME_KEY = "page.menu.name";
		public static final String PAGE_TEMPLATE_KEY = "page.template";
				
		private Company targetCompany ;
		
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
		
		public boolean hasWebSiteMenu(String name ){
			if( webSite != null ){
				try {
					WebSiteUtils.getMenuComponent( webSite.getMenu(), name);
					return true;
				} catch (MenuNotFoundException e) {
					return false;
				}				
			}
			return false;
		}
		
		
		public List<String> getMenuNames()throws MenuNotFoundException { 
			if( webSite != null ){
				Set<String> names = WebSiteUtils.getMenuNames(webSite);
				log.debug( names );
				List<String> list = new ArrayList<String>(names);
				log.debug(list);
				return list;
			}
			return Collections.EMPTY_LIST;
		}
		
		
		public String getContentType(){
			return DEFAULT_PAGE_CONTENT_TYPE;
		}
		
		public boolean isSetPage(){
			return this.builder != null;
		}
		
		public boolean isSetTemplate(){
			Page pageToUse = getPage();
			if( pageToUse != null && pageToUse.getPageId() > 0 ){			
				return StringUtils.isNotEmpty(pageToUse.getProperty(PAGE_TEMPLATE_KEY, null));				
			}
			return false;
		}
		
		public String getView(){
			if(isSetTemplate()){
				Page pageToUse = getPage();
				pageToUse.getProperty(PAGE_TEMPLATE_KEY, null);
			}
			return DisplayController.DEFAULT_PAGE_TEMPLATE;
		}
		
		public boolean isSetNavigator (){
			Page pageToUse = getPage();
			if( pageToUse != null && pageToUse.getPageId() > 0 ){		
				return StringUtils.isNotEmpty(pageToUse.getProperty(NAVIGATOR_SELECTED_NAME_KEY, null));	
			}		
			return false;
		}
				
		public MenuComponent getNavigator() throws MenuNotFoundException{
			if(isSetNavigator()){
				Page pageToUse = getPage();
				String name = pageToUse.getProperty(PAGE_MENU_NAME_KEY, DEFAULT_PAGE_MENU_NAME);
				String selected = pageToUse.getProperty(NAVIGATOR_SELECTED_NAME_KEY, null);			
				return WebSiteUtils.getMenuComponent(getWebSiteMenu(name), selected );			
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
		 * @return targetCompany
		 */
		public Company getTargetCompany() {
			if( targetCompany == null && user != null)
				return user.getCompany();
			return targetCompany;
		}

		/**
		 * @param targetCompany 설정할 targetCompany
		 */
		public void setTargetCompany(Company company) {
			this.targetCompany = company;
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

			public Builder company(Company company){
				this.pageActionAdaptor.targetCompany = company;
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