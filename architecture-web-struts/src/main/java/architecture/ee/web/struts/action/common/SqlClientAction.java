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

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import architecture.ee.services.SqlQueryClient;
import architecture.ee.util.OutputFormat;

import architecture.ee.web.struts.action.support.FrameworkDispatchActionSupport;
import architecture.ee.web.util.ParamUtils;
import architecture.ee.web.util.ServletUtils;
import architecture.ee.web.view.json.JsonView;
/**
 * 
 * @author  <a href="mailto:donghyuck.son@gmail.com">Donghyuck Son </a>
 *
 */

public class SqlClientAction extends FrameworkDispatchActionSupport {
	
	private String [] stringToArray(String parametersString){
		return  StringUtils.splitPreserveAllTokens(parametersString, ",");
	}
	
	
	public ActionForward unitOfWork(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		OutputFormat output = getOutputFormat(request, response);
		
		String script = ParamUtils.getParameter(request, "script" );
		String action = ParamUtils.getParameter(request, "action" );
		String parametersString = ParamUtils.getParameter(request, "parameters", null );
		
		if( action == null )
		    return (mapping.findForward("input"));
		
		SqlQueryClient client = getComponent("sqlQueryClient", SqlQueryClient.class);
		Object result = null;
		if( StringUtils.isEmpty( parametersString)  )
		{
			result = client.unitOfWork(script, action, null);
		}    
		else
		{
			result = client.unitOfWork(script, action, stringToArray(parametersString));
		}

		Map model = getModelMap(request, response);
		if( result instanceof List )
			model.put("items", result);
		else if (result instanceof Map)
			model.put("item", result);		
		
		saveModelMap(request, model);
		
		
		if(output == OutputFormat.JSON ){
			JsonView view = new JsonView();		    
			view.setModelKey("items");			
			if( result instanceof List )
				view.setModelKey("items");
			else if (result instanceof Map)
				view.setModelKey("item");			
			view.render(model, request, response);
			return null;
		}		
		
		return (mapping.findForward(output.name().toLowerCase()));
	}
		
	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
				
		OutputFormat output = getOutputFormat(request, response);
		
		String statement = ParamUtils.getParameter(request, "statement" );
		String parametersString = ParamUtils.getParameter(request, "parameters", null );
		
		SqlQueryClient client = getComponent("sqlQueryClient", SqlQueryClient.class);
		
		List<Map<String, Object>> list ;	
		if( StringUtils.isEmpty( parametersString)  )
		    list = client.list(statement);	
		else
			list = client.list(statement, stringToArray(parametersString));

		Map model = getModelMap(request, response);
		model.put("items", list);
		saveModelMap(request, model);
				
		if(output == OutputFormat.JSON ){
			JsonView view = new JsonView();		    
			view.setModelKey("items");
			view.render(model, request, response);
			return null;
		}		
		
		return (mapping.findForward(output.name().toLowerCase()));
	}
	
	public ActionForward map(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		OutputFormat output = ServletUtils.getOutputFormat(request, response);
		
		String statement = ParamUtils.getParameter(request, "statement" );
		String parametersString = ParamUtils.getParameter(request, "parameters", null );
		
		SqlQueryClient client = getComponent("sqlQueryClient", SqlQueryClient.class);
		
		Map<String, Object> item ;	
		if( StringUtils.isEmpty( parametersString)  )
			item = client.uniqueResult(statement);
		else
			item = client.uniqueResult(statement, stringToArray(parametersString));
		
		Map model = getModelMap(request, response);
		model.put("item", item);
		saveModelMap(request, model);					
		if( output == OutputFormat.JSON ){
			JsonView view = new JsonView();		    
			view.setModelKey("item");
			view.render(model, request, response);
			return null;
		}		
		return (mapping.findForward(output.name().toLowerCase()));
	}	
}