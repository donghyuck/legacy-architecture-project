package architecture.common.lifecycle;

import javax.servlet.ServletContext;


public interface ConfigRootHelper {

	public abstract String getRootURI();
	
	public abstract String getInstallRootPath();
	
	public ConfigRoot getConfigRoot();
	
	public void setServletContext(ServletContext servletContext);
	
}
