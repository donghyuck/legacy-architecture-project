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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.views.freemarker.FreemarkerManager;
import org.apache.struts2.views.freemarker.ScopesHashModel;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.view.AbstractTemplateView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import architecture.common.exception.ComponentNotFoundException;
import architecture.ee.util.ApplicationHelper;
import architecture.ee.web.util.WebApplicatioinConstants;
import architecture.ee.web.view.freemarker.FreeMarkerHelper;

import com.opensymphony.xwork2.util.ValueStack;

import freemarker.cache.StrongCacheStorage;
import freemarker.cache.TemplateLoader;
import freemarker.core.Environment;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * 스트럿츠 2 지원을 위한 Freemarker 클래스
 * 
 * @author  <a href="mailto:donghyuck.son@gmail.com">Donghyuck Son </a>
 *
 */
public class ExtendedFreemarkerManager extends FreemarkerManager {

	private boolean exposeSpringMacroHelpers = false;
    private List tagModels;
    
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
	    tagModels = Collections.emptyList();
	}

	public void setFreeMarkerConfig(FreeMarkerConfig freeMarkerConfig) {
		this.freeMarkerConfig = freeMarkerConfig;
	}

	@Override
	protected Configuration createConfiguration(ServletContext servletContext)
			throws TemplateException {		
		if(ApplicationHelper.isReady())
		try {
			Configuration configuration = freeMarkerConfig.getConfiguration();			
			configureDefaultConfiguration(configuration, servletContext );
			return configuration;
		} catch (ComponentNotFoundException e) {			
		}
		return super.createConfiguration(servletContext);
	}
	
	protected void configureDefaultConfiguration(Configuration configuration, 	ServletContext servletContext) {
		configuration.setCacheStorage(new StrongCacheStorage());
		configuration.setTemplateExceptionHandler(getTemplateExceptionHandler());
		// configuration.addAutoImport("framework",
		// "/template/common/include/framework-macros.ftl");
		// configuration.setLocalizedLookup(false);
		if (configuration.getObjectWrapper() == null) {
			configuration.setObjectWrapper(createObjectWrapper(servletContext));
		}
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
    
    public void setTagModels(List tagModels)
    {
        this.tagModels = tagModels;
    }
    
	    

	/**
	 * @return exposeSpringMacroHelpers
	 */
	public boolean isExposeSpringMacroHelpers() {
		return exposeSpringMacroHelpers;
	}

	/**
	 * @param exposeSpringMacroHelpers 설정할 exposeSpringMacroHelpers
	 */
	public void setExposeSpringMacroHelpers(boolean exposeSpringMacroHelpers) {
		this.exposeSpringMacroHelpers = exposeSpringMacroHelpers;
	}

	@Override
	protected void populateContext(ScopesHashModel model, ValueStack stack,
			Object action, HttpServletRequest request,
			HttpServletResponse response) {
		super.populateContext(model, stack, action, request, response);		
		HashMap<String, Object> map = new HashMap<String, Object>();		
		populateStatics(map);		
		
		if(exposeSpringMacroHelpers)
			exposeSpringMacroHelpers(map, request, response);
		
		model.putAll(map);		
	}

	protected void exposeSpringMacroHelpers(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response){
		model.put(AbstractTemplateView.SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE, 	new RequestContext(request, response, request.getSession().getServletContext(), model));
	}

	
	
	public void populateStatics(Map<String, Object> model){		
		BeansWrapper b = (BeansWrapper) wrapper;
		FreeMarkerHelper.populateStatics(b, model);		
	}

	@Override
    protected TemplateLoader createTemplateLoader(ServletContext servletContext, String templatePath) {
		TemplateLoader loader = freeMarkerConfig.getConfiguration().getTemplateLoader();	  
	    //log.debug("templatePath:" + templatePath );
	    //log.debug("loader:" + loader.getClass().getName() );
	    
	    return loader;	        
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
