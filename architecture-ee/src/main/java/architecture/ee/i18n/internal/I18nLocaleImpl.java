package architecture.ee.i18n.internal;

import java.io.Serializable;

import architecture.common.cache.CacheSizes;
import architecture.common.model.factory.ModelTypeFactory;
import architecture.common.model.support.DateModelObjectSupport;
import architecture.common.util.StringUtils;
import architecture.ee.i18n.I18nLocale;

/**
 * @author   donghyuck
 */
public class I18nLocaleImpl extends DateModelObjectSupport implements I18nLocale{

	private long localeId = -1L;

	private String language;

	private String country;

	private String encoding;

	private String variant;

	public long getLocaleId() {
		return localeId;
	}

	public void setLocaleId(long localeId) {
		this.localeId = localeId;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getVariant() {
		return variant;
	}

	public void setVariant(String variant) {
		this.variant = variant;
	}

	public int compareTo(architecture.ee.i18n.I18nLocale o) {
		long pk = o.getLocaleId();

		if (getLocaleId() < pk) {
			return -1;
		}
		else if (getLocaleId() > pk) {
			return 1;
		}
		else {
			return 0;
		}
	}

	public java.util.Locale toJavaLocale() {
		return new java.util.Locale(language, StringUtils.isNotEmpty(country)?country:"", StringUtils.isNotEmpty(variant)? variant: "");
	}

	public int hashCode() {
		return (int)getLocaleId();
	}
	
	public Object clone() {
		I18nLocaleImpl impl = new I18nLocaleImpl();
		impl.setLocaleId(getLocaleId());
		impl.setLanguage(getLanguage());
		impl.setCountry(getCountry());
		impl.setVariant(getVariant());
		impl.setEncoding(getEncoding());
		impl.setCreationDate(getCreationDate());
		impl.setModifiedDate(getModifiedDate());		
		return impl;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{localeId=");
		sb.append(getLocaleId());
		sb.append(", language=");
		sb.append(getLanguage());		
		sb.append(", country=");
		sb.append(getCountry());	
		sb.append(", variant=");
		sb.append(getVariant());				
		sb.append(", creattionDate=");
		sb.append(getCreationDate());
		sb.append(", modifiedDate=");
		sb.append(getModifiedDate());
		sb.append("}");
		return sb.toString();
	}
	
	
	public Serializable getPrimaryKeyObject() {
		return getLocaleId();
	}
	public void setPrimaryKeyObject(Serializable primaryKeyObj) {
		setLocaleId(((Long)primaryKeyObj).longValue());
	}
	public int getModelObjectType() {		
		return ModelTypeFactory.getTypeIdFromCode("I18N_LOCALE");
	}
	
	public int getCachedSize() {
		int size = 0 ;
		size += CacheSizes.sizeOfLong();
		size += CacheSizes.sizeOfString(language);
		size += CacheSizes.sizeOfString(country) ;
		size += CacheSizes.sizeOfString(variant) ;
		size += CacheSizes.sizeOfDate();
		size += CacheSizes.sizeOfDate();		
		return size;
	}
}
