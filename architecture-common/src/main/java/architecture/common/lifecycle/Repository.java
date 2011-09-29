package architecture.common.lifecycle;

public interface Repository extends Component {
	
	/**
	 * 루트 경로를 리턴한다.
	 * 
	 * @return
	 */
	public abstract String getRootURI();
	
	/**
	 * 실재 사용하는 루트 경로를 리턴한다.
	 * @return
	 */
	public abstract String getEffectiveRootPath();
	
	/**
	 * ConfigRoot 객체를 리턴한다.
	 * @return
	 */
	public abstract ConfigRoot getConfigRoot();

}
