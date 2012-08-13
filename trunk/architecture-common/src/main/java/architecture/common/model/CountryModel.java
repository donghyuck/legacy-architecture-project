package architecture.common.model;

import architecture.common.i18n.Country;

public interface CountryModel extends ModelObject<Country> {

	public void setCountryId(long countryId);

	public long getCountryId();
	
	/**
	 * country uppercase two-letter ISO-3166 code.
	 * @return
	 */
	public String getCountry() ;
	
	/**
	 * language lowercase two-letter ISO-639 code.
	 * @return
	 */
	public String getLanguage() ;
	

}
