package architecture.ee.model.internal;

import java.util.Date;

import architecture.common.util.StringUtils;
import architecture.ee.g11n.I18nLocale;
import architecture.ee.g11n.internal.I18nLocaleImpl;
import architecture.ee.model.I18nLocaleModel;

public class I18nLocaleModelImpl extends BaseModelImpl<I18nLocale> implements I18nLocaleModel {
	
	private long localeId = -1L;
	private String language;
	private String country;
	private String encoding;
	private String variant;
	private Date creationDate;
	private Date modifiedDate;	

	public long getPrimaryKey(){
		return getLocaleId();
	}

	public void setPrimaryKey(long pk){
		setLocaleId(pk);
	}
	
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
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public int compareTo(architecture.ee.g11n.I18nLocale o) {
		long pk = o.getPrimaryKey();

		if (getPrimaryKey() < pk) {
			return -1;
		}
		else if (getPrimaryKey() > pk) {
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
		return (int)getPrimaryKey();
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
	
	public String toXmlString() {
		return null;
	}
	
}