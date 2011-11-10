package architecture.ee.util;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.ee.admin.AdminHelper;
import architecture.ee.i18n.I18nLocalizer;
import architecture.ee.i18n.I18nText2;
import architecture.ee.model.impl.I18nTextModel2Impl;

public class I18nTextUtils {
    
	private static final Log log = LogFactory.getLog(I18nTextUtils.class);
	
	
	public static List<I18nLocalizer> getI18nLocalizers(String name){		
		return ApplicationHelper.getI18nTextManager().getI18nLocalizersByName(name);
	}
	
	public static I18nLocalizer getI18nLocalizer(String bundleName){ 	
		return getI18nLocalizer(bundleName, AdminHelper.getConfigService().getLocale());
	}
	
	public static Locale getLocale(){		
		return  AdminHelper.getConfigService().getLocale();
	}
	
	public static I18nLocalizer getI18nLocalizer(String bundleName, Locale targetLocale){ 		

		Locale localeToUse = targetLocale;		
		if(localeToUse == null)
			localeToUse = getLocale();

		if(log.isDebugEnabled())
			log.debug("Load localizer [ name=" + bundleName + ", locale=" + localeToUse + " ]");
		
		List<I18nLocalizer> localizers = getI18nLocalizers(bundleName);
		
		for(I18nLocalizer localizer : localizers ){
			boolean isMatch = localeToUse.equals( localizer.getI18nLocale().toJavaLocale());
			if(log.isDebugEnabled())
				log.debug( " match " + isMatch );				
			if( isMatch )
				return localizer;			
		}
		
		/*
		for(I18nLocalizer localizer : localizers ){			
			boolean isMatch = getLocale().equals( localizer.getI18nLocale().toJavaLocale());
			if(log.isDebugEnabled())
				log.debug( " local checking with " + getLocale() + " (" + isMatch +")" );				
			if( isMatch )
				return localizer;	
		}*/

		for(I18nLocalizer localizer : localizers ){			
			boolean isMatch = Locale.ENGLISH.equals( localizer.getI18nLocale().toJavaLocale());
			if(log.isDebugEnabled())
				log.debug( " local checking with " + Locale.US + " (" + isMatch +")" );			
			if( isMatch )
				return localizer;	
		}
		
		return null;
	}	

    public static ResourceBundle getResourceBundle(String bundleName, Locale targetLocale){
    	
    	ResourceBundle bundle = null;
    	I18nLocalizer localizer = getI18nLocalizer(bundleName, targetLocale);    	
    	if( localizer!=null ){
    		bundle = localizer.getResourceBundle();
    	}else{
    		if(log.isDebugEnabled())
    			log.debug(" ... in local file system.");
    		
    		bundle = ResourceBundle.getBundle(bundleName , targetLocale);   
    	}
    	return bundle;
    }
    
    public static ResourceBundle getResourceBundle(String bundleName){    	
    	Locale targetLocale = AdminHelper.getConfigService().getLocale();    	
    	return getResourceBundle(bundleName, targetLocale);
	}
	
	
    public static String generateResourceBundleKey(int objectType, long objectID, int objectAttribute)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(objectType).append(".").append(objectID).append(".").append(objectAttribute);
        return builder.toString();
    }
    
    public static I18nText2 getText(List<I18nText2> list, String localeCode, int attribute)
    {
        if(list == null)
            return null;
        
        for(I18nText2 text : list){
            if(checkEquals(text, localeCode, attribute))
                return text;
        }
        return null;
    }

    private static boolean checkEquals(I18nText2 text, String localeCode, int attribute)
    {
        return text.getLocaleCode().equals(localeCode)&& text.getObjectAttribute() == attribute;
    }
    
    public static void setText(List<I18nText2> list, String localeCode, int attribute, String text)
    {
    	I18nText2 i18nText = getText(list, localeCode, attribute);
        if(i18nText == null)
        {
            if(text != null)
                list.add(createText(attribute, localeCode, text));
        } else
        {
        	i18nText.setText(text);
        }
    }
    
    private static I18nText2 createText(int objectAttribute, String localeCode, String text)
    {
    	I18nText2 i18nText = (I18nText2) new I18nTextModel2Impl();
    	i18nText.setTextId(-1L);
    	i18nText.setObjectId(-1L);
    	i18nText.setObjectType(-1);
    	i18nText.setObjectAttribute(objectAttribute);
    	i18nText.setLocaleCode(localeCode);
    	i18nText.setText(text);
        return i18nText;
    }
    
}
