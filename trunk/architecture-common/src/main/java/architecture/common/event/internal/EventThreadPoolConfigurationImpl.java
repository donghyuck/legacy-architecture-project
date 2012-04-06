package architecture.common.event.internal;

import java.util.concurrent.TimeUnit;

import architecture.common.event.config.EventThreadPoolConfiguration;

/**
 * @author    donghyuck
 */
public class EventThreadPoolConfigurationImpl implements EventThreadPoolConfiguration
{

    /**
	 * @uml.property  name="cORE_POOL_SIZE"
	 */
    private static final int CORE_POOL_SIZE = 16;

    /**
	 * @uml.property  name="mAXIMUM_POOL_SIZE"
	 */
    private static final int MAXIMUM_POOL_SIZE = 64;

    /**
	 * @uml.property  name="kEEP_ALIVE_TIME"
	 */
    private static final long KEEP_ALIVE_TIME = 60L;

    /**
	 * @return
	 * @uml.property  name="cORE_POOL_SIZE"
	 */
    public int getCorePoolSize()
    {
        return CORE_POOL_SIZE;
    }

    /**
	 * @return
	 * @uml.property  name="mAXIMUM_POOL_SIZE"
	 */
    public int getMaximumPoolSize()
    {
        return MAXIMUM_POOL_SIZE;
    }

    /**
	 * @return
	 * @uml.property  name="kEEP_ALIVE_TIME"
	 */
    public long getKeepAliveTime()
    {
        return KEEP_ALIVE_TIME;
    }

    public TimeUnit getTimeUnit()
    {
        return TimeUnit.SECONDS;
    }
}
