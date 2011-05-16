package architecture.common.lifecycle;

public interface Server extends Application {
	
	public Version getVersion();
	
	public String getInstallRootPath();
	
	public String getHomePath();
	
}