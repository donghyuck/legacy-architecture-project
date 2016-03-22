/*
 * Copyright 2012 Donghyuck, Son
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package architecture.ee.util;

public interface ApplicationConstants extends architecture.common.lifecycle.ApplicationConstants {

    /** SETUP PROPERTY KEY */
    public static final String DEFAULT_SETUP_CONTEXT_FILE_LOCATION = "classpath:setupApplicationContext.xml";

    public static final String SETUP_COMPLETE_PROP_NAME = "setup.complete";
    public static final String BOOTSTRAP_CONTEXT_PROP_NAME = "bootstrap.context";

    /** USER PROPERTY KEY */
    public static final String USER_LOCALE_PROP_NAME = "userLocale";

    /** APPLICATION PROPERTY KEY */
    public static final String SKIN_USERS_CHOOSE_LOCALE_PROP_NAME = "skin.default.usersChooseLocale";

    public static final String FREEMARKER_TEMPLATE_UPDATE_DELAY_PROP_NAME = "framework.freemarker.templateUpdateDelay";
    public static final String FREEMARKER_LOG_ERROR_PROP_NAME = "framework.freemarker.logError";
    public static final String FREEMARKER_STRONG_TEMPLATE_CACHE_SIZE_PROP_NAME = "framework.freemarker.strongTemplateCacheSize";
    public static final String FREEMARKER_WEAK_TEMPLATE_CACHE_SIZE_PROP_NAME = "framework.freemarker.weakTemplateCacheSize";

    public static final String SECURITY_AUTHENTICATION_ENCODING_ALGORITHM_PROP_NAME = "security.authentication.encoding.algorithm";
    public static final String SECURITY_AUTHENTICATION_ENCODING_SALT_PROP_NAME = "security.authentication.encoding.salt";
    public static final String SECURITY_AUTHENTICATION_AUTHORITY_PROP_NAME = "security.authentication.authority";

    public static final String LOCALE_LANGUAGE_PROP_NAME = "locale.language";
    public static final String LOCALE_COUNTRY_PROP_NAME = "locale.country";
    public static final String LOCALE_CHARACTER_ENCODING_PROP_NAME = "locale.characterEncoding";
    public static final String LOCALE_TIMEZONE_PROP_NAME = "locale.timeZone";

    public static final String RESOURCE_SQL_LOCATION_PROP_NAME = "resource.sql.location";
    public static final String RESOURCE_TEMPLATE_LOCATION_PROP_NAME = "resource.template.location";

    public static final String SCRIPTING_GROOVY_SOURCE_LOCATION_PROP_NAME = "scripting.groovy.source.location";
    public static final String SCRIPTING_GROOVY_SOURCE_ENCODING_PROP_NAME = "scripting.groovy.source.encoding";
    public static final String SCRIPTING_GROOVY_SOURCE_RECOMPILE_PROP_NAME = "scripting.groovy.source.recompile";
    public static final String SCRIPTING_GROOVY_DEBUG_PROP_NAME = "scripting.groovy.debug";

    // setRecompileGroovySource
    public static final String RESOURCE_LICENSE_LOCATION_PROP_NAME = "resoruce.license.location";

    public static final int USER = 1;

    public static final int SYSTEM = 14;

    /** SESSION PROPERTY KEY */

}
