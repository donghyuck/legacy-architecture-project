package architecture.ee.model.impl;

import java.io.Serializable;

import architecture.ee.i18n.I18nCountry;
import architecture.ee.model.I18nCountryModel;

public class I18nCountryModelImpl extends BaseModelObject<I18nCountry>
		implements I18nCountryModel, I18nCountry {
	
	private long countryId;
	private String name;
	private String a2;
	private String a3;
	private String number;
	private String idd;
	private boolean enabled;

	public long getCountryId() {
		return countryId;
	}

	public void setCountryId(long countryId) {
		this.countryId = countryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getA2() {
		return a2;
	}

	public void setA2(String a2) {
		this.a2 = a2;
	}

	public String getA3() {
		return a3;
	}

	public void setA3(String a3) {
		this.a3 = a3;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getIdd() {
		return idd;
	}

	public void setIdd(String idd) {
		this.idd = idd;
	}

	public boolean isEnabled() {
		return enabled;
	}

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
