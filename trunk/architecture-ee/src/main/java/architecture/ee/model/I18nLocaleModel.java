package architecture.ee.model;

import java.util.Date;

import architecture.ee.g11n.I18nLocale;

public interface I18nLocaleModel extends BaseModel<I18nLocale> {


    public abstract long getLocaleId();

    public abstract void setLocaleId(long id) ;

    public abstract String getLanguage() ;

    public abstract void setLanguage(String language) ;

    public abstract String getCountry();

    public abstract void setCountry(String country);

    public abstract String getEncoding();

    public abstract void setEncoding(String encoding);

    public abstract Date getCreationDate();

    public abstract void setCreationDate(Date creationDate) ;

    public abstract Date getModifiedDate();

    public abstract void setModifiedDate(Date modifiedDate) ;
        
    public abstract String getVariant() ;

    public abstract void setVariant(String variant);

    public abstract java.util.Locale toJavaLocale();
        
	public int hashCode();

	public String toString();
	
}