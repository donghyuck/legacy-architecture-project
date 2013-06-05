package architecture.ee.web.struts2.util;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.LocaleProvider;
import com.opensymphony.xwork2.TextProviderSupport;
import com.opensymphony.xwork2.util.ValueStack;

public class FrameworkTextProvider extends TextProviderSupport {

	private Log log = LogFactory.getLog(getClass());
	
    protected Class clazz;
    protected LocaleProvider provider;
    protected ResourceBundle bundle;
    protected boolean i18nTextBundlesLoaded;
    protected Map i18nTextBundles;
    
	public FrameworkTextProvider(Class clazz, LocaleProvider provider) {
		super(clazz, provider);
		this.clazz = clazz;
		this.provider = provider;
		this.i18nTextBundlesLoaded = false;
	}

	public FrameworkTextProvider(ResourceBundle bundle, LocaleProvider provider) {
		super(bundle, provider);
		this.i18nTextBundlesLoaded = false;
        this.bundle = bundle;
        this.provider = provider;
	}


    public String getText(String key, String defaultValue, String args[])
    {
        return doGetText(key, defaultValue, args, null);
    }

    public String getText(String key, String defaultValue, List args)
    {
        Object argsArray[] = args == null || args.equals(Collections.emptyList()) ? null : args.toArray();
        return doGetText(key, defaultValue, argsArray, null);
    }

    public String getText(String key, String defaultValue, List args, ValueStack stack)
    {
        Object argsArray[] = args == null ? null : args.toArray();
        return doGetText(key, defaultValue, argsArray, stack);
    }

    public String getText(String key, String defaultValue, String args[], ValueStack stack)
    {
        return doGetText(key, defaultValue, args, stack);
    }
	
    protected String doGetText(String key, String defaultValue, Object args[], ValueStack stack)
    {	
    	// 1. Get Stack
    	ValueStack theStack = stack != null ? stack : ActionContext.getContext().getValueStack();
    	
    	// 2. get Locale Information
    	Locale locale; 
        if(stack == null)
            locale = getLocale();
        else
        	locale = (Locale) theStack.getContext().get(ActionContext.LOCALE);
                
        if(locale == null)
            locale = getLocale();
        
        log.debug(
        		String.format("Find text with %s ( %s ).", new Object[] {
        				key, locale
                    })
        );      
        
        return "";
        
    }
    
    protected Locale getLocale()
    {
        return provider.getLocale();
    }
    
    
}
