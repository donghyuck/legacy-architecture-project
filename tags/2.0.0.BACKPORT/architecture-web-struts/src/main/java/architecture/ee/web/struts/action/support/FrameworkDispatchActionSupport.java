package architecture.ee.web.struts.action.support;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.common.exception.Codeable;
import architecture.common.exception.ComponentNotFoundException;
import architecture.common.user.User;
import architecture.common.user.authentication.AuthToken;
import architecture.ee.util.OutputFormat;
import architecture.ee.web.util.ServletUtils;
import architecture.ee.web.util.WebApplicatioinConstants;
import architecture.ee.web.util.WebApplicationHelper;
import architecture.user.util.SecurityHelper;

public abstract class FrameworkDispatchActionSupport extends
		org.springframework.web.struts.DispatchActionSupport {

	protected Log log;

	public FrameworkDispatchActionSupport() {
		log = LogFactory.getLog(getClass());
	}

	protected final <T> T getComponent(Class<T> requiredType)
			throws ComponentNotFoundException {
		return WebApplicationHelper.getComponent(requiredType);
	}

	protected final <T> T getComponent(String requiredName, Class<T> requiredType) throws ComponentNotFoundException {
		return WebApplicationHelper.getComponent(requiredName, requiredType);
	}

	protected final void autowireComponent(Object obj) {
		WebApplicationHelper.autowireComponent(obj);
	}

	protected boolean isCodeable (Throwable exception){
		if( exception instanceof Codeable )
			return true;
		else 
			return false;
	}	
	
	protected User getUser (){
		return SecurityHelper.getUser();
	} 

	protected AuthToken getAuthToken (){
		return SecurityHelper.getAuthToken();
	} 
	
	protected OutputFormat getOutputFormat(HttpServletRequest request, HttpServletResponse response){
		return ServletUtils.getOutputFormat(request, response);
	}
	
	protected Map getModelMap(HttpServletRequest request, HttpServletResponse response){
         return ServletUtils.getModelMap(request, response);		
	}
	
    protected void saveModelMap(HttpServletRequest request,  Map modelMap) {
        // Remove any error messages attribute if none are required
        if ((modelMap == null) || modelMap.isEmpty()) {
            request.removeAttribute(WebApplicatioinConstants.MODEL_ATTRIBUTE);
            return;
        }
        // Save the error messages we need
        request.setAttribute(WebApplicatioinConstants.MODEL_ATTRIBUTE, modelMap);
    }
	
}
