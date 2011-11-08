package architecture.ee.model;

import java.util.Date;

import architecture.ee.i18n.I18nLocale;


/**
 * @author                 donghyuck
 */
public interface I18nLocaleModel extends ModelObject<I18nLocale> {

    /**
	 * @return
	 * @uml.property  name="localeId"
	 */
    public abstract long getLocaleId();

    /**
	 * @param  id
	 * @uml.property  name="localeId"
	 */
    public abstract void setLocaleId(long id) ;

    /**
	 * @return
	 * @uml.property  name="language"
	 */
    public abstract String getLanguage() ;

    /**
	 * @param  language
	 * @uml.property  name="language"
	 */
    public abstract void setLanguage(String language) ;

    /**
	 * @return
	 * @uml.property  name="country"
	 */
    public abstract String getCountry();

    /**
	 * @param  country
	 * @uml.property  name="country"
	 */
    public abstract void setCountry(String country);

    /**
	 * @return
	 * @uml.property  name="encoding"
	 */
    public abstract String getEncoding();

    /**
	 * @param  encoding
	 * @uml.property  name="encoding"
	 */
    public abstract void setEncoding(String encoding);

    /**
	 * @return
	 * @uml.property  name="creationDate"
	 */
    public abstract Date getCreationDate();

    /**
	 * @param  creationDate
	 * @uml.property  name="creationDate"
	 */
    public abstract void setCreationDate(Date creationDate) ;

    /**
	 * @return
	 * @uml.property  name="modifiedDate"
	 */
    public abstract Date getModifiedDate();

    /**
	 * @param  modifiedDate
	 * @uml.property  name="modifiedDate"
	 */
    public abstract void setModifiedDate(Date modifiedDate) ;
        
    /**
	 * @return
	 * @uml.property  name="variant"
	 */
    public abstract String getVariant() ;

    /**
	 * @param  variant
	 * @uml.property  name="variant"
	 */
    public abstract void setVariant(String variant);

    public abstract java.util.Locale toJavaLocale();
}
