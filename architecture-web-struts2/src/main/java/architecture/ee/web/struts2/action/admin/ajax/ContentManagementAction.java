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
package architecture.ee.web.struts2.action.admin.ajax;

import java.util.Collections;
import java.util.List;

import architecture.ee.exception.NotFoundException;
import architecture.ee.web.content.Content;
import architecture.ee.web.content.ContentManager;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;

public class ContentManagementAction extends FrameworkActionSupport {


    private int pageSize = 0 ;
    
    private int startIndex = 0 ;  
    
	private Content targetContent;

	private Long objectId = -1L;
	
	private Integer objectType = 0;
	
	private Long contentId = -1L; 
	
	private ContentManager contentManager ;

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
	public void setTargetContent(Content targetContent) {
		this.targetContent = targetContent;
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
	

    public int getTotalTargetContentCount() {    	
    	return contentManager.getContentCount(objectType, objectId);
    }
    
    public Content getTargetContent() throws NotFoundException{
    	return contentManager.getContent(contentId);
    } 
    
    public List<Content> getTargetContents(){   

    	if( objectType < 1 || objectId < 1 ){
    		return Collections.EMPTY_LIST;
    	}
    	 if( pageSize > 0 ){
             return contentManager.getContent(objectType, objectId, startIndex, pageSize);      
    	 }
        return contentManager.getContent(objectType, objectId);
    }
    
	public String execute() throws Exception {
		return success();
	}
	
}
