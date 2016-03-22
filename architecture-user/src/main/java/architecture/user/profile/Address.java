package architecture.user.profile;

/**
 * @author donghyuck
 */
public class Address {
    /**
     * @author donghyuck
     */
    enum Type {

	/**
	 */
	WORK("work"),
	/**
	*/
	HOME("home"),
	/**
	*/
	PO_BOX("po_box"),
	/**
	*/
	OTHER("other");
	/**
	 */
	private String name;

	private Type(String name) {
	    this.name = name;
	}

	/**
	 * @return
	 */
	public String getName() {
	    return name;
	}

	public static Type fromName(String name) {
	    if (HOME.getName().equals(name))
		return HOME;
	    if (WORK.getName().equals(name))
		return WORK;
	    if (PO_BOX.getName().equals(name))
		return PO_BOX;
	    if (OTHER.getName().equals(name))
		return OTHER;
	    else
		throw new IllegalArgumentException(
			(new StringBuilder()).append("Unknown address typename: ").append(name).toString());
	}
    }

    /**
     */
    private String street1;
    /**
     */
    private String street2;
    /**
     */
    private String city;
    /**
     */
    private String stateOrProvince;
    /**
     */
    private String country;
    /**
     */
    private String postalCode;
    /**
     */
    private Type type;

    public Address() {
    }

    /**
     * @return
     */
    public String getStreet1() {
	return street1;
    }

    /**
     * @param street1
     */
    public void setStreet1(String street1) {
	this.street1 = street1;
    }

    /**
     * @return
     */
    public String getStreet2() {
	return street2;
    }

    /**
     * @param street2
     */
    public void setStreet2(String street2) {
	this.street2 = street2;
    }

    /**
     * @return
     */
    public String getCountry() {
	return country;
    }

    /**
     * @param country
     */
    public void setCountry(String country) {
	this.country = country;
    }

    /**
     * @return
     */
    public String getCity() {
	return city;
    }

    /**
     * @param city
     */
    public void setCity(String city) {
	this.city = city;
    }

    /**
     * @return
     */
    public String getStateOrProvince() {
	return stateOrProvince;
    }

    /**
     * @param stateOrProvince
     */
    public void setStateOrProvince(String stateOrProvince) {
	this.stateOrProvince = stateOrProvince;
    }

    /**
     * @return
     */
    public String getPostalCode() {
	return postalCode;
    }

    /**
     * @param postalCode
     */
    public void setPostalCode(String postalCode) {
	this.postalCode = postalCode;
    }

    /**
     * @return
     */
    public Type getType() {
	return type;
    }

    /**
     * @param type
     */
    public void setType(Type type) {
	this.type = type;
    }

    public String getTypeString() {
	return type == null ? null : type.getName();
    }

    public void setTypeString(String typeName) {
	type = Type.fromName(typeName);
    }

    public boolean equals(Object o) {
	if (this == o)
	    return true;
	if (o == null || getClass() != o.getClass())
	    return false;
	Address address = (Address) o;
	if (city == null ? address.city != null : !city.equals(address.city))
	    return false;
	if (country == null ? address.country != null : !country.equals(address.country))
	    return false;
	if (postalCode == null ? address.postalCode != null : !postalCode.equals(address.postalCode))
	    return false;
	if (stateOrProvince == null ? address.stateOrProvince != null
		: !stateOrProvince.equals(address.stateOrProvince))
	    return false;
	if (street1 == null ? address.street1 != null : !street1.equals(address.street1))
	    return false;
	if (street2 == null ? address.street2 != null : !street2.equals(address.street2))
	    return false;
	return type == address.type;
    }

    public int hashCode() {
	int result = street1 == null ? 0 : street1.hashCode();
	result = 31 * result + (street2 == null ? 0 : street2.hashCode());
	result = 31 * result + (city == null ? 0 : city.hashCode());
	result = 31 * result + (stateOrProvince == null ? 0 : stateOrProvince.hashCode());
	result = 31 * result + (country == null ? 0 : country.hashCode());
	result = 31 * result + (postalCode == null ? 0 : postalCode.hashCode());
	result = 31 * result + (type == null ? 0 : type.hashCode());
	return result;
    }

}
