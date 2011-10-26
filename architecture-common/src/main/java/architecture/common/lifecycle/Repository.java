package architecture.common.lifecycle;

public interface Repository {
	
	public ConfigRoot getConfigRoot();	
	
	/**
	 * 루트 경로를 리턴한다.
	 * 
	 * @return
	 */
	public String getRootURI();
	
	
}
