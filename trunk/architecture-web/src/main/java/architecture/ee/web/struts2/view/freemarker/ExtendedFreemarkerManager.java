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

import architecture.ee.util.ApplicationHelper;
import architecture.ee.web.theme.ThemeManager;
import architecture.ee.web.util.ApplicatioinConstants;

import com.opensymphony.xwork2.util.ValueStack;

import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.StrongCacheStorage;
import freemarker.cache.TemplateLoader;
import freemarker.core.Environment;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;

public class ExtendedFreemarkerManager extends FreemarkerManager {
	
    //private List tagModels;  
	
	private static final String THEME_CONFIG_SERVLET_CONTEXT_KEY = "themes.freemarker.Configuration";

    private static final Log log = LogFactory.getLog(ExtendedFreemarkerManager.class);
    
    private static final int UPDATE_DELAY = 60;
            
    public static final TemplateExceptionHandler LOG_DEBUG_HANDLER = new TemplateExceptionHandler() {

        public void handleTemplateException(TemplateException te, Environment env, Writer out)
        {
        	ExtendedFreemarkerManager.log.warn("Freemarker template exception. ", te);
        }
    };    
    
    public ExtendedFreemarkerManager() {
    	
	}
        

    private ThemeManager getThemeManager() {
		return ApplicationHelper.getComponent(ThemeManager.class);
	}

	public synchronized Configuration getThemeConfiguration(ServletContext servletContext) throws TemplateException {
		Configuration config = (Configuration)servletContext.getAttribute(THEME_CONFIG_SERVLET_CONTEXT_KEY);
        if(config == null)
            synchronized(servletContext)
            {
            	if(wrapper == null)
            		wrapper = createObjectWrapper(servletContext);
                config = createThemeConfiguration(servletContext);                
                servletContext.setAttribute(THEME_CONFIG_SERVLET_CONTEXT_KEY, config);
            }
        return config;
    }
	
    protected Configuration createThemeConfiguration(ServletContext servletContext)
    {	
        Configuration configuration = new ThemeConfiguration(getThemeManager());
        configureDefaultConfiguration(configuration, servletContext);
        configuration.setTemplateLoader(getTemplateLoader(servletContext));
        configuration.setWhitespaceStripping(true);
        return configuration;
    }
    
	@Override
	protected Configuration createConfiguration(ServletContext servletContext)
			throws TemplateException {		
        Configuration configuration = new Configuration();
        configuration.setTemplateLoader(getTemplateLoader(servletContext));
        configureDefaultConfiguration(configuration, servletContext);
        configuration.setWhitespaceStripping(true);
        return configuration;
	}

    protected void configureDefaultConfiguration(Configuration configuration, ServletContext servletContext)
    {     
    	configuration.setCacheStorage(new StrongCacheStorage());        
    	configuration.setTemplateExceptionHandler(getTemplateExceptionHandler());         
        configuration.setOutputEncoding(ApplicationHelper.getCharacterEncoding());        
        configuration.setDefaultEncoding(ApplicationHelper.getCharacterEncoding());        
        configuration.addAutoImport("framework", "/template/common/include/framework-macros.ftl");        
        configuration.setLocalizedLookup(false);      
        configuration.setObjectWrapper(wrapper);
    }
    
    protected TemplateLoader getTemplateLoader(ServletContext servletContext)
    {    	
    	log.debug("getTemplateLoader" );
        return new MultiTemplateLoader(new TemplateLoader[] {
            new ThemeTemplateLoader(), 
            new ExtendedWebappTemplateLoader(servletContext), 
            new ExtendedClassTemplateLoader(ExtendedFreemarkerResult.class, "/")
        });
    }
    
    
	@Override
	protected void loadSettings(ServletContext servletContext) {
		
		log.debug("loadSettings" + config ); 
		
		super.loadSettings(servletContext);
		boolean devMode = isDevMode();
		
        if(devMode){
        	config.setTemplateUpdateDelay(1);
	    }else{
	    	int dealy = ApplicationHelper.getApplicationIntProperty(ApplicatioinConstants.FREEMARKER_TEMPLATE_UPDATE_DELAY_PROP_NAME, UPDATE_DELAY);
	    	config.setTemplateUpdateDelay(dealy);
	    }
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        
        log.debug("loadSettings end" );  
        
        if( request == null || response == null )
        {
            return;
        } else
        {
        	config.setDefaultEncoding(ApplicationHelper.getCharacterEncoding());
        	config.setLocale(ApplicationHelper.getLocale());
        	config.setTimeZone(ApplicationHelper.getTimeZone());
            return;
        }	
        
	}
	
    protected boolean isDevMode()
    {
        return ApplicationHelper.getApplicationBooleanProperty("devMode", false);
    } 
    
	@Override
	protected ScopesHashModel buildScopesHashModel(
			ServletContext servletContext, HttpServletRequest request,
			HttpServletResponse response, ObjectWrapper wrapper,
			ValueStack stack) {
		return super.buildScopesHashModel(servletContext, request, response, wrapper, stack);
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
			model.put("I18nTextUtils",         staticModels.get("architecture.ee.util.I18nTextUtils"));
			model.put("LocaleUtils",           staticModels.get("architecture.ee.util.LocaleUtils"));
			model.put("SecurityHelper",        staticModels.get("architecture.ee.util.SecurityHelper"));
			model.put("ApplicationHelper",     staticModels.get("architecture.ee.util.ApplicationHelper"));			
			model.put("ParamUtils",            staticModels.get("architecture.ee.web.util.ParamUtils"));
			model.put("ServletUtils",          staticModels.get("architecture.ee.web.util.ServletUtils"));
			model.put("ApplicatioinConstants", staticModels.get("architecture.ee.web.util.ApplicatioinConstants"));
			
		} catch (TemplateModelException e) {
			log.error(e);
		}		
		model.put("statics", BeansWrapper.getDefaultInstance().getStaticModels());
	}
	
    private TemplateExceptionHandler getTemplateExceptionHandler()
    {
        boolean devMode = isDevMode();
        boolean logErrorDefault = !devMode;
        boolean logError = ApplicationHelper.getApplicationBooleanProperty(ApplicatioinConstants.FREEMARKER_LOG_ERROR_PROP_NAME, logErrorDefault);
        TemplateExceptionHandler handler;
        
        if(logError)
            handler = LOG_DEBUG_HANDLER;
        else
            handler = TemplateExceptionHandler.HTML_DEBUG_HANDLER;
        return handler;
    }
    
	
}