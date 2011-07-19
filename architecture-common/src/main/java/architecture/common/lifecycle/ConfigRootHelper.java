package architecture.common.lifecycle;

import javax.servlet.ServletContext;


public interface ConfigRootHelper {

	public abstract String getRootURI();
	
	public abstract String getEffectiveRootPath();
	
	public ConfigRoot getConfigRoot();
	
	public void setServletContext(ServletContext servletContext);
	
}
