package architecture.ee.web.struts2.view.freemarker;

import java.io.IOException;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.views.freemarker.FreemarkerResult;

import architecture.ee.util.OutputFormat;
import architecture.ee.web.struts2.action.FrameworkActionSupport;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class ExtendedFreemarkerResult extends FreemarkerResult {
	
	private Locale locale;
	private TimeZone timeZone;
	private Log log = LogFactory.getLog(getClass());

	public ExtendedFreemarkerResult() {
		locale = null;
		timeZone = null;
	}

	@Override
	public void doExecute(String locationArg, ActionInvocation invocation) throws IOException, TemplateException {
		
		ActionContext ac = invocation.getInvocationContext();
    	HttpServletRequest req = (HttpServletRequest)ac.get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse res = (HttpServletResponse)ac.get(ServletActionContext.HTTP_RESPONSE);
        ServletContext servletContext = (ServletContext)ac.get(ServletActionContext.SERVLET_CONTEXT);
		
        this.locale = ServletActionContext.getActionContext(req).getLocale();        
        Action action = (Action)ac.getActionInvocation().getAction();
        
        if( action instanceof FrameworkActionSupport ){
        	String dataTypeString = ((FrameworkActionSupport)action).getDataType();
        	OutputFormat dataType = OutputFormat.stingToOutputFormat(dataTypeString);
        	if(dataType == OutputFormat.XML)
        		setContentType("text/xml");
        }
        
        if(log.isDebugEnabled()){
        	log.debug("view (location:" + location + ", action: " + action.getClass().getName() + ")");
        }
		super.doExecute(locationArg, invocation);
	}

	@Override
	protected boolean preTemplateProcess(Template template, TemplateModel model)
			throws IOException {
		if(locale != null)
	        template.setLocale(locale);
	    if(timeZone != null)
	        template.setTimeZone(timeZone);
	    return super.preTemplateProcess(template, model);
	}

}
