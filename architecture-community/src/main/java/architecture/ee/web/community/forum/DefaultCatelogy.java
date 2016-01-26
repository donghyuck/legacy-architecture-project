package architecture.ee.web.community.forum;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import architecture.common.cache.CacheSizes;
import architecture.common.util.StringUtils;

public class DefaultCatelogy implements Category {

	private long categoryId;

	private int categroyOrder;

	private String name;

	private String description;

	private Map<String, String> properties;
	
	private Date creationDate;

	private Date modifiedDate;

	public DefaultCatelogy() {
		this.categoryId = -1L;
		this.categroyOrder = 0 ;
		Date now = Calendar.getInstance().getTime();
		this.creationDate = now;
		this.modifiedDate = now;
	}

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public int getCategroyOrder() {
		return categroyOrder;
	}

	public void setCategroyOrder(int categroyOrder) {
		this.categroyOrder = categroyOrder;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	/**
	 * @return properties
	 */
	public Map<String, String> getProperties() {
		synchronized (this) {
			if (properties == null) {
				properties = new ConcurrentHashMap<String, String>();
			}
		}
		return properties;
	}


	public boolean getBooleanProperty(String name, boolean defaultValue) {
		String value = getProperties().get(name);		
		String valueToUse = StringUtils.defaultString(value, Boolean.toString(defaultValue));
		return Boolean.parseBoolean(valueToUse);
	}

	public long getLongProperty(String name, long defaultValue) {
		String value = getProperties().get(name);		
		String valueToUse = StringUtils.defaultString(value, Long.toString(defaultValue));
		return Long.parseLong(valueToUse);
	}

	public int getIntProperty(String name, int defaultValue) {
		String value = getProperties().get(name);		
		String valueToUse = StringUtils.defaultString(value, Integer.toString(defaultValue));
		return Integer.parseInt(valueToUse);
	}

	public String getProperty(String name, String defaultString) {
		String value = getProperties().get(name);
		return StringUtils.defaultString(value, defaultString);
	}

	/**
	 * @param properties 설정할 properties
	 */
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
	

	public int getCachedSize() {
		return CacheSizes.sizeOfLong() + CacheSizes.sizeOfString(name) + CacheSizes.sizeOfString(description)
				+ CacheSizes.sizeOfInt() + CacheSizes.sizeOfDate() + CacheSizes.sizeOfDate();
	}
}
