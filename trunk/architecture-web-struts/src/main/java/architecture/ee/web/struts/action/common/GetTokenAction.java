package architecture.ee.web.struts.action.common;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import architecture.ee.util.OutputFormat;
import architecture.ee.web.struts.action.support.FrameworkActionSupport;
import architecture.ee.web.util.ParamUtils;
import architecture.ee.web.view.json.JsonView;

public class GetTokenAction extends FrameworkActionSupport  {
	@Override
	public ActionForward execute(
			ActionMapping mapping, 
			ActionForm form,
			HttpServletRequest request, 
			HttpServletResponse response)
			throws Exception {
		
		OutputFormat output = getOutputFormat(request, response);		
	
		boolean doCreate = ParamUtils.getBooleanParameter(request, "create");
		if( doCreate )
		    saveToken(request);
		
		
		Map model = getModelMap(request, response);
		Map<String, String> item = new java.util.HashMap<String, String>() ; 
		
		String token = (String)request.getSession().getAttribute( org.apache.struts.Globals.TRANSACTION_TOKEN_KEY );
		
		item.put("success", "true");		
		item.put("token", token );		
		model.put("item", item );		
		
		saveModelMap(request, model);		
		
		if(output == OutputFormat.JSON ){
			JsonView view = new JsonView();		    
			view.setModelKey("item");			
			view.render(model, request, response);
			return null;
		}
		
		return (mapping.findForward(output.name().toLowerCase()));
		
	}
}
