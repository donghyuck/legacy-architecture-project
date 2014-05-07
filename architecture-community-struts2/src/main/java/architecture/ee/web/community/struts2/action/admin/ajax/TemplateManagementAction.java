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

import java.util.Collections;
import java.util.List;

import architecture.ee.exception.NotFoundException;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;
import architecture.ee.web.template.Template;
import architecture.ee.web.template.TemplateManager;

public class TemplateManagementAction extends FrameworkActionSupport {


    private int pageSize = 0 ;
    
    private int startIndex = 0 ;  
    
	private Template targetTemplate;

	private Long objectId = -1L;
	
	private Integer objectType = 0;
	
	private Long templateId = -1L; 
	
	private TemplateManager templateManager ;

	/**
	 * @return pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize 설정할 pageSize
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return startIndex
	 */
	public int getStartIndex() {
		return startIndex;
	}

	/**
	 * @param startIndex 설정할 startIndex
	 */
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}


	/**
	 * @param targetContent 설정할 targetContent
	 */
	public void setTargetTemplate(Template targetTemplate) {
		this.targetTemplate = targetTemplate;
	}

	/**
	 * @return objectId
	 */
	public Long getObjectId() {
		return objectId;
	}

	/**
	 * @param objectId 설정할 objectId
	 */
	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	/**
	 * @return objectType
	 */
	public Integer getObjectType() {
		return objectType;
	}

	/**
	 * @param objectType 설정할 objectType
	 */
	public void setObjectType(Integer objectType) {
		this.objectType = objectType;
	}

	/**
	 * @return contentId
	 */
	public Long getTemplateId() {
		return templateId;
	}

	/**
	 * @param contentId 설정할 contentId
	 */
	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	/**
	 * @return contentManager
	 */
	public TemplateManager getTemplateManager() {
		return templateManager;
	}

	/**
	 * @param contentManager 설정할 contentManager
	 */
	public void setTemplateManager(TemplateManager templateManager) {
		this.templateManager = templateManager;
	}
	

    public int getTotalTargetTemplateCount() {    	
    	return templateManager.getTemplateCount(objectType, objectId);
    }
    
    public Template getTargetTemplate() throws NotFoundException{
    	return templateManager.getTemplate(templateId);
    } 
    
    public List<Template> getTargetTemplates(){   

    	if( objectType < 1 || objectId < 1 ){
    		return Collections.EMPTY_LIST;
    	}
    	 if( pageSize > 0 ){
             return templateManager.getTemplate(objectType, objectId, startIndex, pageSize);      
    	 }
        return templateManager.getTemplate(objectType, objectId);
    }
    
	public String execute() throws Exception {
		return success();
	}
	
}
