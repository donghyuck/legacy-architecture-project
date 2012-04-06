package architecture.common.adaptor;

/**
 * @author  donghyuck
 */
public interface Context {
	
	/**
	 * @uml.property  name="tYPE"
	 */
	public static final String TYPE = "type";
	
	public static final String DATA = "data";
	
	public static final String BATCH = "batch";
	
	public static final String QUERY_NAME = "queryName";
	
	public static final String QUERY_STRING = "queryString";
	
	/**
	 * @author   donghyuck
	 */
	public enum Type {
		/**
		 * @uml.property  name="iNPUT"
		 * @uml.associationEnd  
		 */
		INPUT, 
		/**
		 * @uml.property  name="oUTPUT"
		 * @uml.associationEnd  
		 */
		OUTPUT;
	};
	
	
	public String getConnectorName();
	
	public <T> T getObject(String name, Class<T> requiredType);
	
	public Object getObject(String name);
	
	public void setObject(String key, Object obj);
	
	public int size();

	/**
	 * @return
	 * @uml.property  name="tYPE"
	 */
	public Type getType();
	
}
