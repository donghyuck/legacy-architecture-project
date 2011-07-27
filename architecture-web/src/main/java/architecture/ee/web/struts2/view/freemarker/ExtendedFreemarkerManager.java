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

import architecture.ee.util.AdminHelper;
import architecture.ee.util.ApplicatioinConstants;

import com.opensymphony.xwork2.util.ValueStack;

import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.StrongCacheStorage;
import freemarker.cache.TemplateLoader;
import freemarker.cache.WebappTemplateLoader;
import freemarker.core.Environment;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;

public class ExtendedFreemarkerManager extends FreemarkerManager {
	
    private List tagModels;  

    private static final Log log = LogFactory.getLog(ExtendedFreemarkerManager.class);
    private static final int UPDATE_DELAY = 0x7fffffff;
    private static BeansWrapper wrapper = new BeansWrapper();
    public static final TemplateExceptionHandler LOG_DEBUG_HANDLER = new TemplateExceptionHandler() {

        public void handleTemplateException(TemplateException te, Environment env, Writer out)
        {
        	ExtendedFreemarkerManager.log.warn("Freemarker template exception. ", te);
        }
    };
    
    public ExtendedFreemarkerManager() {
		this.tagModels = Collections.emptyList();
		
	}


	public void setTagModels(List tagModels)
    {
        this.tagModels = tagModels;
    }
    
    private ObjectWrapper getExtendedObjectWrapper()
    {
        BeansWrapper wrapper = new BeansWrapper();
        wrapper.setSimpleMapWrapper(false);
        return wrapper;
    }
    

	@Override
	protected void populateContext(ScopesHashModel model, ValueStack stack, Object action, HttpServletRequest request, HttpServletResponse response) {

		super.populateContext(model, stack, action, request, response);
		
		HashMap map = new HashMap();
		
		populateStatics(map);
		
		model.putAll(map);
		
	}

	protected TemplateLoader getTemplateLoader(ServletContext servletContext){		
		return new MultiTemplateLoader(new TemplateLoader[] {new WebappTemplateLoader(servletContext), new ExtendedClassTemplateLoader( ExtendedFreemarkerResult.class, "/")});
	}
	
	@Override
	protected Configuration createConfiguration(ServletContext servletContext) throws TemplateException {
        Configuration configuration = new Configuration();
        configuration.setTemplateLoader(getTemplateLoader(servletContext));
        configureDefaultConfiguration(configuration, servletContext);
        return configuration;
	}

    protected void configureDefaultConfiguration(Configuration configuration, ServletContext servletContext)
    {

        configuration.setCacheStorage(new StrongCacheStorage());
        configuration.setTemplateExceptionHandler(getTemplateExceptionHandler());        
        configuration.setObjectWrapper(getExtendedObjectWrapper());
        
        configuration.setOutputEncoding(AdminHelper.getCharacterEncoding());
        configuration.setDefaultEncoding(AdminHelper.getCharacterEncoding());
        configuration.addAutoImport("framework", "/template/default/include/framework-macros.ftl");
        configuration.setLocalizedLookup(false);
        loadSettings(servletContext, configuration);
        
    }
    
    protected void loadSettings(ServletContext servletContext, Configuration configuration)
    {
    	super.loadSettings(servletContext);
    	
        boolean devMode = isDevMode();
        if(devMode)
            configuration.setTemplateUpdateDelay(1);
        else
            configuration.setTemplateUpdateDelay(AdminHelper.getApplicationIntProperty(ApplicatioinConstants.FREEMARKER_TEMPLATE_UPDATE_DELAY_PROP_NAME, UPDATE_DELAY));
        
        
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        if(request == null || response == null)
        {
            return;
        } else
        {
            configuration.setDefaultEncoding(AdminHelper.getCharacterEncoding());
            configuration.setLocale(AdminHelper.getLocale());
            configuration.setTimeZone(AdminHelper.getTimeZone());
            return;
        }
    }
    
    
    private TemplateExceptionHandler getTemplateExceptionHandler()
    {
        boolean devMode = isDevMode();
        boolean logErrorDefault = !devMode;
        boolean logError = AdminHelper.getApplicationBooleanProperty(ApplicatioinConstants.FREEMARKER_LOG_ERROR_PROP_NAME, logErrorDefault);
        TemplateExceptionHandler handler;
        if(logError)
            handler = LOG_DEBUG_HANDLER;
        else
            handler = TemplateExceptionHandler.HTML_DEBUG_HANDLER;
        return handler;
    }
    
	public static void populateStatics(Map model){
		 try
	        {
	            TemplateHashModel enumModels = wrapper.getEnumModels();
	            try
	            {
	            	
	            }
	            catch(Exception e)
	            {
	                log.error(e);
	            }
	            model.put("enums", wrapper.getEnumModels());
	            
	        }
	        catch(UnsupportedOperationException e) { }
	        TemplateHashModel staticModels = wrapper.getStaticModels();
	        try
	        {
	            model.put("AdminHelper", staticModels.get("architecture.ee.util.AdminHelper"));
	        }
	        catch(TemplateModelException e)
	        {
	            log.error(e);
	        }
	        model.put("statics", BeansWrapper.getDefaultInstance().getStaticModels());
	}
	
    protected int getStrongCacheSize()
    {
        if(isDevMode())
            return 0;
        else
            return AdminHelper.getApplicationIntProperty(ApplicatioinConstants.FREEMARKER_STRONG_TEMPLATE_CACHE_SIZE_PROP_NAME, 400);
    }

    protected int getWeakCacheSize()
    {
        if(isDevMode())
            return 0;
        else
            return AdminHelper.getApplicationIntProperty(ApplicatioinConstants.FREEMARKER_WEAK_TEMPLATE_CACHE_SIZE_PROP_NAME, 500);
    }
    
    protected boolean isDevMode()
    {
        return AdminHelper.getApplicationBooleanProperty("devMode", false);
    }
	/**
	public void flushTemplateCache(ServletContext context)
			throws TemplateException {
		Configuration config = getThemeConfiguration(context);
		if (config.getTemplateLoader() instanceof MultiTemplateLoader) {
			MultiTemplateLoader multi = (MultiTemplateLoader) config
					.getTemplateLoader();
			multi.resetState();
		}
		config.clearTemplateCache();
	}

	public void flushTemplateCacheWithoutServletContext()
			throws TemplateException {
		ServletContext context = ServletActionContext.getServletContext();
		if (context == null)
			context = new FakeServletContext();
		flushTemplateCache(context);
	}*/
    
    
}
