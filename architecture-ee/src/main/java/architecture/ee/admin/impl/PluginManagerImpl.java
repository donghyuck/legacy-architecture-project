package architecture.ee.admin.impl;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.common.event.api.EventPublisher;
import architecture.common.event.api.EventSource;
import architecture.common.plugin.PluginManager;
import architecture.common.util.TaskEngine;


/**
 * @author  donghyuck
 */
public class PluginManagerImpl implements PluginManager, EventSource {
	
	private static final Log Log = LogFactory.getLog(PluginManagerImpl.class);
		
	protected File pluginDirectory ;
	protected DataSource dataSource;
	
    /**
	 */
    private EventPublisher eventPublisher ;
    private AtomicBoolean initialized;
    /**
	 */
    private TaskEngine taskEngine;
	
    public PluginManagerImpl() { 
    	this.initialized = new AtomicBoolean(false);
    	this.eventPublisher = null;
    	this.taskEngine = null;
    	this.pluginDirectory = null;
    }

	public void initialize(){
    	if(!initialized.get()){    		
    		taskEngine.submit(new Runnable() {
				public void run() {
					
				}
    			
    		});    		
    		initialized.set(true);
    	}
    	// 컨텍스트가 로그 되면 동적으로 로드한다.
    	// 데이터베이스에 정보를 기반으로 로드한다.
    }
      
    public boolean isInitialized(){
    	return initialized.get();    	
    }

    /**
	 * @param eventPublisher
	 */
    public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

    /**
	 * @param taskEngine
	 */
    public void setTaskEngine(TaskEngine taskEngine) {
		this.taskEngine = taskEngine;
	}

    /**
	 * @param dataSource
	 */
    public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}