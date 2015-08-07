package architecture.ee.spring.task;

public interface TaskDefinitionBean {

	public abstract Runnable getTask();
	
	public abstract int getPriority();
	
}
