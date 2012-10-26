package architecture.ee.web.struts.action.common;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import architecture.common.exception.CodeableException;
import architecture.common.user.UserManager;
import architecture.common.user.UserTemplate;
import architecture.ee.exception.ApplicationException;
import architecture.ee.util.OutputFormat;
import architecture.ee.web.struts.action.support.FrameworkActionSupport;
import architecture.ee.web.util.ParamUtils;
import architecture.ee.web.view.json.JsonView;

public class CreateUserAction  extends FrameworkActionSupport {

	@Override
	public ActionForward execute(
			ActionMapping mapping, 
			ActionForm form,
			HttpServletRequest request, 
			HttpServletResponse response)
			throws Exception {

		OutputFormat output = getOutputFormat(request, response);
		
		// Token 
		if ( ! isTokenValid(request, true) ){			
			saveToken(request);			
			if( output == OutputFormat.HTML )
			    return mapping.findForward("input");
			else 
				throw CodeableException.newException(ApplicationException.class, 5501, null) ;
		}
		
		UserManager userManager = getComponent(UserManager.class);		
		String username = ParamUtils.getParameter(request, "username");
		String name = ParamUtils.getParameter(request, "name");
		String email = ParamUtils.getParameter(request, "email");
		String password = ParamUtils.getParameter(request, "password");
		boolean emailVisible = ParamUtils.getBooleanParameter(request, "emailVisible");
		boolean nameVisible = ParamUtils.getBooleanParameter(request, "nameVisible");
		
		UserTemplate ut = new UserTemplate(username);
		ut.setPassword(password);
		ut.setEmail(email);
		ut.setName(name);
		ut.setNameVisible(nameVisible);
		ut.setEmailVisible(emailVisible);
		userManager.createApplicationUser(ut);
		
		// Token 		
		Map model = getModelMap(request, response);
		Map<String, String> item = new java.util.HashMap<String, String>() ; 
		item.put("success", "true");		
		model.put("item", item);		
		
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