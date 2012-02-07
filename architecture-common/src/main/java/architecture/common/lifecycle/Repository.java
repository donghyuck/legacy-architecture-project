package architecture.common.lifecycle;

import java.io.File;

public interface Repository extends Component {
	
	public abstract ConfigRoot getConfigRoot();	
	
	/**
	 * 루트 경로를 리턴한다.
	 * 
	 * @return
	 */
	public abstract String getRootURI();
	
	public abstract String getURI(String name) ;
	
	public abstract File getFile(String name);
	
	public abstract String getEffectiveRootPath();
	
	public abstract ApplicationProperties getSetupApplicationProperties();
	
	public abstract File getLicenseFile();
}
