package architecture.user.profile;



public class IntegerConverter extends SingleFieldMapper
    implements TypeConverter
{

    public IntegerConverter()
    {
    }

    public Integer convertFromString(String s)
        throws TypeConverter.ConversionException
    {
        try
        {
            return Integer.valueOf(Integer.parseInt(s));
        }
        catch(NumberFormatException nfe)
        {
            throw new TypeConverter.ConversionException(nfe);
        }
    }

    public String convertToString(Integer object)
        throws TypeConverter.ConversionException
    {
        return Integer.toString(object.intValue());
    }

    public boolean objectIsConvertable(Object object)
    {
        return object instanceof Integer;
    }

    public boolean objectIsInitialized(Integer object)
    {
        return object != null;
    }

    public boolean stringIsConvertable(String string)
    {
        if(string == null)
            return false;
        try
        {
            Integer.parseInt(string.trim());
        }
        catch(NumberFormatException e)
        {
            return false;
        }
        return true;
    }

    public String getValidationKey()
    {
        return "validators.number_error.text";
    }

	public String convertToString(Object obj) throws ConversionException {
		return convertToString((Integer) obj);
	}

	public boolean objectIsInitialized(Object obj) {
		return objectIsInitialized((Integer)obj);
	}


}
