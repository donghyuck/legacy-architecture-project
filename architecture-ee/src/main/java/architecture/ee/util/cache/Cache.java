package architecture.ee.util.cache;

import java.util.Map;

/**
 * @author                 donghyuck
 */
public interface Cache extends Map {

	/**
	 * @return
	 * @uml.property  name="name"
	 */
	public abstract String getName();

	/**
	 * @param  name
	 * @uml.property  name="name"
	 */
	public abstract void setName(String name);

	/**
	 * @return
	 * @uml.property  name="maxCacheSize"
	 */
	public abstract int getMaxCacheSize();

	/**
	 * @param  maxCacheSize
	 * @uml.property  name="maxCacheSize"
	 */
	public abstract void setMaxCacheSize(int maxCacheSize);

	/**
	 * @return
	 * @uml.property  name="maxLifetime"
	 */
	public abstract long getMaxLifetime();

	/**
	 * @param  maxLifetime
	 * @uml.property  name="maxLifetime"
	 */
	public abstract void setMaxLifetime(long maxLifetime);

	public abstract int getCacheSize();

	public abstract long getCacheHits();

	public abstract long getCacheMisses();
}
