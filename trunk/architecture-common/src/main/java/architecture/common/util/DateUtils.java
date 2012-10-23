package architecture.common.util;

import java.util.Date;

public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    public static Date clone(Date date)
    {
        return date != null ? new Date(date.getTime()) : null;
    }
    
}
