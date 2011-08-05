package architecture.ee.web.struts2.view.freemarker;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.views.freemarker.FreemarkerManager;
import org.apache.struts2.views.freemarker.ScopesHashModel;

import architecture.ee.util.AdminHelper;
import architecture.ee.util.ApplicatioinConstants;

import com.opensymphony.xwork2.util.ValueStack;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
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

    private static final Log log = LogFactory.getLog(ExtendedFreemarkerManager.class);
    
    private static final int UPDATE_DELAY = 60;
        
    public static final TemplateExceptionHandler LOG_DEBUG_HANDLER = new TemplateExceptionHandler() {

        public void handleTemplateException(TemplateException te, Environment env, Writer out)
        {
        	ExtendedFreemarkerManager.log.warn("Freemarker template exception. ", te);
        }
    };    
    
    public ExtendedFreemarkerManager() {
	//	this.tagModels = Collections.emptyList();
	}
    
    
	/**
     *  Create a freemarker Configuration. (1)
     */
	@Override
	protected Configuration createConfiguration(ServletContext servletContext)
			throws TemplateException {

        Configuration configuration = new Configuration();
        configuration.setTemplateExceptionHandler(LOG_DEBUG_HANDLER);        
        
        if(log.isDebugEnabled()){
            log.debug("mruMaxStrongSize: " + mruMaxStrongSize);
            log.debug("templateUpdateDelay: " + templateUpdateDelay);
        }
        
        if (mruMaxStrongSize > 0) {
            configuration.setSetting(Configuration.CACHE_STORAGE_KEY, "strong:" + mruMaxStrongSize);
        }               
        if (templateUpdateDelay != null) {
            configuration.setSetting(Configuration.TEMPLATE_UPDATE_DELAY_KEY, templateUpdateDelay );
        }  
                
        if (encoding != null) {
            configuration.setDefaultEncoding(encoding);
        }        
        
        configuration.setWhitespaceStripping(true);        
        
        return configuration;
	}

	/**
	 * Load freemarker settings, default to freemarker.properties (if found in classpath
	 */
	@Override
	protected void loadSettings(ServletContext servletContext) {

		config.setCacheStorage(new StrongCacheStorage());
        config.setTemplateExceptionHandler(getTemplateExceptionHandler()); 
    	config.setOutputEncoding(AdminHelper.getCharacterEncoding());
    	config.setDefaultEncoding(AdminHelper.getCharacterEncoding());
    	config.addAutoImport("framework", "/template/default/include/framework-macros.ftl");
    	config.setLocalizedLookup(false);  
    	
    	
		super.loadSettings(servletContext);   
		
        boolean devMode = isDevMode();
        
        if(devMode){
        	config.setTemplateUpdateDelay(1);
	    }else{
	    
	    	int dealy = AdminHelper.getApplicationIntProperty(ApplicatioinConstants.FREEMARKER_TEMPLATE_UPDATE_DELAY_PROP_NAME, UPDATE_DELAY);
	    	config.setTemplateUpdateDelay(dealy);
	    	log.debug("(update) templateUpdateDelay: " + dealy );
	    
	    }
        
        if(log.isDebugEnabled()){
            log.debug("devMode:" + devMode );
        }
        
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        
        if( request == null || response == null )
        {
            return;
        } else
        {
        	config.setDefaultEncoding(AdminHelper.getCharacterEncoding());
        	config.setLocale(AdminHelper.getLocale());
        	config.setTimeZone(AdminHelper.getTimeZone());
            return;
        }
	}
	
	/**
	 * 
	 * create a freemarker TemplateLoader that loads freemarker template in the
	 * following order :-
	 * <ol>
	 * <li>path defined in ServletContext init parameter named 'templatePath' or
	 * 'TemplatePath' (must be an absolute path)</li>
	 * <li>webapp classpath</li>
	 * <li>struts's static folder (under
	 * [STRUT2_SOURCE]/org/apache/struts2/static/</li>
	 * </ol>
	 * <p/>
	 * 
	 */
	@Override
	protected TemplateLoader createTemplateLoader(ServletContext servletContext, String templatePath) {
		
		TemplateLoader templatePathLoader = null;

		if(! StringUtils.isEmpty(templatePath))
        try {
            if (templatePath.startsWith("class://")) {
                // substring(7) is intentional as we "reuse" the last slash
                templatePathLoader = new ClassTemplateLoader(getClass(), templatePath.substring(7));
            } else if (templatePath.startsWith("file://")) {
                templatePathLoader = new FileTemplateLoader(new File(templatePath));
            }
        } catch (IOException e) {
            log.error("Invalid template path specified: " + e.getMessage(), e);
        }
		
		return templatePathLoader != null ?
                new MultiTemplateLoader(
                	new TemplateLoader[]{
                        templatePathLoader,
                        new ExtendedWebappTemplateLoader(servletContext),
                        new ExtendedClassTemplateLoader(ExtendedFreemarkerResult.class, "/")
                }): 
                new MultiTemplateLoader(
                	new TemplateLoader[]{
                		new ExtendedWebappTemplateLoader(servletContext),
                		new ExtendedClassTemplateLoader(ExtendedFreemarkerResult.class, "/")});
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
		
		HashMap map = new HashMap();		
		populateStatics(map);		
		model.putAll(map);
	
	}

	public void populateStatics(Map model){
		
		BeansWrapper b = (BeansWrapper)wrapper;
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
			model.put("AdminHelper",           staticModels.get("architecture.ee.util.AdminHelper"));
			model.put("I18nTextUtils",         staticModels.get("architecture.ee.util.I18nTextUtils"));
			model.put("LocaleUtils",           staticModels.get("architecture.ee.util.LocaleUtils"));
			model.put("SecurityHelper",        staticModels.get("architecture.ee.util.SecurityHelper"));
			model.put("ApplicatioinConstants", staticModels.get("architecture.ee.util.ApplicatioinConstants"));
			
			
			model.put("ParamUtils",           staticModels.get("architecture.ee.web.util.ParamUtils"));
			model.put("ServletUtils",           staticModels.get("architecture.ee.web.util.ServletUtils"));
			
		} catch (TemplateModelException e) {
			log.error(e);
		}
		
		model.put("statics", BeansWrapper.getDefaultInstance().getStaticModels());

	}
	
    protected boolean isDevMode()
    {
        return AdminHelper.getApplicationBooleanProperty("devMode", false);
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
    
}