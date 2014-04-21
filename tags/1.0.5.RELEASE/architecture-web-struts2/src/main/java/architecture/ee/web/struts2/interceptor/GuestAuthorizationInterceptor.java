package architecture.ee.web.struts2.interceptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;

import architecture.common.user.User;
import architecture.ee.web.struts2.annotation.AlwaysAllowAnonymous;
import architecture.ee.web.struts2.annotation.AlwaysDisallowAnonymous;
import architecture.user.security.authentication.AuthenticationProvider;
import architecture.user.security.authentication.AuthenticationProviderFactory;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

@SuppressWarnings("serial")
public class GuestAuthorizationInterceptor implements Interceptor {

	private AuthenticationProvider authProvider;
	
	private Log log = LogFactory.getLog(getClass());
	
	public void destroy() {
	}

	public void init() {
	}

	public void setAuthenticationProvider(AuthenticationProvider authProvider) {
		this.authProvider = authProvider;
	}

	public String intercept(ActionInvocation invocation) throws Exception {
		
		Authentication auth ;
        try
        {
        	auth = AuthenticationProviderFactory.getSecurityContextAuthenticationProvider().getAuthentication();
        }
        catch(Exception ex)
        {
            String message = "Failed to extract authentication. Unable to check authoirzation.";
            log.info("Failed to extract authentication. Unable to check authoirzation.", ex);
            throw new SecurityException("Failed to extract jive authentication. Unable to check authoirzation.");
        }        
        
        invocation.getInvocationContext().put("authentication", auth);
        
        User user = AuthenticationProviderFactory.getSecurityContextAuthenticationProvider().getUser();
        
        if(user.isAnonymous()){        	
        	Action action = (Action) invocation.getAction();
        	String method = invocation.getProxy().getMethod();
        	if( !actionAllowsGuest(action, method) || actionDisallowsGuest(action, method)){
        		
        		return "unauthenticated";
        	}        	
        }
		return invocation.invoke();
	}

	protected boolean actionAllowsGuest(Action action, String method)
    {
        return checkAnnotation(AlwaysAllowAnonymous.class, action, method);
    }

    protected boolean actionDisallowsGuest(Action action, String method)
    {
        return checkAnnotation(AlwaysDisallowAnonymous.class, action, method);
    }

    
	protected boolean checkAnnotation(Class annotation, Action action, String method)
    {
        if(action.getClass().isAnnotationPresent(annotation))
            return true;
        try
        {
            if(action.getClass().getMethod(method, new Class[0]).isAnnotationPresent(annotation))
                return true;
        }
        catch(NoSuchMethodException e) { }
        return false;
    }

	
}
