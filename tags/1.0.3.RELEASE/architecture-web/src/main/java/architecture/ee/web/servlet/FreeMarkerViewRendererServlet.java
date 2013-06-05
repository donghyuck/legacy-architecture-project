/*
 * Copyright 2012 Donghyuck, Son
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
   
   

package architecture.ee.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.DefaultRequestToViewNameTranslator;

import architecture.common.exception.ComponentDisabledException;
import architecture.ee.web.util.WebApplicatioinConstants;
import architecture.ee.web.util.WebApplicationHelper;

public class FreeMarkerViewRendererServlet extends ViewRendererServlet {

	public FreeMarkerViewRendererServlet() {
		translator = new DefaultRequestToViewNameTranslator();
		viewResolverName = "freemarkerViewResolver";
	}

	@Override
	protected void renderView(HttpServletRequest httpservletrequest,
			HttpServletResponse httpservletresponse) throws Exception {
		
		boolean enabled = WebApplicationHelper.getApplicationBooleanProperty(WebApplicatioinConstants.VIEW_FREEMARKER_ENABLED, false);
		if( enabled )
			super.renderView(httpservletrequest, httpservletresponse);
		else
		    throw new ComponentDisabledException();
		
	}

}
