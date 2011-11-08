package architecture.ee.model.impl;

import java.io.Serializable;

import architecture.ee.i18n.I18nCountry;
import architecture.ee.model.I18nCountryModel;

/**
 * @author  donghyuck
 */
public class I18nCountryModelImpl extends BaseModelObject<I18nCountry>
		implements I18nCountryModel, I18nCountry {
	
	/**
	 * @uml.property  name="countryId"
	 */
	private long countryId;
	/**
	 * @uml.property  name="name"
	 */
	private String name;
	/**
	 * @uml.property  name="a2"
	 */
	private String a2;
	/**
	 * @uml.property  name="a3"
	 */
	private String a3;
	/**
	 * @uml.property  name="number"
	 */
	private String number;
	/**
	 * @uml.property  name="idd"
	 */
	private String idd;
	/**
	 * @uml.property  name="enabled"
	 */
	private boolean enabled;

	/**
	 * @return
	 * @uml.property  name="countryId"
	 */
	public long getCountryId() {
		return countryId;
	}

	/**
	 * @param countryId
	 * @uml.property  name="countryId"
	 */
	public void setCountryId(long countryId) {
		this.countryId = countryId;
	}

	/**
	 * @return
	 * @uml.property  name="name"
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 * @uml.property  name="name"
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return
	 * @uml.property  name="a2"
	 */
	public String getA2() {
		return a2;
	}

	/**
	 * @param a2
	 * @uml.property  name="a2"
	 */
	public void setA2(String a2) {
		this.a2 = a2;
	}

	/**
	 * @return
	 * @uml.property  name="a3"
	 */
	public String getA3() {
		return a3;
	}

	/**
	 * @param a3
	 * @uml.property  name="a3"
	 */
	public void setA3(String a3) {
		this.a3 = a3;
	}

	/**
	 * @return
	 * @uml.property  name="number"
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number
	 * @uml.property  name="number"
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * @return
	 * @uml.property  name="idd"
	 */
	public String getIdd() {
		return idd;
	}

	/**
	 * @param idd
	 * @uml.property  name="idd"
	 */
	public void setIdd(String idd) {
		this.idd = idd;
	}

	/**
	 * @return
	 * @uml.property  name="enabled"
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled
	 * @uml.property  name="enabled"
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public int compareTo(I18nCountry o) {
		long pk = o.getCountryId();

		if (getCountryId() < pk) {
			return -1;
		} else if (getCountryId() > pk) {
			return 1;
		} else {
			return 0;
		}
	}

	public int hashCode() {
		return (int) getCountryId();
	}

	public Object clone() {
		I18nCountryModelImpl impl = new I18nCountryModelImpl();
		return impl;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{countryId=");
		sb.append(getCountryId());
		sb.append(", a2=");
		sb.append(getA2());
		sb.append(", a3=");
		sb.append(getA3());
		sb.append(", number=");
		sb.append(getNumber());
		sb.append(", idd=");
		sb.append(getIdd());
		sb.append(", enabled=");
		sb.append(isEnabled());
		sb.append("}");
		return sb.toString();
	}

	public Serializable getPrimaryKeyObject() {
		return getCountryId();
	}

	public void setPrimaryKeyObject(Serializable primaryKeyObj) {
		this.setCountryId((Long) primaryKeyObj );
	}

	public int getObjectType() {
		return 11;
	}

}
