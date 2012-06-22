package architecture.ee.model;

import architecture.ee.i18n.I18nLocale;


/**
 * @author                   donghyuck
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
