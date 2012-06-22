package architecture.ee.model;

import architecture.ee.i18n.I18nCountry;

/**
 * @author                   donghyuck
 */
public interface I18nCountryModel extends ModelObject<I18nCountry> {

	/**
	 * @return
	 * @uml.property  name="name"
	 */
	public String getName();

	/**
	 * @param  countryId
	 * @uml.property  name="countryId"
	 */
	public void setCountryId(long countryId);

	/**
	 * @return
	 * @uml.property  name="countryId"
	 */
	public long getCountryId();

	/**
	 * @param  name
	 * @uml.property  name="name"
	 */
	public void setName(String name);

	/**
	 * @return
	 * @uml.property  name="a2"
	 */
	public String getA2();

	/**
	 * @param  a2
	 * @uml.property  name="a2"
	 */
	public void setA2(String a2);

	/**
	 * @return
	 * @uml.property  name="a3"
	 */
	public String getA3();

	/**
	 * @param  a3
	 * @uml.property  name="a3"
	 */
	public void setA3(String a3);

	/**
	 * @return
	 * @uml.property  name="number"
	 */
	public String getNumber();

	/**
	 * @param  number
	 * @uml.property  name="number"
	 */
	public void setNumber(String number);

	/**
	 * @return
	 * @uml.property  name="idd"
	 */
	public String getIdd();

	/**
	 * @param  idd
	 * @uml.property  name="idd"
	 */
	public void setIdd(String idd);

	/**
	 * @return
	 * @uml.property  name="enabled"
	 */
	public boolean isEnabled();

	/**
	 * @param  enabled
	 * @uml.property  name="enabled"
	 */
	public void setEnabled(boolean enabled);
	
}
