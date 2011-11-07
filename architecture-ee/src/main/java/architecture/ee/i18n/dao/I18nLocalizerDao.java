package architecture.ee.i18n.dao;

import java.util.List;

import architecture.ee.i18n.I18nLocalizer;

public interface I18nLocalizerDao {

	public abstract int getAvailableI18nLocalizerCount();
    
	public abstract List<I18nLocalizer> getAvailableI18nLocalizers();	
	
	public abstract I18nLocalizer getI18nLocalizerById(long localizerId);
	
	public abstract List<I18nLocalizer> getI18nLocalizersByName(String name);
		 
    public abstract I18nLocalizer addI18nLocalizer(I18nLocalizer localizer);

    public abstract I18nLocalizer updateI18nLocalizer(I18nLocalizer localizer);

    public abstract void deleteI18nLocalizer(I18nLocalizer localizer);

    
/**
    public abstract void updateTexts(List<I18nText2> texts);

    public abstract void deleteTexts(List<I18nText2> texts);
    **/
}
