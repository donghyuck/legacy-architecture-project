package architecture.user.profile;



public class PhoneNumberConverter extends BeanPropsExternalMapper
    implements TypeConverter
{

    public PhoneNumberConverter()
    {
        super(PhoneNumber.class);
    }

    public PhoneNumber convertFromString(String s)
        throws TypeConverter.ConversionException
    {
        String tokens[] = s.split("\\|");
        if(tokens.length == 2)
            try
            {
                PhoneNumber.Type type = PhoneNumber.Type.fromName(tokens[1]);
                return new PhoneNumber(ProfileFieldUtil.unEscapeFieldDelimiters(tokens[0]), type);
            }
            catch(IllegalArgumentException iae)
            {
                throw new TypeConverter.ConversionException(iae);
            }
        if(tokens.length == 1)
        {
            PhoneNumber ph = new PhoneNumber();
            ph.setPhoneNumber(ProfileFieldUtil.unEscapeFieldDelimiters(tokens[0]));
            return ph;
        } else
        {
            throw new TypeConverter.ConversionException((new StringBuilder()).append("Unable to convert '").append(s).append("' into a phone number").toString());
        }
    }

    public String convertToString(PhoneNumber object)
        throws TypeConverter.ConversionException
    {
        if(!objectIsInitialized(object))
            return null;
        StringBuilder builder = new StringBuilder(ProfileFieldUtil.escapeFieldDelimiters(object.getPhoneNumber()));
        if(object.getType() != null)
        {
            builder.append("|");
            builder.append(object.getType().getName());
        }
        return builder.toString();
    }

    public boolean objectIsConvertable(Object object)
    {
        return object instanceof PhoneNumber;
    }

    public boolean objectIsInitialized(PhoneNumber ph)
    {
        return ph.getPhoneNumber() != null && ph.getPhoneNumber().length() > 0;
    }

    public boolean stringIsConvertable(String string)
    {
        if(string == null)
            return false;
        string = string.trim();
        int digitCount = 0;
        for(int i = 0; i < string.length(); i++)
        {
            char phoneChar = string.charAt(i);
            if(phoneChar >= '0' && phoneChar <= '9')
            {
                digitCount++;
                continue;
            }
            if(phoneChar != '-' && phoneChar != ' ' && phoneChar != '(' && phoneChar != ')' && phoneChar != '+')
                return false;
        }

        return digitCount >= 6;
    }

    public String getValidationKey()
    {
        return "validators.phone_fmt_error.text";
    }

	public String convertToString(Object obj) throws ConversionException {
		return convertToString((PhoneNumber)obj);
	}

	public boolean objectIsInitialized(Object obj) {
		return objectIsInitialized((PhoneNumber)obj);
	}

}