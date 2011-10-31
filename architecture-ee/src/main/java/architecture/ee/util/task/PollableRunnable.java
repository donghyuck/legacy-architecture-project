package architecture.ee.util.task;


public interface PollableRunnable extends Runnable, Pollable {
	
	public abstract void cancel();

}
