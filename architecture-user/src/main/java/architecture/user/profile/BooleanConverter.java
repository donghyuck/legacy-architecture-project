package architecture.user.profile;


public class BooleanConverter extends SingleFieldMapper
    implements TypeConverter
{

    public BooleanConverter()
    {
    }

    public Boolean convertFromString(String s)
        throws TypeConverter.ConversionException
    {
        return Boolean.valueOf(s);
    }

    public String convertToString(Boolean object)
        throws TypeConverter.ConversionException
    {
        return String.valueOf(object);
    }

    public boolean objectIsConvertable(Object object)
    {
        return object instanceof Boolean;
    }

    public boolean stringIsConvertable(String string)
    {
        return true;
    }

    public boolean objectIsInitialized(Boolean object)
    {
        return object != null;
    }

    public String getValidationKey()
    {
        return "";
    }

	public String convertToString(Object obj) throws ConversionException {
		return convertToString((Boolean)obj);
	}

	public boolean objectIsInitialized(Object obj) {
		return objectIsInitialized((Boolean)obj);
	}

   
}
