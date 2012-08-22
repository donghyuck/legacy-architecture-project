package architecture.common.model.impl;

import java.io.Serializable;
import java.util.Locale;

import architecture.common.i18n.Country;
import architecture.common.model.CountryModel;
import architecture.common.model.ModelObjectType;

public class CountryModelImpl extends BaseModelObject <Country> implements CountryModel {

	private long countryId;
	
	private String country;
	
	private String language;
	
	public Serializable getPrimaryKeyObject() {
		return countryId;
	}

	public int getObjectType() {
		return ModelObjectType.Unknown.getTypeId();
	}

	public int getCachedSize() {
		return 0;
	}

	public int compareTo(Country o) {
		return 0;
	}

	public void setCountryId(long countryId) {
		this.countryId = countryId;
	}

	public long getCountryId() {
		return countryId;
	}

	public String getCountry() {
		return country;
	}

	public String getLanguage() {
		return language;
	}

	public Locale getLoacle(){
		return new Locale(getLanguage(), getCountry());
	}
	
}
