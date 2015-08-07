package architecture.ee.spring.task;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.common.event.spi.EventDispatcher;
import architecture.common.task.TaskEngine;

public class TaskEngineBootstrap {
	
	private Log log = LogFactory.getLog(TaskEngineBootstrap.class);
	private TaskEngine taskEngine;
	private List<TaskDefinitionBean> tasks;
	private EventDispatcher eventDispatcher;

	public TaskEngineBootstrap(TaskEngine taskEngine) {
		this.taskEngine = taskEngine;
	}

	public List<TaskDefinitionBean> getTasks() {
		return tasks;
	}

	public void setTasks(List<TaskDefinitionBean> tasks) {
		this.tasks = tasks;
	}

	public EventDispatcher getEventDispatcher() {
		return eventDispatcher;
	}

	public void setEventDispatcher(EventDispatcher eventDispatcher) {
		this.eventDispatcher = eventDispatcher;
	}
	
	public void initialize() throws Exception {
		for( TaskDefinitionBean task : tasks ){
			
		}
		
	}
	
	
	public boolean hasStateOtherTheRunning(TaskDefinitionBean taskDef){
		if( !(taskDef instanceof SimpleTaskDefinitionBean))
			return false;
		List states = ((SimpleTaskDefinitionBean)taskDef).getValidApplicationState();
		if(states == null || states.size() == 0)
			return false;
		else
			return states.size() != 1 || ((ApplicationState)states.get(0)).equals(ApplicationState.RUNNING);
		
	}
	
}
