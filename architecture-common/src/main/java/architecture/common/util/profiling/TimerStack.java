package architecture.common.util.profiling;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TimerStack {


    private static final Log log = LogFactory.getLog(TimerStack.class);
    
    private static ThreadLocal<ProfilingTimerBean> current = new ThreadLocal<ProfilingTimerBean>();
    
    public static final String ACTIVATE_PROPERTY = "architecture.common.profile.activate";
    
    public static final String ACTIVATE_MEMORY_PROPERTY = "architecture.common.profile.activate.memory";
    
    public static final String MIN_TIME = "architecture.common.profile.mintime";
    
    public static final String MIN_TOTAL_TIME = "architecture.common.profile.mintotaltime";
    
    private static volatile long configuredMinTime = Long.getLong(MIN_TIME, 0L).longValue();
    
    private static volatile long configuredMinTotalTime = Long.getLong(MIN_TOTAL_TIME, 0L).longValue();
    
    private static volatile boolean activeFlag = true; //"true".equalsIgnoreCase(System.getProperty(ACTIVATE_PROPERTY, "false"));
    
    private static volatile boolean profileMemoryFlag = true; //"true".equalsIgnoreCase(System.getProperty(ACTIVATE_MEMORY_PROPERTY, "false"));
    
    private static TimerLogger logger = new TimerLogger() {

        public void log(String s)
        {
            TimerStack.log.debug(s);
        }

    };

    public static void push(String name)
    {
        if(!isActive())
            return;
        ProfilingTimerBean newTimer = new ProfilingTimerBean(name);
        newTimer.setStartTime();
        if(isProfileMemory())
            newTimer.setStartMem();
        ProfilingTimerBean currentTimer = (ProfilingTimerBean)current.get();
        if(currentTimer != null)
            currentTimer.addChild(newTimer);
        current.set(newTimer);
    }

    public static void pop(String name)
    {
        if(!isActive())
            return;
        ProfilingTimerBean currentTimer = (ProfilingTimerBean)current.get();
        if(isProfileMemory())
            currentTimer.setEndMem();
        if(currentTimer != null && name != null && name.equals(currentTimer.getResource()))
        {
            currentTimer.setEndTime();
            ProfilingTimerBean parent = currentTimer.getParent();
            if(parent == null)
            {
                if(currentTimer.getTotalTime() > getMinTotalTime())
                    printTimes(currentTimer);
                current.set(null);
            } else
            {
                current.set(parent);
            }
        } else
        if(currentTimer != null)
        {
            printTimes(currentTimer);
            current.set(null);            
            log.debug( String.format("Unmatched Timer.  Was expecting {}, instead got {}", currentTimer.getResource(), name) );
        }
    }

    private static void printTimes(ProfilingTimerBean currentTimer)
    {
        String printable = currentTimer.getPrintable(getMinTime());
        if(printable != null && !"".equals(printable.trim()))
            logger.log(printable);
    }

    private static long getMinTime()
    {
        return configuredMinTime;
    }

    private static long getMinTotalTime()
    {
        return configuredMinTotalTime;
    }

    public static boolean isActive()
    {
        return activeFlag;
    }

    public static void setMinTime(long minTime)
    {
        configuredMinTime = minTime;
    }

    public static void setMinTotalTime(long minTotalTime)
    {
        configuredMinTotalTime = minTotalTime;
    }

    public static boolean isProfileMemory()
    {
        return profileMemoryFlag;
    }

    public static void setActive(boolean active)
    {
        activeFlag = active;
    }

    public static void setProfileMemory(boolean active)
    {
        profileMemoryFlag = active;
    }

    public static void setLogger(TimerLogger logger)
    {
        TimerStack.logger = logger;
    }


}
