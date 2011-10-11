package architecture.ee.web.struts2.interceptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;


public class ExtendedMethodFilterInterceptor extends MethodFilterInterceptor {
	
	private static final Log log = LogFactory.getLog(ExtendedMethodFilterInterceptor.class);
	
	private boolean alwaysInvokePrepare;
    private static final String PREPARE_PREFIX = "prepare";
    private static final String ALT_PREPARE_PREFIX = "prepareDo";
    
    private static final String PREFIXES[] = {
    	PREPARE_PREFIX, ALT_PREPARE_PREFIX
    };
    
    
    public void setAlwaysInvokePrepare(String alwaysInvokePrepare)
    {
        this.alwaysInvokePrepare = Boolean.parseBoolean(alwaysInvokePrepare);
    }
    
	@Override
	protected String doIntercept(ActionInvocation invocation) throws Exception {
        Object action = invocation.getAction();
        if(action instanceof Preparable)
        {
            try
            {
            	ExtendedPrefixMethodInvocationUtil.invokePrefixMethod(invocation, PREFIXES);
            }
            catch(Exception e)
            {
                log.warn("an exception occured while trying to execute prefixed method", e);
            }
            if(alwaysInvokePrepare)
                ((Preparable)action).prepare();
        }
        return invocation.invoke();
	}

	
}
