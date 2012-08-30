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
import architecture.ee.web.struts.action.FrameworkDispatchActionSupport;
import architecture.ee.web.util.ModelMap;
import architecture.ee.web.util.ParamUtils;
import architecture.ee.web.util.WebApplicatioinConstants;

public class SqlQueryClientAction extends FrameworkDispatchActionSupport {
	
	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String view = ParamUtils.getParameter(request, "output", "xml" );
		String statement = ParamUtils.getParameter(request, "statement" );
		String parametersString = ParamUtils.getParameter(request, "parameters", null );
		
		SqlQueryClient client = getComponent("sqlQueryClient", SqlQueryClient.class);
		
		List<Map<String, Object>> list ;	
		if( StringUtils.isEmpty( parametersString)  )
		    list = client.list(statement);	
		else
			list = client.list(statement, StringUtils.splitPreserveAllTokens(parametersString, ","));

		ModelMap model = new ModelMap();
		model.put("list", list);
		request.setAttribute(WebApplicatioinConstants.MODEL_ATTRIBUTE, model);			
		return (mapping.findForward(view));
	}
	
}