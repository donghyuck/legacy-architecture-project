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

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.views.freemarker.FreemarkerManager;
import org.apache.struts2.views.freemarker.ScopesHashModel;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import architecture.common.exception.ComponentNotFoundException;
import architecture.ee.util.ApplicationHelper;
import architecture.ee.web.util.WebApplicatioinConstants;

import com.opensymphony.xwork2.util.ValueStack;

import freemarker.cache.StrongCacheStorage;
import freemarker.core.Environment;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;

/**
 * 스트럿츠 2 지원을 위한 Freemarker 클래스
 * 
 * @author  <a href="mailto:donghyuck.son@gmail.com">Donghyuck Son </a>
 *
 */
public class ExtendedFreemarkerManager extends FreemarkerManager {

	private static final Log log = LogFactory.getLog(ExtendedFreemarkerManager.class);
	
	private static final int UPDATE_DELAY = 60;
	
	private FreeMarkerConfig freeMarkerConfig ;
	
	
	
	public static final TemplateExceptionHandler LOG_DEBUG_HANDLER = new TemplateExceptionHandler() {		
	        public void handleTemplateException(TemplateException te, Environment env, Writer out)
	        {
	        	ExtendedFreemarkerManager.log.warn("Freemarker template exception. ", te);
	        }
	   };
	   
	   
	public ExtendedFreemarkerManager() {    	
	}

	public void setFreeMarkerConfig(FreeMarkerConfig freeMarkerConfig) {
		this.freeMarkerConfig = freeMarkerConfig;
	}

	@Override
	protected Configuration createConfiguration(ServletContext servletContext)
			throws TemplateException {		
		if(ApplicationHelper.isReady())
		try {
			//FreeMarkerConfig freeMarkerConfig = ApplicationHelper.getComponent(FreeMarkerConfig.class);
			Configuration configuration = freeMarkerConfig.getConfiguration();
			configureDefaultConfiguration(configuration, servletContext );

			return configuration;
		} catch (ComponentNotFoundException e) {			
		}
		return super.createConfiguration(servletContext);
	}
	
    protected void configureDefaultConfiguration(Configuration configuration, ServletContext servletContext)
    {     
    	configuration.setCacheStorage(new StrongCacheStorage());        
    	configuration.setTemplateExceptionHandler(getTemplateExceptionHandler());     
    	
        // configuration.addAutoImport("framework", "/template/common/include/framework-macros.ftl");        
        // configuration.setLocalizedLookup(false);      
    	
        configuration.setObjectWrapper(wrapper);
    }
    
    @Override
	protected void loadSettings(ServletContext servletContext) {
		super.loadSettings(servletContext);
		HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        
        if( request == null || response == null )
        {
            return;
        } else
        {
        	config.setLocale(ApplicationHelper.getLocale());
        	config.setTimeZone(ApplicationHelper.getTimeZone());
            return;
        }	
	}

	@Override
	protected void populateContext(ScopesHashModel model, ValueStack stack,
			Object action, HttpServletRequest request,
			HttpServletResponse response) {

		super.populateContext(model, stack, action, request, response);
		HashMap<String, Object> map = new HashMap<String, Object>();		
		populateStatics(map);		
		model.putAll(map);
	}

	public void populateStatics(Map<String, Object> model){
		
		BeansWrapper b = (BeansWrapper) wrapper;
		
		try {
			TemplateHashModel enumModels = b.getEnumModels();
			try {

			} catch (Exception e) {
				log.error(e);
			}
			model.put("enums", b.getEnumModels());
		} catch (UnsupportedOperationException e) {
		}

		TemplateHashModel staticModels = b.getStaticModels();
		try {
			model.put("LocaleUtils",             staticModels.get("architecture.ee.web.util.LocaleUtils"));
			model.put("SecurityHelper",         staticModels.get("architecture.security.util.SecurityHelper"));
			model.put("ApplicationHelper",     staticModels.get("architecture.ee.util.ApplicationHelper"));							
		} catch (TemplateModelException e) {
			log.error(e);
		}		
		model.put("statics", BeansWrapper.getDefaultInstance().getStaticModels());
	}

	private TemplateExceptionHandler getTemplateExceptionHandler()
    {
    	boolean logErrorDefault = false;
        boolean logError = ApplicationHelper.getApplicationBooleanProperty(WebApplicatioinConstants.VIEW_FREEMARKER_DEBUG, logErrorDefault);
        TemplateExceptionHandler handler;
        
        if(logError)
            handler = LOG_DEBUG_HANDLER;
        else
            handler = TemplateExceptionHandler.HTML_DEBUG_HANDLER;
        return handler;
    }
	
	
    
}
