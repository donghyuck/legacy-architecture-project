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
package architecture.ee.web.security.spring.authentication;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.ui.ModelMap;

import architecture.common.util.StringUtils;
import architecture.ee.util.OutputFormat;
import architecture.ee.web.util.WebApplicatioinConstants;
import architecture.ee.web.view.json.JsonView;

public class ExtendedAuthenticationSuccessHandler extends
		SimpleUrlAuthenticationSuccessHandler {
	

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException { 
		
		OutputFormat output = getOutputFormat( request, response );
		if( output == OutputFormat.JSON )
		{
			// Token 		
			String referer = request.getHeader("Referer");			
			Map model = new ModelMap();
			Map<String, String> item = new java.util.HashMap<String, String>() ; 
			item.put("success", "true");	
			if( StringUtils.isNotEmpty(referer))
				item.put("referer", referer );
			model.put("item", item);			
			request.setAttribute(WebApplicatioinConstants.MODEL_ATTRIBUTE, model);			
			if(output == OutputFormat.JSON ){
				JsonView view = new JsonView();		    
				view.setModelKey("item");			
				try {
					view.render(model, request, response);
				} catch (Exception e) {
				}
				return;
			}			
		}

		super.onAuthenticationSuccess(request, response, authentication );		
	}
	
	protected OutputFormat getOutputFormat(
			HttpServletRequest httpservletrequest,
			HttpServletResponse httpservletresponse) {
		String temp = httpservletrequest.getParameter("output");
		String formatString = StringUtils.defaultString(temp, "html");
		OutputFormat format = OutputFormat.stingToOutputFormat(formatString);
		return format;
	}

}
