package architecture.common.license;

import architecture.common.util.ImplFactory;

public class LicenseManagerFactory {

	public static interface Implementation {
		public abstract LicenseManager getLicenseManager();
	}

	private static Implementation impl = null;

	static {
		impl = (Implementation) ImplFactory.loadImplFromKey(LicenseManagerFactory.Implementation.class);
	}

	public static LicenseManager getLicenseManager() {
		return impl.getLicenseManager();
	}
}
