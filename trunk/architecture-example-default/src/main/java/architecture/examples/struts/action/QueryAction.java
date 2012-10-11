package architecture.examples.struts.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import architecture.ee.services.SqlQueryClient;
import architecture.ee.util.OutputFormat;
import architecture.ee.web.struts.action.FrameworkDispatchActionSupport;
import architecture.ee.web.util.ParamUtils;
import architecture.ee.web.util.ServletUtils;
import architecture.ee.web.view.json.JsonView;

public class QueryAction extends FrameworkDispatchActionSupport {
	

	private String [] stringToArray(String parametersString){
		return  StringUtils.splitPreserveAllTokens(parametersString, ",");
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
		
		if(output == OutputFormat.JSON ){
			JsonView view = new JsonView();		    
			view.setModelKey("items");
			view.render(model, request, response);
			return null;
		}else{
			saveModelMap(request, model);
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
				
		if( output == OutputFormat.JSON ){
			JsonView view = new JsonView();		    
			view.setModelKey("item");
			view.render(model, request, response);
			return null;
		}else{
			saveModelMap(request, model);
		}	
		
		return (mapping.findForward(output.name().toLowerCase()));
	}	
	
	public ActionForward listWithError(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		OutputFormat output = getOutputFormat(request, response);		
		try {
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
			
			if(output == OutputFormat.JSON ){
				JsonView view = new JsonView();		    
				view.setModelKey("items");
				view.render(model, request, response);
				return null;
			}else{
				saveModelMap(request, model);
			}
			
		} catch (Exception e) {
			// 오류처리를 아래와 같이 처리하지 않는 경우에 자동으로 처리한다.
			
			ActionMessages errors = new ActionMessages();
			errors.add( Globals.EXCEPTION_KEY, new ActionMessage(e.getMessage()));
			saveErrors(request, errors);			
			return mapping.findForward("error");
		}
		
		return (mapping.findForward(output.name().toLowerCase()));
	}
	
	
	
	public ActionForward mapWithError(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {		
		OutputFormat output = ServletUtils.getOutputFormat(request, response);
		
		String statement = ParamUtils.getParameter(request, "statement" );
		String parametersString = ParamUtils.getParameter(request, "parameters", null );
		
		try {
			
			SqlQueryClient client = getComponent("sqlQueryClient", SqlQueryClient.class);
			
			Map<String, Object> item ;	
			if( StringUtils.isEmpty( parametersString)  )
				item = client.uniqueResult(statement);
			else
				item = client.uniqueResult(statement, stringToArray(parametersString));

			Map model = getModelMap(request, response);
			model.put("item", item);
					
			if( output == OutputFormat.JSON ){
				JsonView view = new JsonView();		    
				view.setModelKey("item");
				view.render(model, request, response);
				return null;
			}else{
				saveModelMap(request, model);
			}	
			
			return (mapping.findForward(output.name().toLowerCase()));
			
		} catch (Exception e) {
			log.error(e);
			
			ActionMessages errors = new ActionMessages();
			errors.add( Globals.EXCEPTION_KEY, new ActionMessage(e.getMessage()));
			saveErrors(request, errors);
			
			return mapping.findForward("error");
		}
	}
}
