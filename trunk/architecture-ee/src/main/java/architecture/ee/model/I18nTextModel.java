package architecture.ee.model;

import architecture.ee.i18n.I18nText;

public interface I18nTextModel  extends ModelObject<I18nText> {

	public abstract long getTextId();
	
	public abstract void setTextId(long textId);
	
	public abstract long getLocalizerId();

	public abstract void setLocalizerId(long localizerId);	
	
	public abstract String getText();
	
	public abstract void setText(String text);
	
}
