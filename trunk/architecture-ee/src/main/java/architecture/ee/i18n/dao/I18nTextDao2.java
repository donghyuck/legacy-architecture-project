package architecture.ee.i18n.dao;

import java.util.List;
import java.util.Locale;

import architecture.ee.i18n.I18nText2;

public interface I18nTextDao2 {

    public abstract void createTexts(List<I18nText2> list);

    public abstract void updateTexts(List<I18nText2> list);

    public abstract void deleteTexts(List<I18nText2> list);

    public abstract I18nText2 getText(long textId);
    
    public abstract List<I18nText2> getTexts();
    
    public abstract List<I18nText2> getTexts(Locale locale);
    
    public abstract List<I18nText2> getTexts(int objectType);

    public abstract List<I18nText2> getTexts(int objectType, long objectId);

    public abstract List<I18nText2> getTexts(int objectType, String locale);
	
}
