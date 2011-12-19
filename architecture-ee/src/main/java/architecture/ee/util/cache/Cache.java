package architecture.ee.util.cache;

import java.util.Map;

/**
 * @author                 donghyuck
 */
public interface Cache extends Map {
	/**
	 * @return
	 */
	public abstract String getName();

	/**
	 * @param  name
	 */
	public abstract void setName(String name);

	/**
	 * @return
	 */
	public abstract int getMaxCacheSize();

	/**
	 * @param  maxCacheSize
	 */
	public abstract void setMaxCacheSize(int maxCacheSize);

	/**
	 * @return
	 */
	public abstract long getMaxLifetime();

	/**
	 * @param  maxLifetime
	 */
	public abstract void setMaxLifetime(long maxLifetime);

	public abstract int getCacheSize();

	public abstract long getCacheHits();

	public abstract long getCacheMisses();
}
