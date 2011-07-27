package architecture.ee.g11n.dao;

import java.util.List;
import java.util.Locale;

import architecture.ee.g11n.I18nText;

public interface I18nTextDao {

    public abstract void createTexts(List<I18nText> list);

    public abstract void updateTexts(List<I18nText> list);

    public abstract void deleteTexts(List<I18nText> list);

    public abstract I18nText getText(long textId);
    
    public abstract List<I18nText> getTexts();
    
    public abstract List<I18nText> getTexts(Locale locale);
    
    public abstract List<I18nText> getTexts(int objectType);

    public abstract List<I18nText> getTexts(int objectType, long objectId);

    public abstract List<I18nText> getTexts(int objectType, String locale);
	
}
