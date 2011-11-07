package architecture.common.lifecycle;

public interface Repository extends Component {
	
	public ConfigRoot getConfigRoot();	
	
	/**
	 * 루트 경로를 리턴한다.
	 * 
	 * @return
	 */
	public String getRootURI();
	
	public String getEffectiveRootPath();
	
	public ApplicationProperties getSetupApplicationProperties();
	
}
