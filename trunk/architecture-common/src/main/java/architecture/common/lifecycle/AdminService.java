package architecture.common.lifecycle;

import architecture.common.exception.ComponentNotFoundException;

public interface AdminService extends Component {

    public abstract ConfigRoot getConfigRoot();
    
    public abstract Repository getRepository();
    
    public abstract ConfigService getConfigService();
    
	public abstract Version getVersion();
	
	public abstract State getState();
	
	public abstract boolean isReady();
			
	public abstract <T> T getComponent(Class<T> requiredType) throws ComponentNotFoundException;
	
	public abstract void refresh();	

}
