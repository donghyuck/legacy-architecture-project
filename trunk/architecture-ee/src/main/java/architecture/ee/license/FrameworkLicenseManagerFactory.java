package architecture.ee.license;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.common.license.LicenseManager;
import architecture.common.license.LicenseManagerFactory;

/**
 * @author  donghyuck
 */
public class FrameworkLicenseManagerFactory implements LicenseManagerFactory.Implementation {
	
	private static final Log log = LogFactory.getLog(FrameworkLicenseManagerFactory.class);
	
	/**
	 * @uml.property  name="instance"
	 * @uml.associationEnd  
	 */
	private FrameworkLicenseManager instance = null ;
	
	public LicenseManager getLicenseManager() {
		
		log.debug( "loading license manager " );
		
		if( instance == null ){
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
