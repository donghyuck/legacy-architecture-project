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
package architecture.ee.web.community.spring.controller;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import architecture.ee.exception.NotFoundException;
import architecture.ee.web.community.page.Page;
import architecture.ee.web.community.page.PageMaker;
import architecture.ee.web.community.page.PageManager;
import architecture.ee.web.site.WebSite;
import architecture.ee.web.site.WebSiteNotFoundException;
import architecture.ee.web.util.WebSiteUtils;

@Controller ("communityPageController")
@RequestMapping("/pages")
public class ContentController {
	
	private static final Log log = LogFactory.getLog(ContentController.class);
	
	private static final String DEFAULT_PAGE_TEMPLATE = "/html/community/page";		
	
	@Inject
	@Qualifier("pageManager")
	private PageManager pageManager ;

	@Inject
	@Qualifier("freemarkerConfig")
	private FreeMarkerConfig freeMarkerConfig ;
	
	@Autowired private ServletContext servletContext;
	
	public ContentController() {
	}

	@RequestMapping(value="/{name}", method=RequestMethod.GET)
	public String connect(@PathVariable String name, NativeWebRequest request, Model model) throws NotFoundException, IOException {		
		
		
		Page page = pageManager.getPage(name);		
		WebSite website = getCurrentWebSite(request.getNativeRequest(HttpServletRequest.class));
		PageMaker.Builder builder = PageMaker.newBuilder().configuration(freeMarkerConfig.getConfiguration()).servletContext(servletContext).page(page).model(model).request(request.getNativeRequest(HttpServletRequest.class));
		model.addAttribute("website", website);
		model.addAttribute("builder", builder);
		return DEFAULT_PAGE_TEMPLATE;
	}
	
	
	private WebSite getCurrentWebSite(HttpServletRequest request) throws WebSiteNotFoundException{
		return WebSiteUtils.getWebSite(request);
	}
	

}
