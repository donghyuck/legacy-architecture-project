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
package architecture.ee.web.community.struts2.action;

import org.apache.commons.lang3.StringUtils;

import architecture.ee.web.community.page.Page;
import architecture.ee.web.community.page.PageState;
import architecture.ee.web.community.struts2.action.support.PageActionSupport;
import architecture.ee.web.site.WebSite;

public class DisplayPageAction extends PageActionSupport {

	private static final String DEFAULT_TEMPLATE = "/html/subpage.ftl" ;					

	public DisplayPageAction() {
		setTemplate(DEFAULT_TEMPLATE);
	}
				
	public boolean hasWebSitePage(String name){
		WebSite webSiteToUse = getWebSite();
		long pageIdToUse  =webSiteToUse.getLongProperty(name, -1L);
		if( pageIdToUse > 0 ){
			setPageId(pageIdToUse);
			setTargetPageToNull();
		}
		return hasPage();		
	}

	public String execute() throws Exception {						
		Page pageToUse = getTargetPage();
		if( pageToUse.getPageState() != PageState.PUBLISHED ){
			return NOT_FOUND;
		}		
		if(StringUtils.isNotEmpty( pageToUse.getProperty("template", null) ) ){
			setTemplate( pageToUse.getProperty("template", null) );
		}
		return success();
	}
	
}
