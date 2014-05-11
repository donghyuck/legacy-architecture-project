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

import architecture.common.model.factory.ModelTypeFactory;
import architecture.common.model.support.BaseModelObjectSupport;
import architecture.ee.web.community.forum.Category;
import architecture.ee.web.community.forum.Forum;

public class ForumImpl extends BaseModelObjectSupport implements Forum {

	private Long forumId = -1L ;
	
	private Long categoryId = -1L;
	
	private Category  category;
	
	private int displayOrder = 0;
	
	private boolean allowAnonymousPosts = false;
	
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
