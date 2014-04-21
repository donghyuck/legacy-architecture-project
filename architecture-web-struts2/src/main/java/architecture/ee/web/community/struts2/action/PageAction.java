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

import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import architecture.ee.web.community.ContentManager;
import architecture.ee.web.community.ContentNotFoundException;
import architecture.ee.web.community.content.Content;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;

public class PageAction extends FrameworkActionSupport {

	private static final String DEFAULT_TEMPLATE = "/html/page.ftl" ;
	
	private ContentManager contentManager;
	
	private Long contentId = -1L; 
	
	private Content targetContent = null ;
		
	private String template = DEFAULT_TEMPLATE ;
	
	private FreeMarkerConfig freeMarkerConfig ;


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
	 * @return contentId
	 */
	public Long getContentId() {
		return contentId;
	}

	/**
	 * @param contentId 설정할 contentId
	 */
	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}

	/**
	 * @return targetContent
	 * @throws ContentNotFoundException 
	 */
	public Content getTargetContent() throws ContentNotFoundException {
		if( targetContent == null)
			targetContent = getContentById(contentId);		
		return targetContent;
	}

	public Content getContentById(Long contentId)throws ContentNotFoundException {
		return contentManager.getContent(contentId);				
	}

	public String getProcessedTargetContentBodyString() throws Exception {		
		Content contentToUse = getTargetContent();		 
		 return getProcessedContentBodyString(contentToUse);
	}
	
	public String getProcessedContentBodyString(Content content) throws Exception {		
		if(getFreeMarkerConfig() == null)
			this.freeMarkerConfig = getComponent(FreeMarkerConfig.class);
		
		freemarker.template.Template  template = new freemarker.template.Template (
				 content.getContentId().toString(), 
				new StringReader(content.getBody()), 
				 getFreeMarkerConfig().getConfiguration());
		
		models.put("action", this);	 
		
		 StringWriter stringWriter = new StringWriter();
		 template.process(models, stringWriter) ;			
				 
		 return stringWriter.toString();
	}
	
	public String execute() throws Exception {		
		return success();
	}
	
	
	/**
	 * @param targetContent 설정할 targetContent
	 */
	public void setTargetContent(Content targetContent) {
		this.targetContent = targetContent;
	}

	/**
	 * @return contentManager
	 */
	public ContentManager getContentManager() {
		return contentManager;
	}

	/**
	 * @param contentManager 설정할 contentManager
	 */
	public void setContentManager(ContentManager contentManager) {
		this.contentManager = contentManager;
	}	

}