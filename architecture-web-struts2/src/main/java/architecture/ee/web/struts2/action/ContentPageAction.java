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
package architecture.ee.web.struts2.action;

import architecture.ee.exception.NotFoundException;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;
import architecture.ee.web.template.Template;
import architecture.ee.web.template.TemplateManager;
import architecture.ee.web.view.freemarker.TemplateAware;

public class ContentPageAction extends FrameworkActionSupport implements TemplateAware {

	private static final String DEFAULT_PAGE =  "/html/page.ftl";
	private String page = DEFAULT_PAGE ;
	private TemplateManager contentManager ;
	long contentId = -1L;
	private Template targetContent = null;

	/**
	 * @return contentManager
	 */
	public TemplateManager getContentManager() {
		return contentManager;
	}

	/**
	 * @param contentManager 설정할 contentManager
	 */
	public void setContentManager(TemplateManager contentManager) {
		this.contentManager = contentManager;
	}

	/**
	 * @param page 설정할 page
	 */
	public void setPage(String page) {
		this.page = page;
	}

	public Long getTemplateId() {
		return contentId;
	}

	public void setContentId(long contentId) {
		this.contentId = contentId;
	}
	
	@Override
	public String execute() throws Exception {
		return SUCCESS;
	}

	/**
	 * @return pageName
	 */
	public String getPage() {
		if( contentId > 0 ){
			try {
				return getTargetTemplate().getLocation();
			} catch (Exception e) {
				return DEFAULT_PAGE;
			}
		}
		return page;
	}
	
	public Template getTargetTemplate(){
		if(targetContent == null && contentId > 0 )
		{
			try {
				targetContent = contentManager.getTemplate(contentId);
			} catch (NotFoundException e) {
				targetContent = null;
			}
		}
		return targetContent;
	}
	


	public String getTemplateType() {
		try {
			return getTargetTemplate().getTemplateType();
		} catch (Exception e) {
			return null;
		}
	}
	
}
