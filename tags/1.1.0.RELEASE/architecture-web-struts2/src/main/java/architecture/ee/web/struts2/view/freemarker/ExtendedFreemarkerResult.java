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
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.views.freemarker.FreemarkerResult;

import architecture.common.exception.RuntimeError;

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
	    super();
	    locale = null;
		timeZone = null;
	}
	
	@Override
    protected Configuration getConfiguration() throws TemplateException {
        if( freemarkerManager instanceof  ExtendedFreemarkerManager ){            
            ((ExtendedFreemarkerManager) freemarkerManager).getWrapper();
            return ((ExtendedFreemarkerManager) freemarkerManager).getConfiguration(ServletActionContext.getServletContext());      
        }        
        return freemarkerManager.getConfiguration(ServletActionContext.getServletContext());
    }
        
	@Override
    public void doExecute(String locationArg, ActionInvocation invocation) throws IOException, TemplateException {
	    try {
           super.doExecute(locationArg, invocation);
        } catch (Throwable e) {
           throw new  RuntimeError(e);
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
