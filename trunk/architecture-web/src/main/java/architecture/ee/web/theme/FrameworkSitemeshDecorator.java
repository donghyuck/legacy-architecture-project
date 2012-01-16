package architecture.ee.web.theme;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import architecture.common.util.profiling.TimerStack;
import architecture.ee.util.ApplicationHelper;
import architecture.ee.web.struts2.action.FrameworkActionSupport;
import architecture.ee.web.struts2.annotation.Decorate;
import architecture.ee.web.struts2.view.freemarker.ExtendedFreemarkerManager;

import com.opensymphony.module.sitemesh.Decorator;
import com.opensymphony.module.sitemesh.Page;
import com.opensymphony.module.sitemesh.RequestConstants;
import com.opensymphony.sitemesh.Content;
import com.opensymphony.sitemesh.compatability.Content2HTMLPage;
import com.opensymphony.sitemesh.webapp.SiteMeshWebAppContext;
import com.opensymphony.sitemesh.webapp.decorator.BaseWebAppDecorator;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.util.ValueStack;

import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FrameworkSitemeshDecorator extends BaseWebAppDecorator implements RequestConstants {
	
    private final Decorator decorator;
    
    private static final Log log = LogFactory.getLog(FrameworkSitemeshDecorator.class);
    
    FrameworkSitemeshDecorator(Decorator decorator)
    {
        this.decorator = decorator;
    }

    protected void render(Content content, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext, SiteMeshWebAppContext webAppContext)
            throws IOException, ServletException {

    	String profileName = (new StringBuilder()).append("SiteMesh: applyDecorator: ").append(decorator.getName()).append(" (").append(decorator.getPage()).append(")").toString();
    	TimerStack.push(profileName);    	
    	try {
/*			request.setAttribute(PAGE, new Content2HTMLPage(content, request));
			log.debug( "parameters:" + request.getParameterMap() );
			
			// see if the URI path (webapp) is set
			if (decorator.getURIPath() != null) {
			    // in a security conscious environment, the servlet container
			    // may return null for a given URL
			    if (servletContext.getContext(decorator.getURIPath()) != null) {
			        servletContext = servletContext.getContext(decorator.getURIPath());
			    }
			}
			// get the dispatcher for the decorator
			RequestDispatcher dispatcher = servletContext.getRequestDispatcher(decorator.getPage());
			dispatcher.include(request, response);
			request.removeAttribute(PAGE);*/
			
			Page page = new Content2HTMLPage(content, request);
			applyDecorator(page, decorator, request, response, servletContext);
			
			
		}finally{
			TimerStack.pop(profileName);
		}
        
    }
    
    public void applyDecorator(Page page, Decorator decorator, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext)
    throws ServletException, IOException{
        
    	HttpServletRequest originalRequest = null;
        HttpServletResponse originalResponse = null;     
        try {
			originalRequest = ServletActionContext.getRequest();
			originalResponse = ServletActionContext.getResponse();
			ServletActionContext.setRequest(request);
			ServletActionContext.setResponse(response);			
			
			applyDecoratorUsingFreemarker(request, page, response, servletContext, decorator);
			
		} finally {
			ServletActionContext.setRequest(originalRequest);
			ServletActionContext.setResponse(originalResponse);
		}
    }
    
	private void applyDecoratorUsingFreemarker(HttpServletRequest request,
			Page page, HttpServletResponse response,
			ServletContext servletContext, Decorator decorator)
			throws ServletException, IOException {
		
		request.setAttribute(PAGE, page);

		ActionContext ac = ServletActionContext.getActionContext(request);
		if (!shouldDecorate(ac)) {
			writeOriginal(request, response, page);
		}
        		
		if( ac != null ){
			
			loadDefaultDecorator(request, page, response, servletContext, decorator);
			
		}else{
	    	// see if the URI path (webapp) is set
			if (decorator.getURIPath() != null) {
			    // in a security conscious environment, the servlet container
			    // may return null for a given URL
			    if (servletContext.getContext(decorator.getURIPath()) != null) {
			        servletContext = servletContext.getContext(decorator.getURIPath());
			    }
			}
			// get the dispatcher for the decorator
			RequestDispatcher dispatcher = servletContext.getRequestDispatcher(decorator.getPage());
			dispatcher.include(request, response);
						
		}
		request.removeAttribute(PAGE);
	}

	private void loadDefaultDecorator(HttpServletRequest request, Page page, 
			HttpServletResponse response, ServletContext servletContext, Decorator decorator)
			throws IOException {
		try {
			ActionContext ac = ServletActionContext.getActionContext(request);
			ActionInvocation ai = ac.getActionInvocation();
			Object action = ai.getAction();
			ValueStack stack = ServletActionContext.getValueStack(request);
			Configuration config = ApplicationHelper.getComponent(ExtendedFreemarkerManager.class).getConfiguration(servletContext);
			Template template = config.getTemplate(decorator.getPage());
			
			SimpleHash model = ApplicationHelper.getComponent(ExtendedFreemarkerManager.class).buildTemplateModel(stack, action, servletContext, request, response, config.getObjectWrapper());
			model.put("page", page);
			
			java.util.Locale locale = ServletActionContext.getActionContext(request).getLocale();
			template.setLocale(locale);
			
			if( action instanceof FrameworkActionSupport ){
				// 사용자 정의 타임존 설정
			}
			
			template.process(model, response.getWriter());

		} catch (TemplateException e) {
			 log.error((new StringBuilder()).append("Could not process decorator '").append(decorator.getPage()).toString(), e);
        }
		
	}
    
    protected void writeOriginal(HttpServletRequest request, HttpServletResponse response, Page page)
    throws ServletException, IOException
	{
        page.writePage(response.getWriter());
        response.getWriter().flush();
	}
    
    protected boolean shouldDecorate(ActionContext ac){
    	boolean shouldDecorate = true;
        if(ac == null)
            return shouldDecorate;        
        ActionInvocation ai = ac.getActionInvocation();
        if(ai != null && ai.getAction().getClass().isAnnotationPresent(Decorate.class))
            shouldDecorate = ((Decorate)ai.getAction().getClass().getAnnotation(Decorate.class)).value();
        return shouldDecorate;
    }
}
