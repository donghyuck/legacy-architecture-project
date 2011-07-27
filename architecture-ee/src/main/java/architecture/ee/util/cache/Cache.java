package architecture.ee.util.cache;

import java.util.Map;

public interface Cache extends Map {

	public abstract String getName();

	public abstract void setName(String name);

	public abstract int getMaxCacheSize();

	public abstract void setMaxCacheSize(int maxCacheSize);

	public abstract long getMaxLifetime();

	public abstract void setMaxLifetime(long maxLifetime);

	public abstract int getCacheSize();

	public abstract long getCacheHits();

	public abstract long getCacheMisses();
}
