package architecture.user.profile;

import architecture.common.util.StringUtils;

public class ProfileFieldUtil {

    private static final String DELIMITERS = ",:|";
    private static final String PIPE = "&#124;";
    private static final String COMMA = "&#44;";
    private static final String COLON = "&#58;";

    public ProfileFieldUtil() {
    }

    public static String escapeFieldDelimiters(String field) {
	field = StringUtils.replace(field, "|", PIPE);
	field = StringUtils.replace(field, ",", COMMA);
	field = StringUtils.replace(field, ":", COLON);
	return field;
    }

    public static String unEscapeFieldDelimiters(String field) {
	field = StringUtils.replace(field, PIPE, "|");
	field = StringUtils.replace(field, COMMA, ",");
	field = StringUtils.replace(field, COLON, ":");
	return field;
    }

}
