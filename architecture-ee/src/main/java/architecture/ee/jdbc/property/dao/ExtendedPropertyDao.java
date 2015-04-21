package architecture.ee.jdbc.property.dao;

import java.util.Map;

public interface ExtendedPropertyDao {

	public abstract Map<String, String> getProperties(String table, String typeField, long objectID);

	public abstract void deleteProperties(String table, String typeField, long objectID);
	
	public abstract void updateProperties(String table, String keyField, long objectID, Map<String, String> properties);
	
}
