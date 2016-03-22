package architecture.user.profile;

import java.util.StringTokenizer;

import architecture.common.util.StringUtils;

public class AddressConverter extends BeanPropsExternalMapper implements TypeConverter {

    private static final String STREET_1 = "street1";
    private static final String STREET_2 = "street2";
    private static final String CITY = "city";
    private static final String STATE = "state";
    private static final String COUNTRY = "country";
    private static final String ZIP = "zip";
    private static final String TYPE = "type";

    public AddressConverter() {
	super(Address.class);
    }

    public Address convertFromString(String s) throws TypeConverter.ConversionException {
	Address address = new Address();
	StringTokenizer tokenizer = new StringTokenizer(s, ",");
	if (tokenizer.countTokens() == 1) {
	    String singleToken = tokenizer.nextToken();
	    if (singleToken.indexOf(":") == -1)
		address.setStreet1(ProfileFieldUtil.unEscapeFieldDelimiters(singleToken));
	    else
		setField(address, singleToken);
	} else {
	    do {
		if (!tokenizer.hasMoreTokens())
		    break;
		String nextToken = tokenizer.nextToken();
		String labelAndVal[] = nextToken.split(":");
		if (labelAndVal.length == 2 && !StringUtils.isEmpty(labelAndVal[0])
			&& !StringUtils.isEmpty(labelAndVal[1]))
		    setField(address, nextToken);
	    } while (true);
	}
	return address;
    }

    public String convertToString(Address object) throws TypeConverter.ConversionException {
	if (!objectIsInitialized(object))
	    return null;
	StringBuilder builder = new StringBuilder();
	if (object.getStreet1() != null)
	    addField(builder, STREET_1, object.getStreet1());
	if (object.getStreet2() != null)
	    addField(builder, STREET_2, object.getStreet2());
	if (object.getCity() != null)
	    addField(builder, CITY, object.getCity());
	if (object.getStateOrProvince() != null)
	    addField(builder, STATE, object.getStateOrProvince());
	if (object.getCountry() != null)
	    addField(builder, COUNTRY, object.getCountry());
	if (object.getPostalCode() != null)
	    addField(builder, ZIP, object.getPostalCode());
	if (object.getType() != null)
	    addField(builder, TYPE, object.getType().name());
	return builder.toString();
    }

    public boolean objectIsInitialized(Address address) {
	return address.getStreet1() != null || address.getStreet2() != null || address.getCity() != null
		|| address.getStateOrProvince() != null || address.getPostalCode() != null
		|| address.getCountry() != null;
    }

    public boolean objectIsConvertable(Object object) {
	return object instanceof Address;
    }

    private void addField(StringBuilder builder, String fieldName, String fieldVal) {
	fieldVal = ProfileFieldUtil.escapeFieldDelimiters(fieldVal);
	if (builder.length() > 0)
	    builder.append(",");
	builder.append(fieldName).append(":").append(fieldVal);
    }

    public void setField(Address address, String field) {
	String labelAndVal[] = field.split(":");
	if (labelAndVal.length != 2)
	    throw new IllegalArgumentException(
		    (new StringBuilder()).append(field).append(" is not of the form label:value").toString());
	String label = labelAndVal[0];
	String value = labelAndVal[1];
	value = ProfileFieldUtil.unEscapeFieldDelimiters(value);
	if (STREET_1.equals(label))
	    address.setStreet1(value);
	else if (STREET_2.equals(label))
	    address.setStreet2(value);
	else if (CITY.equals(label))
	    address.setCity(value);
	else if (STATE.equals(label))
	    address.setStateOrProvince(value);
	else if (COUNTRY.equals(label))
	    address.setCountry(value);
	else if (ZIP.equals(label))
	    address.setPostalCode(value);
	else if (TYPE.equals(label))
	    address.setType(Address.Type.valueOf(value));
    }

    public boolean stringIsConvertable(String string) {
	return true;
    }

    public String getValidationKey() {
	return "validators.address_fmt_err.text";
    }

    public static boolean anyConbinedFieldsSet(String combinedFieldValue) {
	String params[] = combinedFieldValue.split(",");
	for (String param : params) {
	    if (!param.contains(":"))
		continue;
	    String labelValue[] = param.split(":");
	    if (labelValue.length == 2 && !"type".equals(labelValue[0]) && !StringUtils.isEmpty(labelValue[1]))
		return true;
	}

	return false;
    }

    public static boolean allCombinedFieldsSet(String combinedFieldValue) {
	String params[] = combinedFieldValue.split(",");
	for (String param : params) {
	    if (param.contains(":") && param.split(":").length != 2)
		return false;
	}
	return true;
    }

    public String convertToString(Object obj) throws ConversionException {
	return convertToString((Address) obj);
    }

    public boolean objectIsInitialized(Object obj) {
	return objectIsInitialized((Address) obj);
    }

}
