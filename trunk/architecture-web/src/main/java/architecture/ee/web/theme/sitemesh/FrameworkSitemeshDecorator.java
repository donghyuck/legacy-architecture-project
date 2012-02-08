package architecture.ee.web.theme.sitemesh;

import java.io.IOException;
import java.io.PrintWriter;

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
    		Page page = new Content2HTMLPage(content, request);				
    		applyDecorator(page, decorator, request, response, servletContext, webAppContext);
		}finally{
			TimerStack.pop(profileName);
		}        
    }
    
    public void applyDecorator(Page page, Decorator decorator, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext, SiteMeshWebAppContext webAppContext)
    throws ServletException, IOException{
        
    	HttpServletRequest originalRequest = null;
        HttpServletResponse originalResponse = null;     
        try {
			originalRequest = ServletActionContext.getRequest();
			originalResponse = ServletActionContext.getResponse();
			ServletActionContext.setRequest(request);
			ServletActionContext.setResponse(response);
			applyDecoratorUsingFreemarker(request, page, response, servletContext, webAppContext, decorator);		
        } finally {
			ServletActionContext.setRequest(originalRequest);
			ServletActionContext.setResponse(originalResponse);
		}
    }
    
	private void applyDecoratorUsingFreemarker(
			HttpServletRequest request,
			Page page, 
			HttpServletResponse response,
			ServletContext servletContext, 
			SiteMeshWebAppContext webAppContext,
			Decorator decorator)
			throws ServletException, IOException {
		
		request.setAttribute(PAGE, page);		
		ActionContext ac = ServletActionContext.getActionContext(request);		
        
		if (!shouldDecorate(ac)) {
			writeOriginal(request, response, webAppContext, page);
		}else{
			if( decorator instanceof ThemeDecorator ){
				ThemeDecorator td = (ThemeDecorator)decorator;				
				try {
					ValueStack stack = ServletActionContext.getValueStack(request);
					Object action = ac.getActionInvocation().getAction();
					ExtendedFreemarkerManager freemarkerManager = ApplicationHelper.getComponent(ExtendedFreemarkerManager.class);
					Configuration config = freemarkerManager.getThemeConfiguration(servletContext);					
					Template template = config.getTemplate(td.getPage());					
					SimpleHash model = freemarkerManager.buildTemplateModel(stack, action, servletContext, request, response, config.getObjectWrapper());				
					model.put("page", page);							
					java.util.Locale locale = ac.getLocale();
					template.setLocale(locale);
					if(action instanceof FrameworkActionSupport){
						// setting user timezone                	
					}
					template.process(model, response.getWriter());
				} catch (Exception e) {
					log.error((new StringBuilder()).append("Could not process decorator '").append(td.getPage()).toString(), e);
				}
			}else if(decorator.getPage().endsWith(".jsp")){
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
			}else{
				loadDefaultDecorator(request, page, response, servletContext, decorator);
			}
		}
		request.removeAttribute(PAGE);
	}

	private void loadDefaultDecorator(HttpServletRequest request, Page page, 
			HttpServletResponse response, ServletContext servletContext, Decorator decorator)
			throws IOException {
		try {
			
			ActionContext ac = ServletActionContext.getActionContext(request);			
			ValueStack stack = ServletActionContext.getValueStack(request);
			if( ac != null){
				Object action = ac.getActionInvocation().getAction();
				ExtendedFreemarkerManager freemarkerManager = ApplicationHelper.getComponent(ExtendedFreemarkerManager.class);
				Configuration config = freemarkerManager.getConfiguration(servletContext);
				Template template = config.getTemplate(decorator.getPage());
				SimpleHash model = freemarkerManager.buildTemplateModel(stack, action, servletContext, request, response, config.getObjectWrapper());
				model.put("page", page);			
				java.util.Locale locale = ServletActionContext.getActionContext(request).getLocale();
				template.setLocale(locale);
				
				if( action instanceof FrameworkActionSupport ){
					// 사용자 정의 타임존 설정
				}				
				template.process(model, response.getWriter());
			}else{
				 log.error("ActionContext is null, cannot apply decorator.");
			}
			
		} catch (TemplateException e) {
			 log.error((new StringBuilder()).append("Could not process decorator '").append(decorator.getPage()).toString(), e);
        }		
	}
    
    protected void writeOriginal(HttpServletRequest request, HttpServletResponse response, SiteMeshWebAppContext webAppContext, Page page)
    throws ServletException, IOException
	{    	    	
    	response.setContentLength(page.getContentLength());    	
    	if(webAppContext.isUsingStream()){
            PrintWriter writer = new PrintWriter(response.getOutputStream());
            page.writePage(writer);
            writer.flush();
            response.getOutputStream().flush();
    	}else{
            page.writePage(response.getWriter());
            response.getWriter().flush();     
    	}   
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
