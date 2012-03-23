package architecture.common.adaptor;

public interface DataProcessor {

	public abstract Context getContext();
	
	public Object process(Object... args);
	
}
