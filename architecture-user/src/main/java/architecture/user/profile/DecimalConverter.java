package architecture.user.profile;



public class DecimalConverter extends SingleFieldMapper
    implements TypeConverter
{

    public DecimalConverter()
    {
    }

    public Double convertFromString(String s)
        throws TypeConverter.ConversionException
    {
        try
        {
            return Double.valueOf(Double.parseDouble(s));
        }
        catch(NumberFormatException nfe)
        {
            throw new TypeConverter.ConversionException(nfe);
        }
    }

    public String convertToString(Double object)
        throws TypeConverter.ConversionException
    {
        return Double.toString(object.doubleValue());
    }

    public boolean objectIsConvertable(Object object)
    {
        return object instanceof Double;
    }

    public boolean objectIsInitialized(Double object)
    {
        return object != null;
    }

    public boolean stringIsConvertable(String string)
    {
        if(string == null)
            return false;
        try
        {
            Double.parseDouble(string.trim());
        }
        catch(NumberFormatException e)
        {
            return false;
        }
        return true;
    }

    public String getValidationKey()
    {
        return "validators.decimal_error.text";
    }

    public boolean objectIsInitialized(Object obj)
    {
        return objectIsInitialized((Double)obj);
    }

    public String convertToString(Object obj)
        throws TypeConverter.ConversionException
    {
        return convertToString((Double)obj);
    }

}
