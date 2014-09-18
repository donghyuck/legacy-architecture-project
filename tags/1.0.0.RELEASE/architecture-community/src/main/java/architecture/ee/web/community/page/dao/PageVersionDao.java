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

import java.util.List;

import architecture.ee.web.community.page.PageVersion;

public interface PageVersionDao {
	
	public abstract void update (PageVersion pageVersion);
	
	public abstract void delete (PageVersion pageVersion);
	
	public abstract PageVersion getPageVersion( long pageId, int versionNumber);
	
	public abstract List<PageVersion> getPageVersions( long pageId);
	
	public abstract List<Integer> getPageVersionIds( long pageId);
	
}
