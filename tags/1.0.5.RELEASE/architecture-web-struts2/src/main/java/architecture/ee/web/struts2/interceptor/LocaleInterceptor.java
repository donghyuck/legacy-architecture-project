package architecture.ee.web.struts2.interceptor;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.StrutsStatics;

import architecture.common.user.User;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;
import architecture.ee.web.util.LocaleUtils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class LocaleInterceptor implements Interceptor {
	
	private static final String LOCALE_ATTRIBUTE = "user.locale";
	private static final Log log = LogFactory.getLog(LocaleInterceptor.class);
	
    public LocaleInterceptor()
    {
    }

    public void destroy()
    {
    }

    public void init()
    {
    }

    public String intercept(ActionInvocation invocation)
        throws Exception
    {
        ActionContext ac = invocation.getInvocationContext();
        
        HttpServletRequest req = (HttpServletRequest)ac.get(StrutsStatics.HTTP_REQUEST);
        Locale locale;
        if(req.getAttribute(LOCALE_ATTRIBUTE) != null)
        {
            locale = (Locale)req.getAttribute("extended.locale");
        } else
        {
            User user = null;
            if(invocation.getAction() instanceof FrameworkActionSupport)
            {
            	FrameworkActionSupport jas = (FrameworkActionSupport)invocation.getAction();
                user = jas.getUser();
            }            
            
            locale = LocaleUtils.getUserLocale(req, user, false);
            
            req.setAttribute(LOCALE_ATTRIBUTE, locale);
        }
        if(locale != null)
        {
            log.debug((new StringBuilder()).append("Setting locale to '").append(locale).append("'").toString());
            ac.setLocale(locale);
        }
        log.debug("Leaving LocaleInterceptor");
        return invocation.invoke();
    }

    
}
