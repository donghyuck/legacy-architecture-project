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
package architecture.ee.web.community.forum;

import java.io.Serializable;
import java.util.Date;

import architecture.common.cache.Cacheable;
import architecture.common.model.NoNamedEntityModelObject;

public interface Forum extends NoNamedEntityModelObject  {
	
	public int getDisplayOrder() ;

	public void setDisplayOrder(int displayOrder) ;


	public Long getObjectType() ;


	public void setObjectType(Long objectType) ;
	
	
	public Long getObjectId() ;
	
	
	public void setObjectId(Long objectId) ;
	
	
	public String getBoardName() ;
	
	
	public void setBoardName(String boardName) ;
	
	
	public String getBoardDesc() ;
	
	
	public void setBoardDesc(String boardDesc) ;
	
	public boolean isCommentYn() ;
	
	
	public void setCommentYn(boolean commentYn) ;
	
	
	public boolean isFileYn() ;
	
	
	public void setFileYn(boolean fileYn) ;
	
	
	public boolean isAnonyYn() ;
	
	
	public void setAnonyYn(boolean anonyYn) ;
	
	
	public boolean isUseYn() ;
	
	
	public void setUseYn(boolean useYn) ;
	
	
	public Date getLastThreadDate() ;
	
	
	public void setLastThreadDate(Date lastThreadDate) ;
	
	
	public Long getTotalCnt() ;
	
	
	public void setTotalCnt(Long totalCnt) ;
	
	
	/**
	 * @return forumId
	 */
	public Long getForumId() ;
	
	public void setForumId(Long forumId) ;
	
	
	
	/**
	 * @return categoryId
	 */
	public Long getCategoryId() ;
	
	
	/**
	 * @param categoryId 설정할 categoryId
	 */
	public void setCategoryId(Long categoryId) ;
	/**
	 * @return category
	 */
	public Category getCategory() ;
	
	
	/**
	 * @param category 설정할 category
	 */
	public void setCategory(Category category) ;
	
	
	public Serializable getPrimaryKeyObject() ;
	
	
	public int getModelObjectType() ;
	
	
	public int getCachedSize() ;

	public Long getCreateId() ;

	public void setCreateId(Long createId) ;

	public Long getModifyId() ;

	public void setModifyId(Long modifyId) ;

}
