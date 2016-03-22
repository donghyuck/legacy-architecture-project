package architecture.user.profile;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

import architecture.common.util.StringUtils;

public class URLConverter extends SingleFieldMapper implements TypeConverter {

    private static final Pattern INVALID_URL_PATTERN = Pattern.compile("[^\\w\\d@/.;:?&=%!*'()$,_+~-]");

    public URLConverter() {
    }

    public URL convertFromString(String s) throws TypeConverter.ConversionException {
	try {
	    return new URL(s);
	} catch (MalformedURLException e) {
	    throw new TypeConverter.ConversionException(e);
	}
    }

    public String convertToString(URL object) throws TypeConverter.ConversionException {
	return object.toExternalForm();
    }

    public boolean objectIsConvertable(Object object) {
	return object instanceof URL;
    }

    public boolean objectIsInitialized(URL object) {
	return object != null;
    }

    public boolean stringIsConvertable(String string) {
	if (string == null)
	    return false;
	if (INVALID_URL_PATTERN.matcher(string).find())
	    return false;
	else
	    return StringUtils.verifyUrl(string.trim());
    }

    public String getValidationKey() {
	return "validators.url_fmt_error.text";
    }

    public String convertToString(Object obj) throws ConversionException {
	return convertToString((URL) obj);
    }

    public boolean objectIsInitialized(Object obj) {
	return objectIsInitialized((URL) obj);
    }

}
