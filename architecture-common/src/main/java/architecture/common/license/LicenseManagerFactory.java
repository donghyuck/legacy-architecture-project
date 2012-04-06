package architecture.common.license;

import architecture.common.util.ImplFactory;

/**
 * @author  donghyuck
 */
public class LicenseManagerFactory {

	public static interface Implementation {
		public abstract LicenseManager getLicenseManager();
	}

	/**
	 * @uml.property  name="impl"
	 * @uml.associationEnd  
	 */
	private static Implementation impl = null;

	static {
		impl = (Implementation) ImplFactory.loadImplFromKey(LicenseManagerFactory.Implementation.class);
	}

	public static LicenseManager getLicenseManager() {
		return impl.getLicenseManager();
	}
}
