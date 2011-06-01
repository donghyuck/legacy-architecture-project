package architecture.ee.g11n;

public class I18nTextUtils {
	
    public static String generateResourceBundleKey(int objectType, long objectID, int objectAttribute)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(objectType).append(".").append(objectID).append(".").append(objectAttribute);
        return builder.toString();
    }
}
