package architecture.ee.web.struts2.util;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ValidationAware;

public class ActionUtils {
    
	public static boolean isMultiPart(HttpServletRequest request)
    {
        String contentType = getContentType(request);
        return contentType != null && contentType.startsWith("multipart/form-data");
    }
    
    public static String getContentType(HttpServletRequest request)
    {
        String contentType = request.getContentType();
        if(contentType == null)
            contentType = request.getHeader("Content-Type");
        return contentType;
    }
    
    public static Action getAction()
    {
        if(ActionContext.getContext().getValueStack().peek() instanceof Action)
            return (Action)ActionContext.getContext().getValueStack().peek();
        ActionInvocation ai = ActionContext.getContext().getActionInvocation();
        if(ai != null)
            return (Action)ai.getAction();
        else
            return null;
    }
    
    public static Throwable getException(){
    	Object e = ActionContext.getContext().getValueStack().findValue("exception");
    	if( e != null )
    		return (Throwable)e;
    	else 
    		return null;
    	
    }
    
    public static Collection<String> getActionMessages()
    {
        Action a = getAction();
        if(a != null && (a instanceof ValidationAware))
            return ((ValidationAware)a).getActionMessages();
        else
            return null;
    }
}
