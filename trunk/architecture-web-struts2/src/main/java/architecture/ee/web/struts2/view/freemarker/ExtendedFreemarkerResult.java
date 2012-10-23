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
package architecture.ee.web.struts2.view.freemarker;


import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.views.freemarker.FreemarkerResult;

import architecture.common.user.User;
import architecture.ee.util.OutputFormat;
import architecture.ee.web.struts2.action.FrameworkActionSupport;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class ExtendedFreemarkerResult extends FreemarkerResult  {

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
    	HttpServletRequest request = (HttpServletRequest)ac.get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse response = (HttpServletResponse)ac.get(ServletActionContext.HTTP_RESPONSE);
        ServletContext servletContext = (ServletContext)ac.get(ServletActionContext.SERVLET_CONTEXT);
		
        this.locale = ServletActionContext.getActionContext(request).getLocale();        
        
        Action action = (Action)ac.getActionInvocation().getAction();
        
        if( invocation.getAction() instanceof FrameworkActionSupport )
        {
        	User user = ((FrameworkActionSupport)action).getUser();
        	OutputFormat format = ((FrameworkActionSupport)action).getOutputFormat();
        	if( format == OutputFormat.XML ){
        		setContentType("text/xml");
        	}
        }
                
        ExtendedFreemarkerManager manager = (ExtendedFreemarkerManager) freemarkerManager;
        Configuration configuration = manager.getConfiguration(servletContext);        
        // Configuration config = manager.getThemeConfiguration(servletContext);
        Template template = configuration.getTemplate(locationArg);
        freemarker.template.SimpleHash model = manager.buildTemplateModel(invocation.getStack(), invocation.getAction(), servletContext, request, response, configuration.getObjectWrapper());
        
        if(preTemplateProcess(template, model))
        {
        	try {
                // Process the template
                Writer writer = null;
                boolean useOutputStream = false;
				try {
					writer = getWriter();
				} catch (IllegalStateException e) {
					useOutputStream = true;
				}
				
				if(useOutputStream)
                {
                    writer = new StringWriter();
                    template.process(model, writer);
                    OutputStream os = ServletActionContext.getResponse().getOutputStream();
                    os.write(writer.toString().getBytes());
                } else
                {
                    template.process(model, writer);
                }
				
            } finally {
                // Give subclasses a chance to hook into postprocessing
                postTemplateProcess(template, model);
            }
        }
		
	}

	@Override
	protected boolean preTemplateProcess(Template template, TemplateModel model) throws IOException {
		if(locale != null)
	        template.setLocale(locale);
	    if(timeZone != null)
	        template.setTimeZone(timeZone);
	    return super.preTemplateProcess(template, model);
	}	
	
}
