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
package architecture.ee.web.forum.impl;

import java.io.Serializable;

import architecture.common.model.ModelObjectType;
import architecture.common.model.support.BaseModelObjectSupport;
import architecture.ee.web.forum.Category;

public class CategoryImpl extends BaseModelObjectSupport implements Category {

	private Long categoryId = -1L ;
	
	private int displayOrder = 0 ;

	public Serializable getPrimaryKeyObject() {
		return categoryId;
	}

	public ModelObjectType getModelObjectType() {
		return ModelObjectType.FORUM_CATEGORY;
	}

	public int getCachedSize() {
		// TODO 자동 생성된 메소드 스텁
		return 0;
	}
	
}
