package architecture.common.adaptor;

public interface Context {
	
	public static final String TYPE = "type";
	
	public static final String DATA = "data";
	
	public static final String BATCH = "batch";
	
	public static final String QUERY_NAME = "queryName";
	
	public static final String QUERY_STRING = "queryString";
	
	public enum Type {
		INPUT, 
		OUTPUT;
	};
	
	
	public String getConnectorName();
	
	public <T> T getObject(String name, Class<T> requiredType);
	
	public Object getObject(String name);
	
	public void setObject(String key, Object obj);
	
	public int size();

	public Type getType();
	
}
