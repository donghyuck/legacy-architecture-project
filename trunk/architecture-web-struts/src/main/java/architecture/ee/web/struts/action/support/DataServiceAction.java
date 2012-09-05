package architecture.ee.web.struts.action.support;


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
import architecture.ee.web.struts.action.FrameworkDispatchActionSupport;
import architecture.ee.web.util.ModelMap;
import architecture.ee.web.util.ParamUtils;
import architecture.ee.web.util.ServletUtils;
import architecture.ee.web.util.WebApplicatioinConstants;
import architecture.ee.web.view.json.JsonView;

public class DataServiceAction extends FrameworkDispatchActionSupport {
	
	
	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
				
		OutputFormat output = ServletUtils.getOutputFormat(request, response);
		
		String statement = ParamUtils.getParameter(request, "statement" );
		String parametersString = ParamUtils.getParameter(request, "parameters", null );
		
		SqlQueryClient client = getComponent("sqlQueryClient", SqlQueryClient.class);
		
		List<Map<String, Object>> list ;	
		if( StringUtils.isEmpty( parametersString)  )
		    list = client.list(statement);	
		else
			list = client.list(statement, StringUtils.splitPreserveAllTokens(parametersString, ","));

		ModelMap model = new ModelMap();
		model.put("items", list);
		request.setAttribute(WebApplicatioinConstants.MODEL_ATTRIBUTE, model);			
				
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
			item = client.uniqueResult(statement, StringUtils.splitPreserveAllTokens(parametersString, ","));

		ModelMap model = new ModelMap();
		model.put("item", item);
		request.setAttribute(WebApplicatioinConstants.MODEL_ATTRIBUTE, model);			
				
		if(output == OutputFormat.JSON ){
			JsonView view = new JsonView();		    
			view.setModelKey("item");
			view.render(model, request, response);
			return null;
		}		
		
		return (mapping.findForward(output.name().toLowerCase()));
	}	
}