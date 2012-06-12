package architecture.user.profile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter extends SingleFieldMapper implements TypeConverter {

	public DateConverter() {
	}

	public Date convertFromString(String s)
			throws TypeConverter.ConversionException {
		try {
			return new Date(Long.parseLong(s));
		} catch (NumberFormatException e) {
			throw new TypeConverter.ConversionException((new StringBuilder()).append("Unable to parse date with form: ").append(s).toString());
		}
	}

	public String convertToString(Date object)
			throws TypeConverter.ConversionException {
		return Long.toString(object.getTime());
	}

	public boolean objectIsConvertable(Object object) {
		return object instanceof Date;
	}

	public boolean stringIsConvertable(String string) {
		if (string == null)
			return false;
		try {
			(new SimpleDateFormat("yyyy/MM/dd")).parse(string.trim());
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	public boolean objectIsInitialized(Date object) {
		return object != null && object.getTime() != 0L;
	}

	public String getValidationKey() {
		return "validators.date_error.text";
	}

	public String convertToString(Object obj) throws ConversionException {
		return convertToString((Date) obj);
	}

	public boolean objectIsInitialized(Object obj) {
		return objectIsConvertable((Date)obj);
	}

}