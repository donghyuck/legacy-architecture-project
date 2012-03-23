package architecture.common.adaptor;


public interface ReadConnector extends Connector{

	public abstract Object pull(Context context);
	
}
