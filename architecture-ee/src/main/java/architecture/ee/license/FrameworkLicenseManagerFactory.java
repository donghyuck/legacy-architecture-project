package architecture.ee.license;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.common.license.LicenseManager;
import architecture.common.license.LicenseManagerFactory;
import architecture.common.util.L10NUtils;

/**
 * @author donghyuck
 */
public class FrameworkLicenseManagerFactory implements LicenseManagerFactory.Implementation {

    private static final Log log = LogFactory.getLog(FrameworkLicenseManagerFactory.class);

    private FrameworkLicenseManager instance = null;

    public LicenseManager getLicenseManager() {

	log.debug(L10NUtils.getMessage("003408"));

	if (instance == null) {
	    try {
		instance = new FrameworkLicenseManager();
		instance.initialize();
	    } catch (Throwable e) {
		log.error(e);
	    }
	}
	return instance;
    }

}
