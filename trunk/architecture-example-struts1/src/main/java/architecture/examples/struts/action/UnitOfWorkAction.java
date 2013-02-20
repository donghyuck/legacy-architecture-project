package architecture.examples.struts.action;

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
import architecture.ee.web.struts.action.FrameworkActionSupport;
import architecture.ee.web.util.ParamUtils;
import architecture.ee.web.view.json.JsonView;

public class UnitOfWorkAction extends FrameworkActionSupport {

	private String [] stringToArray(String parametersString){
		return  StringUtils.splitPreserveAllTokens(parametersString, ",");
	}
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		OutputFormat output = getOutputFormat(request, response);
		
		String script = ParamUtils.getParameter(request, "script" );
		String action = ParamUtils.getParameter(request, "method" );
		String parametersString = ParamUtils.getParameter(request, "parameters", null );
		
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
	
}
