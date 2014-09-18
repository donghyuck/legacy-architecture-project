/*
 * Copyright 2012 Donghyuck, Son
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package architecture.ee.web.struts2.view.servlet;
import java.io.FileNotFoundException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.StrutsResultSupport;
import org.apache.struts2.views.freemarker.FreemarkerManager;

import architecture.ee.util.OutputFormat;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;
import architecture.ee.web.struts2.view.freemarker.ExtendedFreemarkerManager;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class ServletDispatcherResult extends StrutsResultSupport  {
	
	private static final Log log = LogFactory.getLog(ServletDispatcherResult.class);
	
	org.apache.struts2.dispatcher.ServletDispatcherResult proxy;	
	
	protected FreemarkerManager freemarkerManager;
	
	@Override
	protected void doExecute(String finalLocation, ActionInvocation invocation)
			throws Exception {
		
		ActionContext ac = invocation.getInvocationContext();
    	HttpServletRequest req = (HttpServletRequest)ac.get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse res = (HttpServletResponse)ac.get(ServletActionContext.HTTP_RESPONSE);
        ServletContext servletContext = (ServletContext)ac.get(ServletActionContext.SERVLET_CONTEXT);
        try
        {
        	// JSP 인 경우
        	if( finalLocation.endsWith(".jsp")){
        		String templateName = (new StringBuilder()).append(finalLocation.substring(0, finalLocation.length() - 4)).append(".ftl").toString();
        		log.debug((new StringBuilder()).append("Looking for template override with name ").append(templateName).toString());
                try
                {
                	//ProfilerFactory.getProfiler().start("ThemeServletDispatcherResult");
                    try
                    {
                    	ExtendedFreemarkerManager manager = (ExtendedFreemarkerManager) freemarkerManager;
                        Configuration config = manager.getConfiguration(servletContext); //.getThemeConfiguration(servletContext);
                        Template template = config.getTemplate(templateName);
                        freemarker.template.SimpleHash model = manager.buildTemplateModel(invocation.getStack(), invocation.getAction(), servletContext, req, res, config.getObjectWrapper());
                        java.util.Locale locale = ServletActionContext.getActionContext(req).getLocale();
                        template.setLocale(locale);
                        Action action = (Action)ac.getActionInvocation().getAction();
                        if( action instanceof FrameworkActionSupport)
                        {
                        	architecture.common.user.User user = ((FrameworkActionSupport)action).getUser();
                            //template.setTimeZone(LocaleUtils.getTimeZone(req, user));                        	
                        	OutputFormat dataType = ((FrameworkActionSupport)action).getOutputFormat();
                        	
                        	if( dataType == OutputFormat.XML)
                        		res.setContentType("text/xml");                        	
                        }
                        
                        template.process(model, res.getWriter());
                    }
                    catch(FileNotFoundException fnfe)
                    {
                        log.debug((new StringBuilder()).append("Template override: '").append(templateName).append("' does not exist.").toString());
                        throw fnfe;
                    }
                    catch(Exception e)
                    {
                        log.debug((new StringBuilder()).append("Could not load template override: ").append(templateName).toString(), e);
                        throw e;
                    }
                }
                finally
                {
                	//ProfilerFactory.getProfiler().stop("ThemeServletDispatcherResult");
                }
                return;
        	}
        }catch(Exception e){
            log.debug("Freemarker override not loaded. Continuing to jsp result.");	
        }        
        proxy.doExecute(finalLocation, invocation);
	}
		
    public void setLocation(String location)
    {
        super.setLocation(location);
        proxy.setLocation(location);
    }
    
    public void setParse(boolean parse)
    {
        super.setParse(parse);
        proxy.setParse(parse);
    }
}
