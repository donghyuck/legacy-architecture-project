package architecture.ee.model;

import architecture.ee.i18n.I18nCountry;

public interface I18nCountryModel extends ModelObject<I18nCountry> {

	public String getName();

	public void setCountryId(long countryId);

	public long getCountryId();

	public void setName(String name);

	public String getA2();

	public void setA2(String a2);

	public String getA3();

	public void setA3(String a3);

	public String getNumber();

	public void setNumber(String number);

	public String getIdd();

	public void setIdd(String idd);

	public boolean isEnabled();

	public void setEnabled(boolean enabled);
	
}
