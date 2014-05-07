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
package architecture.ee.web.community.page;

import java.util.List;

import architecture.common.user.User;

public interface PageManager {

		public abstract Page createPage(User user, BodyType bodyType, String name, String title, String body);
		
		public abstract void updatePage(Page page);
		
		public abstract void updatePage(Page page, boolean forceNewVersion);
		
		public abstract Page getPage(long pageId) throws PageNotFoundException;
		
		public abstract Page getPage(long pageId, int versionId) throws PageNotFoundException ;
		
		public abstract Page getPage(String name) throws PageNotFoundException;
		
		public abstract Page getPage(String name, int versionId)throws PageNotFoundException;
		
		public abstract List<Page> getPages(int objectType);
		
		public abstract List<Page> getPages(int objectType, long objectId);
		
		public abstract int getPageCount(int objectType);
		
		public abstract int getPageCount(int objectType, long objectId);		
		
		public abstract List<PageVersion> getPageVersions(long pageId);
				
}
