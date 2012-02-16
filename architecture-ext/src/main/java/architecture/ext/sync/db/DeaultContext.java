package architecture.ext.sync.db;

import java.util.HashMap;
import java.util.Map;

import architecture.common.adaptor.Context;

public class DeaultContext implements Context {

	private Map<String, Object> context = new HashMap<String, Object>();
	
	@SuppressWarnings("unchecked")
	public <T> T getObject(String name, Class<T> requiredType) {
		return (T)context.get(name);
	}

	public <T> T getObject(Class<T> requiredType) {

		
		for( Object obj : context.values()){
			if( requiredType.getClass().isInstance(obj) )
				return (T) obj;
		}
		return null;
	}

	public void setObject(String key, Object obj) {
		context.put(key, obj);
	}

}
