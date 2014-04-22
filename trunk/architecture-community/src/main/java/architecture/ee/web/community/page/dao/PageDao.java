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
package architecture.ee.web.community.page.dao;

import architecture.ee.web.community.page.Page;

public interface PageDao {
	
	public abstract void create(Page page);
	
	public abstract void update(Page page, boolean flag);
	
	public abstract void delete(Page page);
	
	public abstract Page getPageById(long pageId);
	
	public abstract Page getPageById(long pageId, int versionNumber);
	
	public abstract Page getPageByName(String name);
	
	public abstract Page getPageByName(String name, int versionNumber);
	
	public abstract Page getPageByTitle(int objectType, long objectId, String title);	
	
}
