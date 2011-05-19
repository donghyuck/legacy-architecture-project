package architecture.common.lifecycle;

public interface Server extends Application {
	
    public abstract ConfigRoot getConfigRoot();
    
    public abstract String getInstallRootPath();
    
    public abstract String getRootURI();
    
}