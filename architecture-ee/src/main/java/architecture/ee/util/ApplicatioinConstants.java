package architecture.ee.util;

public interface ApplicatioinConstants extends architecture.common.lifecycle.ApplicationConstants {
			
		
	/** SETUP PROPERTY KEY */	
	public static final String DEFAULT_SETUP_CONTEXT_FILE_LOCATION = "classpath:setupApplicationContext.xml";		
	
	public static final String SETUP_COMPLETE_PROP_NAME = "setup.complete";	
	
	
	
	/** USER PROPERTY KEY */
	public static final String USER_LOCALE_PROP_NAME = "userLocale";
	
	
	
	
	/** APPLICATION PROPERTY KEY */	
	
	public static final String RESOURCE_SQL_LOCATION_PROP_NAME = "resource.sql.location";
	
	public static final String SKIN_USERS_CHOOSE_LOCALE_PROP_NAME = "skin.default.usersChooseLocale";
	
	
	
	public static final String FREEMARKER_TEMPLATE_UPDATE_DELAY_PROP_NAME = "framework.freemarker.templateUpdateDelay";
	
	public static final String FREEMARKER_LOG_ERROR_PROP_NAME = "framework.freemarker.logError";
    
	public static final String FREEMARKER_STRONG_TEMPLATE_CACHE_SIZE_PROP_NAME = "framework.freemarker.strongTemplateCacheSize" ;
	
	public static final String FREEMARKER_WEAK_TEMPLATE_CACHE_SIZE_PROP_NAME = "framework.freemarker.weakTemplateCacheSize" ;
	
	
	public static final String SECURITY_AUTHENTICATION_ENCODING_ALGORITHM_PROP_NAME = "security.authentication.encoding.algorithm" ;
	
	public static final String SECURITY_AUTHENTICATION_ENCODING_SALT_PROP_NAME = "security.authentication.encoding.salt" ;

	
	public static final String LOCALE_LANGUAGE_PROP_NAME = "locale.language";	
	
	public static final String LOCALE_COUNTRY_PROP_NAME = "locale.country";	
	
	public static final String LOCALE_CHARACTER_ENCODING_PROP_NAME = "locale.characterEncoding";	
	
	public static final String LOCALE_TIMEZONE_PROP_NAME = "locale.timeZone";
	
	
	public static final String DATABASE_DEFAULT_SQL_LOCATION_PROP_NAME = "database.default.sql.location";	
	
	
	//public static final String DATABASE_DEFAULT_SQL_LOCATION_PROP_NAME = "database.default.sql.location";	
	
	
	public static final int USER = 1;
	
	
}
