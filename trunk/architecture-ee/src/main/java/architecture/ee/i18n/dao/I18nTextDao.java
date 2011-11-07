package architecture.ee.i18n.dao;

import java.util.List;

import architecture.ee.i18n.I18nText;

public interface I18nTextDao {

    
    public abstract List<I18nText> getTexts(long localizerId);
    
    public abstract void setTexts(List<I18nText> texts);
    
    
}
