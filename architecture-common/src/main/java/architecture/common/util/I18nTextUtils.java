package architecture.common.util;

import java.util.List;

import architecture.common.i18n.I18nText;
import architecture.common.i18n.I18nTextManager;
import architecture.common.i18n.impl.I18nTextImpl;
import architecture.common.lifecycle.ApplicationHelperFactory;

public abstract class I18nTextUtils {
	
	public static String generateResourceBundleKey(int objectType, long objectId, int objectAttribute){
        StringBuilder builder = new StringBuilder();                
        builder.append( objectType > 0 ? StringUtils.leftPad(Integer.toString(objectType), 3, '0') : objectType );
        builder.append(".");
        builder.append( objectId > 0 ? StringUtils.leftPad(Long.toString(objectId), 6, '0') : objectId );
        builder.append(".");
        builder.append( objectAttribute > 0 ? StringUtils.leftPad(Integer.toString(objectAttribute), 3, '0') : objectAttribute );        
        return builder.toString();
	}
	
	
	public static I18nText getI18nText(List<I18nText> list, String localeCode, int attribute){
		if( list == null )
			return null;		
		for(I18nText t : list){
		    if(t.getLocaleCode().equals(localeCode) && t.getObjectAttribute() == attribute)
		    	return t;
		}
		return null;
	}
	
	public static void setI18nText(List<I18nText> list, String localeCode, int attribute, String text)
	{
		I18nText t = getI18nText(list, localeCode, attribute);
		if( t == null )
		{
			if( text != null)
				list.add(createI18nText(attribute, localeCode, text));
		}else{
			t.setText(text);
		}
	}
	
	public static I18nText createI18nText( int objectType, long objectId, int objectAttribute, String localeCode, String text)
	{
		I18nText impl = new I18nTextImpl();
		impl.setTextId(-1L);
		impl.setObjectType(objectType);
		impl.setObjectId(objectId);
		impl.setObjectAttribute(objectAttribute);
		impl.setLocaleCode(localeCode);
		impl.setText(text);
		return impl;
	}

	public static I18nText createI18nText(int objectAttribute, String localeCode, String text)
	{
		I18nText impl = new I18nTextImpl();
		impl.setTextId(-1L);
		impl.setObjectType(-1);
		impl.setObjectId(-1L);
		impl.setObjectAttribute(objectAttribute);
		impl.setLocaleCode(localeCode);
		impl.setText(text);
		return impl;
	}
	
	public static I18nTextManager getI18nTextManager(){		
		I18nTextManager manager = ApplicationHelperFactory.getApplicationHelper().getComponent(I18nTextManager.class);
		return manager;
	}

}