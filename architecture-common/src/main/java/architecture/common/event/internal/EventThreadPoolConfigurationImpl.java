package architecture.common.event.internal;

import java.util.concurrent.TimeUnit;

import architecture.common.event.config.EventThreadPoolConfiguration;

public class EventThreadPoolConfigurationImpl implements EventThreadPoolConfiguration {
    private static final int CORE_POOL_SIZE = 16;
    private static final int MAXIMUM_POOL_SIZE = 64;
    private static final long KEEP_ALIVE_TIME = 60L;

    public int getCorePoolSize() {
        return CORE_POOL_SIZE;
    }

    public int getMaximumPoolSize() {
        return MAXIMUM_POOL_SIZE;
    }

    public long getKeepAliveTime() {
        return KEEP_ALIVE_TIME;
    }

    public TimeUnit getTimeUnit() {
        return TimeUnit.SECONDS;
    }
}
