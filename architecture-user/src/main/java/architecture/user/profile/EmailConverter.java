package architecture.user.profile;

import architecture.common.util.StringUtils;

public class EmailConverter extends BeanPropsExternalMapper implements
		TypeConverter {

	public EmailConverter() {
		super(EmailAddress.class);
	}

	
	public EmailConverter(Class provileValueObjectClass) {
		super(provileValueObjectClass);
	}

	public EmailAddress convertFromString(String s)
			throws TypeConverter.ConversionException {
		String tokens[] = s.split("\\|");
		if (tokens.length == 2)
			try {
				EmailAddress.Type type = EmailAddress.Type.fromName(tokens[1]);
				return new EmailAddress(ProfileFieldUtil.unEscapeFieldDelimiters(tokens[0]), type);
			} catch (IllegalArgumentException iae) {
				throw new TypeConverter.ConversionException(iae);
			}
		if (tokens.length == 1) {
			EmailAddress email = new EmailAddress();
			email.setEmail(ProfileFieldUtil.unEscapeFieldDelimiters(tokens[0]));
			return email;
		} else {
			throw new TypeConverter.ConversionException((new StringBuilder())
					.append("Unable to convert '").append(s)
					.append("' into an email address").toString());
		}
	}

	public String convertToString(EmailAddress object)
			throws TypeConverter.ConversionException {
		if (!objectIsInitialized(object))
			return null;

		StringBuilder builder = new StringBuilder(ProfileFieldUtil.escapeFieldDelimiters(object.getEmail()));
		if (object.getType() != null) {
			builder.append("|");
			builder.append(object.getType().getName());
		}
		return builder.toString();
	}

	public boolean objectIsConvertable(Object object) {
		return object instanceof EmailAddress;
	}

	public boolean objectIsInitialized(EmailAddress address) {
		return address.getEmail() != null && address.getEmail().length() > 0;
	}

	public boolean stringIsConvertable(String string) {
		if (string == null)
			return false;
		else
			return StringUtils.isValidEmailAddress(string.trim());
	}

	public String getValidationKey() {
		return "validators.email_error.text";
	}

	public boolean objectIsInitialized(Object obj) {
		return objectIsInitialized((EmailAddress) obj);
	}

	public String convertToString(Object obj)
			throws TypeConverter.ConversionException {
		return convertToString((EmailAddress) obj);
	}

}
