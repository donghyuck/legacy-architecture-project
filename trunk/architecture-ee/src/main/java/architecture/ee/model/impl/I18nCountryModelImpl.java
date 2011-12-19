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
	 */
	private long countryId;
	/**
	 */
	private String name;
	/**
	 */
	private String a2;
	/**
	 */
	private String a3;
	/**
	 */
	private String number;
	/**
	 */
	private String idd;
	/**
	 */
	private boolean enabled;

	/**
	 * @return
	 */
	public long getCountryId() {
		return countryId;
	}

	/**
	 * @param countryId
	 */
	public void setCountryId(long countryId) {
		this.countryId = countryId;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return
	 */
	public String getA2() {
		return a2;
	}

	/**
	 * @param a2
	 */
	public void setA2(String a2) {
		this.a2 = a2;
	}

	/**
	 * @return
	 */
	public String getA3() {
		return a3;
	}

	/**
	 * @param a3
	 */
	public void setA3(String a3) {
		this.a3 = a3;
	}

	/**
	 * @return
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * @return
	 */
	public String getIdd() {
		return idd;
	}

	/**
	 * @param idd
	 */
	public void setIdd(String idd) {
		this.idd = idd;
	}

	/**
	 * @return
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled
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
