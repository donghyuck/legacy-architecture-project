package architecture.common.lifecycle.service;

import architecture.common.exception.ComponentNotFoundException;
import architecture.common.lifecycle.Component;
import architecture.common.lifecycle.ConfigRoot;
import architecture.common.lifecycle.ConfigService;
import architecture.common.lifecycle.Repository;
import architecture.common.lifecycle.State;
import architecture.common.lifecycle.Version;

public interface AdminService extends Component {

    public abstract ConfigService getConfigService();
    
	public abstract Version getVersion();
	
	public abstract State getState();
	
	public abstract boolean isReady();
			
	public abstract <T> T getComponent(Class<T> requiredType) throws ComponentNotFoundException;
	
	public abstract void refresh();

}
