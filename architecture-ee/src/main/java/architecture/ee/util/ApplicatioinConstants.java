package architecture.ee.util;

public interface ApplicatioinConstants extends architecture.common.lifecycle.ApplicationConstants {
	
	public static final String PLACEHOLDER_SQL_RESOURCE_LOCATION_KEY = "database.sql.location";
		
	/** SETUP PROPERTY KEY */	
	public static final String DEFAULT_SETUP_CONTEXT_FILE_LOCATION = "classpath:setupApplicationContext.xml";		
	
	public static final String SETUP_COMPLETE_PROP_NAME = "setup.complete";	
	
	public static final String LOCALE_LANGUAGE_PROP_NAME = "locale.language";	
	
	public static final String LOCALE_COUNTRY_PROP_NAME = "locale.country";	
	
	public static final String LOCALE_CHARACTER_ENCODING_PROP_NAME = "locale.characterEncoding";	
	
	public static final String LOCALE_TIMEZONE_PROP_NAME = "locale.timeZone";
	
	/** UI PROPERTY KEY */
	
	public static final String SKIN_USERS_CHOOSE_LOCALE_PROP_NAME = "skin.default.usersChooseLocale";
		
	/** USER PROPERTY KEY */
	public static final String USER_LOCALE_PROP_NAME = "userLocale";
	
	public static final String FREEMARKER_TEMPLATE_UPDATE_DELAY_PROP_NAME = "framework.freemarker.templateUpdateDelay";
	
	public static final String FREEMARKER_LOG_ERROR_PROP_NAME = "framework.freemarker.logError";
    
	public static final String FREEMARKER_STRONG_TEMPLATE_CACHE_SIZE_PROP_NAME = "framework.freemarker.strongTemplateCacheSize" ;
	
	public static final String FREEMARKER_WEAK_TEMPLATE_CACHE_SIZE_PROP_NAME = "framework.freemarker.weakTemplateCacheSize" ;
	
	public static final String SECURITY_AUTHENTICATION_ENCODING_SALT_PROP_NAME = "security.authentication.encoding.salt" ;

	public static final int USER = 1;
	
	
}
