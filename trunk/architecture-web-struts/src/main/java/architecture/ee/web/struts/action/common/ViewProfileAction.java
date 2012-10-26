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
package architecture.ee.web.struts.action.common;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import architecture.ee.util.OutputFormat;
import architecture.ee.web.struts.action.support.FrameworkActionSupport;
import architecture.ee.web.view.json.JsonView;

/**
 * 
 * 
 * 
 * @author  <a href="mailto:donghyuck.son@gmail.com">Donghyuck Son </a>
 *
 */
public class ViewProfileAction extends FrameworkActionSupport  {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {		
		
		OutputFormat output = getOutputFormat(request, response);
		
		Map model = getModelMap(request, response);
		model.put("user", getUser());		
		saveModelMap(request, model);
		
		// Json processing ....
		if(output == OutputFormat.JSON ){
			JsonView view = new JsonView();		    
			view.setModelKey("user");			
			view.render(model, request, response);
			return null;
		}
		
		return (mapping.findForward(output.name().toLowerCase()));		
	}
	
}
