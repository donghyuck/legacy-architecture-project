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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import architecture.common.user.CompanyManager;
import architecture.common.user.CompanyNotFoundException;
import architecture.common.user.SecurityHelper;
import architecture.common.user.User;
import architecture.ee.exception.NotFoundException;
import architecture.ee.web.community.page.PageManager;
import architecture.ee.web.community.spring.controller.DisplayController.PageActionAdaptor;
import architecture.ee.web.site.WebSite;
import architecture.ee.web.site.WebSiteManager;
import architecture.ee.web.util.WebSiteUtils;

@Controller("community-secure-display-controller")
@RequestMapping("/secure/display")
public class SecureDisplayController {

    private static final Log log = LogFactory.getLog(DisplayController.class);
    private static final String DEFAULT_CONTENT_TYPE = "text/html;charset=UTF-8";

    @Inject
    @Qualifier("pageManager")
    private PageManager pageManager;

    @Inject
    @Qualifier("freemarkerConfig")
    private FreeMarkerConfig freeMarkerConfig;

    @Inject
    @Qualifier("webSiteManager")
    private WebSiteManager webSiteManager;

    @Inject
    @Qualifier("companyManager")
    private CompanyManager companyManager;

    public SecureDisplayController() {

    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String template(@RequestParam(value = "source") String view,
	    @RequestParam(value = "companyId", defaultValue = "0") Long companyId, HttpServletRequest request,
	    HttpServletResponse response, Model model) throws NotFoundException, IOException {

	User user = SecurityHelper.getUser();
	WebSite website = WebSiteUtils.getWebSite(request);

	DisplayController.PageActionAdaptor.Builder builder = PageActionAdaptor.newBuilder();
	if (companyId > 0)
	    try {
		builder.company(companyManager.getCompany(companyId));
	    } catch (CompanyNotFoundException e) {
		// ignore
	    }
	model.addAttribute("action", builder.webSite(website).user(user).build());
	setContentType(response);
	log.debug("path:" + view);
	return view;
    }

    protected void setContentType(HttpServletResponse response) {
	response.setContentType("text/html;charset=UTF-8");
    }

}
