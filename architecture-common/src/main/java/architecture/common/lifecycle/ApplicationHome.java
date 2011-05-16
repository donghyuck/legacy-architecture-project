package architecture.common.lifecycle;

import java.io.File;

public interface ApplicationHome {

	public File getHome();
	
	public String getHomePath();
		
	public File getInstallRoot();
	
	public String getInstallRootPath();
	
	public String getLogsPath();
	
	public File getLogs();
		
}