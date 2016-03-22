
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
package architecture.ee.component.admin;

import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.task.AsyncTaskExecutor;

import architecture.common.event.api.EventPublisher;
import architecture.common.license.License;
import architecture.common.license.LicenseManager;
import architecture.common.lifecycle.ConfigRoot;
import architecture.common.lifecycle.ConfigService;
import architecture.common.lifecycle.Repository;
import architecture.common.lifecycle.State;
import architecture.common.lifecycle.Version;
import architecture.common.lifecycle.bootstrap.Bootstrap;
import architecture.common.lifecycle.service.AdminService;
import architecture.ee.spring.lifecycle.SpringAdminService;
import architecture.ee.util.ApplicationConstants;
import net.sf.ehcache.Cache;

/**
 * 
 * @author donghyuck son
 *
 */
public final class AdminHelper {

    private static final Log log = LogFactory.getLog(AdminHelper.class);

    public static Repository getRepository() {
	return Bootstrap.getBootstrapComponent(Repository.class);
    }

    public static License getLicense() {
	LicenseManager licenseManager = Bootstrap.getBootstrapComponent(LicenseManager.class);
	return licenseManager.getLicense();
    }

    public static boolean isReady() {
	return getAdminService().isReady();
    }

    public static State getState() {
	return getAdminService().getState();
    }

    public static Version getVersion() {
	return getAdminService().getVersion();
    }

    public static EventPublisher getEventPublisher() {
	return Bootstrap.getBootstrapComponent(EventPublisher.class);
    }

    public static boolean isSetupComplete() {
	Boolean isSetupComplete = getAdminService().getConfigService()
		.getLocalProperty(ApplicationConstants.SETUP_COMPLETE_PROP_NAME, false);
	if (isReady()) {
	    return getAdminService().getConfigService()
		    .getApplicationBooleanProperty(ApplicationConstants.SETUP_COMPLETE_PROP_NAME, isSetupComplete);
	}
	return isSetupComplete;
    }

    public static String[] getComponentNames() {
	SpringAdminService adminService = (SpringAdminService) getAdminService();
	if (isReady()) {
	    return adminService.getApplicationContext().getBeanDefinitionNames();
	} else {
	    return Bootstrap.getBootstrapComponentNames();
	}
    }

    public static AsyncTaskExecutor getAsyncTaskExecutor() {
	return Bootstrap.getBootstrapComponent(AsyncTaskExecutor.class);
    }

    public static AdminService getAdminService() {
	return Bootstrap.getBootstrapComponent(AdminService.class);
    }

    public static LicenseManager getLicenseManager() {
	return Bootstrap.getBootstrapComponent(LicenseManager.class);
    }

    public static ConfigService getConfigService() {
	return Bootstrap.getBootstrapComponent(ConfigService.class);
    }

    public static ConfigRoot getConfigRoot() {
	return getRepository().getConfigRoot();
    }

    public static String getEffectiveRootPath() {
	return getRepository().getEffectiveRootPath();
    }

    public static groovy.lang.GroovyClassLoader getGroovyClassLoader() {
	return Bootstrap.getBootstrapComponent(groovy.lang.GroovyClassLoader.class);
    }

    public static net.sf.ehcache.CacheManager getCacheManager() {
	return Bootstrap.getBootstrapComponent(net.sf.ehcache.CacheManager.class);
    }

    public static net.sf.ehcache.Cache getCache(String name) {
	return getCache(name, true);
    }

    public static net.sf.ehcache.Cache getCache(String name, boolean createNotExist) {

	if (!getCacheManager().cacheExists(name) && createNotExist) {

	    int maxElementsInMemory = 5000;
	    boolean overflowToDisk = false;
	    boolean eternal = false;
	    long timeToLiveSeconds = 60;
	    long timeToIdleSeconds = 30;
	    boolean diskPersistent = false;
	    long diskExpiryThreadIntervalSeconds = 0;

	    Cache memoryOnlyCache = new Cache(name, maxElementsInMemory, overflowToDisk, eternal, timeToLiveSeconds,
		    timeToIdleSeconds, diskPersistent, diskExpiryThreadIntervalSeconds);
	    getCacheManager().addCache(memoryOnlyCache);
	} else {
	    return null;
	}
	return getCacheManager().getCache(name);
    }

    public static String getLocalizedMessage(String code, Object[] args, Locale locale) {
	Locale localeToUse = locale;
	if (localeToUse == null)
	    localeToUse = getConfigService().getLocale();
	SpringAdminService adminService = (SpringAdminService) getAdminService();

	if (isReady()) {
	    return adminService.getApplicationContext().getMessage(code, args, localeToUse);
	} else {
	    return Bootstrap.getBootstrapApplicationContext().getMessage(code, args, localeToUse);
	}
    }
}