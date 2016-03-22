package architecture.user.profile;

/**
 * @author donghyuck
 */
public class PhoneNumber {

    /**
     * @author donghyuck
     */
    enum Type {
	/**
	 */
	HOME("home"),
	/**
	 */
	WORK("work"),
	/**
	 */
	FAX("fax"),
	/**
	 */
	MOBILE("mobile"),
	/**
	 */
	PAGER("pager"),
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
	    if (FAX.getName().equals(name))
		return FAX;
	    if (MOBILE.getName().equals(name))
		return MOBILE;
	    if (PAGER.getName().equals(name))
		return PAGER;
	    if (OTHER.getName().equals(name))
		return OTHER;
	    else
		throw new IllegalArgumentException(
			(new StringBuilder()).append("Unknown phone number typename: ").append(name).toString());
	}

	public String toString() {
	    return getName();
	}
    }

    /**
     */
    private String phoneNumber;
    /**
     */
    private Type type;

    public PhoneNumber() {
    }

    public PhoneNumber(String phoneNumber, Type type) {
	this.phoneNumber = phoneNumber;
	this.type = type;
    }

    /**
     * @param phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
	this.phoneNumber = phoneNumber;
    }

    /**
     * @return
     */
    public String getPhoneNumber() {
	return phoneNumber;
    }

    public void setTypeString(String typeName) {
	type = Type.fromName(typeName);
    }

    public String getTypeString() {
	if (type != null)
	    return type.getName();
	else
	    return null;
    }

    /**
     * @param type
     */
    public void setType(Type type) {
	this.type = type;
    }

    /**
     * @return
     */
    public Type getType() {
	return type;
    }

    public boolean equals(Object o) {
	if (this == o)
	    return true;
	if (o == null || getClass() != o.getClass())
	    return false;
	PhoneNumber that = (PhoneNumber) o;
	if (phoneNumber == null ? that.phoneNumber != null : !phoneNumber.equals(that.phoneNumber))
	    return false;
	return type == that.type;
    }

    public int hashCode() {
	int result = phoneNumber == null ? 0 : phoneNumber.hashCode();
	result = 31 * result + (type == null ? 0 : type.hashCode());
	return result;
    }
}
