package architecture.ee.web.community.forum;

import java.util.Date;
import java.util.Map;

import architecture.common.cache.Cacheable;

public interface Category extends Cacheable{

	public abstract long getCategoryId(); 

	public abstract void setCategoryId(long categoryId); 

	public abstract int getCategroyOrder(); 

	public abstract void setCategroyOrder(int categroyOrder); 

	public abstract String getName(); 

	public abstract void setName(String name); 

	public abstract String getDescription(); 

	public abstract void setDescription(String description);

	public abstract Date getCreationDate(); 

	public abstract void setCreationDate(Date creationDate);
	
	public abstract Date getModifiedDate();
	
	public abstract void setModifiedDate(Date modifiedDate);
	
	public abstract Map<String, String> getProperties();
	
	public abstract void setProperties(Map<String, String> properties);
	
	public abstract boolean getBooleanProperty(String name, boolean defaultValue );
	
	public abstract long getLongProperty(String name, long defaultValue );
	
	public abstract int getIntProperty(String name, int defaultValue );
	
	public abstract String getProperty(String name, String defaultValue );
	
}
