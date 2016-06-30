/*
 * Copyright 2016 donghyuck
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

package architecture.ee.web.model;

import java.util.List;

public class ItemList {

	private List<?> items;
	private int totalCount;

	/**
	 * @param items
	 * @param totalCount
	 */

	public ItemList(List<?> items, int totalCount) {
	    super();
	    this.items = items;
	    this.totalCount = totalCount;
	}

	/**
	 * @return items
	 */
	public List<?> getItems() {
	    return items;
	}

	/**
	 * @param items
	 *            설정할 items
	 */
	public void setItems(List<?> items) {
	    this.items = items;
	}

	/**
	 * @return totalCount
	 */
	public int getTotalCount() {
	    return totalCount;
	}

	/**
	 * @param totalCount
	 *            설정할 totalCount
	 */
	public void setTotalCount(int totalCount) {
	    this.totalCount = totalCount;
	}

}