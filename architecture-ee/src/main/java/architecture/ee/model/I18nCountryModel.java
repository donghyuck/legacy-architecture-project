package architecture.ee.model;

import architecture.ee.i18n.I18nCountry;

/**
 * @author                 donghyuck
 */
public interface I18nCountryModel extends ModelObject<I18nCountry> {

	/**
	 * @return
	 */
	public String getName();

	/**
	 * @param  countryId
	 */
	public void setCountryId(long countryId);

	/**
	 * @return
	 */
	public long getCountryId();

	/**
	 * @param  name
	 */
	public void setName(String name);

	/**
	 * @return
	 */
	public String getA2();

	/**
	 * @param  a2
	 */
	public void setA2(String a2);

	/**
	 * @return
	 */
	public String getA3();

	/**
	 * @param  a3
	 */
	public void setA3(String a3);

	/**
	 * @return
	 */
	public String getNumber();

	/**
	 * @param  number
	 */
	public void setNumber(String number);

	/**
	 * @return
	 */
	public String getIdd();

	/**
	 * @param  idd
	 */
	public void setIdd(String idd);

	/**
	 * @return
	 */
	public boolean isEnabled();

	/**
	 * @param  enabled
	 */
	public void setEnabled(boolean enabled);
	
}
