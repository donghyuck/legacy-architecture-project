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
package architecture.ee.web.community.forum.impl;

import java.io.Serializable;
import java.util.Date;

import architecture.common.model.factory.ModelTypeFactory;
import architecture.common.model.support.BaseModelObjectSupport;
import architecture.ee.web.community.forum.Category;
import architecture.ee.web.community.forum.Forum;

public class ForumImpl extends BaseModelObjectSupport implements Forum {

	private Long forumId = -1L ; // BOARD_ID
	
	private Long categoryId = -1L;
	
	private Category  category;
	
	private int displayOrder = 0;
	
	private boolean allowAnonymousPosts = false;
	
	private Long objectType;
	
	private Long objectId;
	
	private String boardName;
	
	private String boardDesc;
	
	private boolean commentYn;
	
	private boolean fileYn;
	
	private boolean anonyYn;
	
	private boolean useYn;
	
	private Date lastThreadDate;
	
	private Long totalCnt;
	
	private Long createId;
	
	private Long modifyId;
	
	
	
	
	
	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	public Long getModifyId() {
		return modifyId;
	}

	public void setModifyId(Long modifyId) {
		this.modifyId = modifyId;
	}

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	public Long getObjectType() {
		return objectType;
	}

	public void setObjectType(Long objectType) {
		this.objectType = objectType;
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public String getBoardName() {
		return boardName;
	}

	public void setBoardName(String boardName) {
		this.boardName = boardName;
	}

	public String getBoardDesc() {
		return boardDesc;
	}

	public void setBoardDesc(String boardDesc) {
		this.boardDesc = boardDesc;
	}

	public boolean isCommentYn() {
		return commentYn;
	}

	public void setCommentYn(boolean commentYn) {
		this.commentYn = commentYn;
	}

	public boolean isFileYn() {
		return fileYn;
	}

	public void setFileYn(boolean fileYn) {
		this.fileYn = fileYn;
	}

	public boolean isAnonyYn() {
		return anonyYn;
	}

	public void setAnonyYn(boolean anonyYn) {
		this.anonyYn = anonyYn;
	}

	public boolean isUseYn() {
		return useYn;
	}

	public void setUseYn(boolean useYn) {
		this.useYn = useYn;
	}

	public Date getLastThreadDate() {
		return lastThreadDate;
	}

	public void setLastThreadDate(Date lastThreadDate) {
		this.lastThreadDate = lastThreadDate;
	}

	public Long getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(Long totalCnt) {
		this.totalCnt = totalCnt;
	}

	/**
	 * @return forumId
	 */
	public Long getForumId() {
		return forumId;
	}

	/**
	 * @param forumId 설정할 forumId
	 */
	public void setForumId(Long forumId) {
		this.forumId = forumId;
	}

	
	/**
	 * @return categoryId
	 */
	public Long getCategoryId() {
		return categoryId;
	}

	/**
	 * @param categoryId 설정할 categoryId
	 */
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * @return category
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * @param category 설정할 category
	 */
	public void setCategory(Category category) {
		this.category = category;
	}

	public Serializable getPrimaryKeyObject() {
		return forumId;
	}

	public int getModelObjectType() {
		return ModelTypeFactory.getTypeIdFromCode("FORUM");
	}

	public int getCachedSize() {

		return 0;
	}



}
