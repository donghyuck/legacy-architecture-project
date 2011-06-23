package architecture.ee.util;

import java.util.List;

import architecture.ee.g11n.I18nText;
import architecture.ee.g11n.internal.I18nTextImpl;

public class I18nTextUtils {
    
    public static String generateResourceBundleKey(int objectType, long objectID, int objectAttribute)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(objectType).append(".").append(objectID).append(".").append(objectAttribute);
        return builder.toString();
    }
    
    public static I18nText getText(List<I18nText> list, String localeCode, int attribute)
    {
        if(list == null)
            return null;
        
        for(I18nText text : list){
            if(checkEquals(text, localeCode, attribute))
                return text;
        }
        return null;
    }

    private static boolean checkEquals(I18nText text, String localeCode, int attribute)
    {
        return text.getLocaleCode().equals(localeCode)&& text.getObjectAttribute() == attribute;
    }
    
    public static void setText(List<I18nText> list, String localeCode, int attribute, String text)
    {
        I18nText i18nText = getText(list, localeCode, attribute);
        if(i18nText == null)
        {
            if(text != null)
                list.add(createText(attribute, localeCode, text));
        } else
        {
        	i18nText.setText(text);
        }
    }
    
    private static I18nText createText(int objectAttribute, String localeCode, String text)
    {
    	I18nText i18nText = new I18nTextImpl();
    	i18nText.setTextId(-1L);
    	i18nText.setObjectId(-1L);
    	i18nText.setObjectType(-1);
    	i18nText.setObjectAttribute(objectAttribute);
    	i18nText.setLocaleCode(localeCode);
    	i18nText.setText(text);
        return i18nText;
    }
    
}
