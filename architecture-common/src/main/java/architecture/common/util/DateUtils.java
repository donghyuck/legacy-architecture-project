package architecture.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    private static final SimpleDateFormat ISO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private static final TimeZone UTC_TIMEZONE = TimeZone.getTimeZone("UTC");

    public static Date clone(Date date) {
	return date != null ? new Date(date.getTime()) : null;
    }

    public static Date parseISODate(String str) throws ParseException {
	if (ISO_DATE_FORMAT.getTimeZone() != UTC_TIMEZONE) {
	    ISO_DATE_FORMAT.setTimeZone(UTC_TIMEZONE);
	}
	return ISO_DATE_FORMAT.parse(str);
    }

}
