package architecture.ee.web.struts2.interceptor;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.StrutsStatics;

import architecture.ee.web.struts2.annotation.SetReferer;
import architecture.ee.web.struts2.annotation.SetRefererNotInherited;
import architecture.ee.web.util.ServletUtils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

/**
 * 
 * @author  <a href="mailto:donghyuck.son@gmail.com">Donghyuck Son </a>
 *
 */
public class RefererInterceptor implements Interceptor {

	public static final String URL_REFERER_KEY = "url.referer";

	public void destroy() {

	}

	public void init() {

	}

	public String intercept(ActionInvocation invocation) throws Exception {
        ActionContext context = invocation.getInvocationContext();
        setReferer(invocation, context);
        return invocation.invoke();
	}
	
	public void setReferer(ActionInvocation invocation, ActionContext context)
    {
        Map session = context.getSession();
        if(session != null && setReferer(invocation))
            session.put(URL_REFERER_KEY, getPageURL(context));
    }

	
	private boolean setReferer(ActionInvocation invocation)
    {
        SetReferer setReferer = null;
        SetRefererNotInherited notInherited = null;
        Object action = invocation.getAction();
        String methodName = invocation.getProxy().getMethod();
        if(methodName == null)
            methodName = "execute";
        try
        {
            Method method = action.getClass().getMethod(methodName, new Class[0]);
            setReferer = (SetReferer)method.getAnnotation(SetReferer.class);
        }
        catch(NoSuchMethodException e)
        {
            try
            {
                methodName = (new StringBuilder()).append("do").append(methodName.substring(0, 1).toUpperCase()).append(methodName.substring(1)).toString();
                Method method = action.getClass().getMethod(methodName, new Class[0]);
                setReferer = (SetReferer)method.getAnnotation(SetReferer.class);
            }
            catch(NoSuchMethodException e1) { }
        }
        if(setReferer == null)
            setReferer = (SetReferer)invocation.getAction().getClass().getAnnotation(SetReferer.class);
        if(setReferer == null)
            notInherited = (SetRefererNotInherited)invocation.getAction().getClass().getAnnotation(SetRefererNotInherited.class);
        if(getPageURL(invocation.getInvocationContext()).contains("/404.jsp"))
            return false;
        else
            return setReferer == null && notInherited == null || setReferer != null && setReferer.value() || notInherited != null && notInherited.value();
    }
	
	public String getPageURL(ActionContext ctx)
    {
		
        HttpServletRequest request = (HttpServletRequest)ctx.get(StrutsStatics.HTTP_REQUEST);
        return getPageURL(request);
    }

    public String getPageURL(HttpServletRequest request)
    {
        StringBuilder page = new StringBuilder();
        page.append(ServletUtils.getServletPath(request));
        String queryString = request.getQueryString();
        if(queryString != null && !"".equals(queryString.trim()))
            page.append('?').append(queryString);
        return page.toString();
    }
	
}
