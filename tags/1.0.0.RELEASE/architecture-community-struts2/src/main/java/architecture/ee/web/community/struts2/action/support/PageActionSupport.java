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
package architecture.ee.web.community.struts2.action.support;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.views.freemarker.ScopesHashModel;
import org.apache.struts2.views.util.ContextUtil;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import architecture.ee.web.community.ContentNotFoundException;
import architecture.ee.web.community.page.BodyContent;
import architecture.ee.web.community.page.BodyType;
import architecture.ee.web.community.page.DefaultBodyContent;
import architecture.ee.web.community.page.DefaultPage;
import architecture.ee.web.community.page.Page;
import architecture.ee.web.community.page.PageManager;
import architecture.ee.web.community.page.PageNotFoundException;
import architecture.ee.web.navigator.MenuComponent;
import architecture.ee.web.navigator.MenuNotFoundException;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;
import architecture.ee.web.util.WebSiteUtils;

import com.opensymphony.xwork2.util.ValueStack;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;


public class PageActionSupport extends FrameworkActionSupport {

	public static final String NOT_FOUND = "page-not-found";
	
	public static final String CUSTOMIZED_SUCCESS = "customized-success";
	
	public static final String NAVIGATOR_SELECTED_NAME_KEY = "navigator.selected.name";
	
	private String template ;
	
	private FreeMarkerConfig freeMarkerConfig ;
	
	private PageManager pageManager ;
		
	private String targetPageName;
	
	private Long targetPageId = -1L; 	
	
	private Page targetPage = null ;
	
	public PageActionSupport(){
		
	}


	/**
	 * @return name
	 */
	public String getName() {
		return targetPageName;
	}

	/**
	 * @param name 설정할 name
	 */
	public void setName(String name) {
		this.targetPageName = name;
	}

	/**
	 * @return pageId
	 */
	public Long getPageId() {
		return targetPageId;
	}

	/**
	 * @param pageId 설정할 pageId
	 */
	public void setPageId(Long pageId) {
		this.targetPageId = pageId;
	}

		
	/**
	 * @return targetPage
	 */
	public Page getTargetPage() {
		if(this.targetPage == null &&  this.targetPageId > 0 ){
			try {
				this.targetPage = this.pageManager.getPage(this.targetPageId);
			} catch (PageNotFoundException e) {
			}	
		}
		if(this.targetPage == null &&  StringUtils.isNotEmpty(this.targetPageName) ){
			try {
				this.targetPage = this.pageManager.getPage(this.targetPageName);
				this.targetPageId = this.targetPage.getPageId();
			} catch (PageNotFoundException e) {
			}
		}		
		if( this.targetPage == null ){
			this.targetPage = new DefaultPage();
			this.targetPage.setBodyContent(new DefaultBodyContent(-1L, -1L, BodyType.RAW, ""));
		}		
		return this.targetPage;
	}
	
	public boolean isSetNavigator () {
		Page pageToUse = getTargetPage();
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
			Page pageToUse = getTargetPage();
			String menuName = pageToUse.getProperty("page.menu.name", "USER_MENU");
			String current = pageToUse.getProperty(NAVIGATOR_SELECTED_NAME_KEY, null);			
			return getWebSiteMenu(menuName, current );			
		}
		throw new MenuNotFoundException();
	}

	/**
	 * @return targetContent
	 * @throws ContentNotFoundException 
	 */
	
	public String getPlainBodyText(){
		return getTargetPage().getBodyText();
	}
	
	public String getProcessedBodyText(){
		Page pageToUse = getTargetPage();
		if( pageToUse.getBodyContent().getBodyType() == BodyType.FREEMARKER ){
			if( StringUtils.isNotEmpty( pageToUse.getBodyText() ) )
				try {
					return processedContentBodyText(pageToUse.getBodyContent());
				} catch (Exception e) {
					log.error(e);
				}
		}
		return getPlainBodyText();
	}
	
	protected String processedContentBodyText(BodyContent content) throws Exception {		
		if(getFreeMarkerConfig() == null)
			this.freeMarkerConfig = getComponent(FreeMarkerConfig.class);		
		
		freemarker.template.Template  template = new freemarker.template.Template ("page-body-"+ content.getBodyId(), new StringReader(content.getBodyText()), getFreeMarkerConfig().getConfiguration());		
		
		ObjectWrapper objectWrapper = freeMarkerConfig.getConfiguration().getObjectWrapper();
		ValueStack stack = ServletActionContext.getValueStack(request);		
		ScopesHashModel model = new ScopesHashModel(objectWrapper, ServletActionContext.getServletContext(), request, stack);		
		buildTemplateModel(model, stack, this, request, response);		
		HashMap<String, Object> map = new HashMap<String, Object>();		
		populateStatics(objectWrapper, map);
		model.putAll(map);
		
		StringWriter stringWriter = new StringWriter();
		template.process(model, stringWriter) ;				 
		
		return stringWriter.toString();
	}
	
	protected void buildTemplateModel(ScopesHashModel model, ValueStack stack, Object action, HttpServletRequest request, HttpServletResponse response) {
		 Map standard = ContextUtil.getStandardContext(stack, request, response);
		 model.putAll(standard);
    }
	protected void populateStatics(ObjectWrapper wrapper, Map<String, Object> model){
		BeansWrapper b = (BeansWrapper) wrapper;
		try {
			TemplateHashModel enumModels = b.getEnumModels();
			try {

			} catch (Exception e) {
				log.error(e);
			}
			model.put("enums", b.getEnumModels());
		} catch (UnsupportedOperationException e) {
		}

		TemplateHashModel staticModels = b.getStaticModels();
		try {
			model.put("TextUtils",              staticModels.get("architecture.common.util.TextUtils"));
			model.put("StringUtils",              staticModels.get("architecture.common.util.StringUtils"));
			model.put("L10NUtils",              staticModels.get("architecture.common.util.L10NUtils"));
			model.put("LocaleUtils",             staticModels.get("architecture.ee.web.util.LocaleUtils"));
			model.put("HtmlUtils",               staticModels.get("architecture.ee.web.util.HtmlUtils"));
			model.put("ServletUtils",             staticModels.get("architecture.ee.web.util.ServletUtils"));
			model.put("ParamUtils",              staticModels.get("architecture.ee.web.util.ParamUtils"));
			model.put("WebSiteUtils",              staticModels.get("architecture.ee.web.util.WebSiteUtils"));
			model.put("CompanyUtils",         staticModels.get("architecture.user.util.CompanyUtils"));
			model.put("SecurityHelper",         staticModels.get("architecture.common.user.SecurityHelper"));
			model.put("ApplicationHelper",     staticModels.get("architecture.ee.util.ApplicationHelper"));							
			model.put("WebApplicationHelper",     staticModels.get("architecture.ee.web.util.WebApplicationHelper"));
		} catch (TemplateModelException e) {
			log.error(e);
		}		
		model.put("statics", BeansWrapper.getDefaultInstance().getStaticModels());
	}
	
	
	protected void setTargetPageToNull(){
		this.targetPage = null;
	}
	
	public boolean isAllowedSignIn(){		
		return WebSiteUtils.isAllowedSignIn(getWebSite());
	}
	
	public boolean isAllowedSignup(){
		return WebSiteUtils.isAllowedSignup(getWebSite());
	}

	public boolean isAllowedSocialConnect(){
		return WebSiteUtils.isAllowedSocialConnect(getWebSite());
	}
	/**
	 * @return template
	 */
	public String getTemplate() {
		return template;
	}


	/**
	 * @param template 설정할 template
	 */
	public void setTemplate(String template) {
		this.template = template;
	}


	/**
	 * @return freeMarkerConfig
	 */
	public FreeMarkerConfig getFreeMarkerConfig() {
		return freeMarkerConfig;
	}

	/**
	 * @param freeMarkerConfig 설정할 freeMarkerConfig
	 */
	public void setFreeMarkerConfig(FreeMarkerConfig freeMarkerConfig) {
		this.freeMarkerConfig = freeMarkerConfig;
	}

	/**
	 * @return pageManager
	 */
	public PageManager getPageManager() {
		return pageManager;
	}

	/**
	 * @param pageManager 설정할 pageManager
	 */
	public void setPageManager(PageManager pageManager) {
		this.pageManager = pageManager;
	}

	public boolean hasPage(){
		return getTargetPage().getPageId() > 0 ? true : false;		 
	}
}