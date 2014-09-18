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

import architecture.ee.util.ApplicationHelper;

public class PageVersionHelper {

	public PageVersionHelper() {

	}
	
	public static List<PageVersion> getPageVersions(long pageId) {
		return ApplicationHelper.getComponent(PageManager.class).getPageVersions(pageId);
	}

	public static PageVersion getPublishedPageVersion(long pageId) {
		List<PageVersion> list = getPageVersions(pageId);
		for( PageVersion pv : list ){
			if( pv.getPageState() == PageState.PUBLISHED )
				return pv;
		}
		return null;
	}

	public static PageVersion getNewestPageVersion(long pageId) {
		List<PageVersion> list = getPageVersions(pageId);
		if( list.size() == 0)
			return null;
		return list.get(0);
	}

	public static PageVersion getDeletedPageVersion(long pageId) {
		List<PageVersion> list = getPageVersions(pageId);
		for( PageVersion pv : list ){
			if( pv.getPageState() == PageState.DELETED )
				return pv;
		}
		return null;
	}
	
}
