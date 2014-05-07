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
package architecture.ee.web.community.struts2.action;

import java.io.StringReader;
import java.io.StringWriter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import architecture.ee.util.OutputFormat;
import architecture.ee.web.community.ContentNotFoundException;
import architecture.ee.web.community.page.BodyContent;
import architecture.ee.web.community.page.BodyType;
import architecture.ee.web.community.page.DefaultBodyContent;
import architecture.ee.web.community.page.DefaultPage;
import architecture.ee.web.community.page.Page;
import architecture.ee.web.community.page.PageManager;
import architecture.ee.web.community.page.PageNotFoundException;
import architecture.ee.web.community.page.PageState;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;
import architecture.ee.web.util.WebSiteUtils;

/**
 * Page Action 
 * <p>
 * 데이버베이스에 저장된 Page 내용을 기반으로 페이지를 생성한다.
 * </p>
 * 
 * @author donghyuck
 *
 */
public class PageAction extends FrameworkActionSupport {
	
	private static final String DEFAULT_TEMPLATE = "/html/community/page.ftl" ;
		
	private String NOT_FOUND = "page-not-found";
	private String template = DEFAULT_TEMPLATE ;
	private String targetPageName;
	private Long targetPageId = -1L; 	
	private Page targetPage = null ;
			
	
	private FreeMarkerConfig freeMarkerConfig ;
	private PageManager pageManager ;
		
	/**
	 * @return template
	 */
	public String getTemplate() {
		return template;
	}

	/**
	 * @return pageManager
	 */
	public PageManager getPageManager() {
		return pageManager;
	}

	/**
	 * @param template 설정할 template
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

	/**
	 * @param pageManager 설정할 pageManager
	 */
	public void setPageManager(PageManager pageManager) {
		this.pageManager = pageManager;
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
		if(targetPage == null &&  this.targetPageId > 0 ){
			try {
				targetPage = pageManager.getPage(targetPageId);
			} catch (PageNotFoundException e) {
			}	
		}
		if(targetPage == null &&  StringUtils.isNotEmpty(this.targetPageName) ){
			try {
				targetPage = pageManager.getPage(targetPageName);
				targetPageId = targetPage.getPageId();
			} catch (PageNotFoundException e) {
			}
		}		
		if( targetPage == null ){
			targetPage = new DefaultPage();
			targetPage.setBodyContent(new DefaultBodyContent(-1L, -1L, BodyType.RAW, ""));
		}
		
		return targetPage;
	}
	
	public boolean hasPage(){
		return this.getTargetPage().getPageId() > 0 ? true : false;		 
	}

	/**
	 * @return template
	 */
	public String getTargetPageTemplate() {
		return template;
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
		models.put("action", this);	
		 StringWriter stringWriter = new StringWriter();
		 template.process(models, stringWriter) ;				 
		 return stringWriter.toString();
	}
	
	protected boolean isCustomized(){
		return ( StringUtils.isEmpty(getName()) && this.getPageId() < 1 ) ? false : true;
	}
	
	public String execute() throws Exception {
		
		log.debug(getTargetPage());
		
		if( !isCustomized())	
			return super.success();	
				
		Page pageToUse = this.getTargetPage();
		if( pageToUse.getPageState() != PageState.PUBLISHED ){
			return NOT_FOUND;
		}			
		return success();
	}
	
	public String success(){
		if( getOutputFormat() ==  OutputFormat.HTML && isCustomized()){
			return "customize-success" ;			
		}
		return super.success();		
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
}