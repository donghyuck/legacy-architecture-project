package architecture.common.util.profiling.impl;

import architecture.common.util.profiling.Profiler;
import architecture.common.util.profiling.ProfilerFactory;

/**
 * 
 * 
 * @author  donghyuck
 */
public class ProfilerFactoryImpl implements ProfilerFactory.Implementation {

	private ThreadLocal<Profiler> profiler = new ThreadLocal<Profiler>();

	private boolean enabled = false;
	
	/**
	 * @return
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return
	 */
	public Profiler getProfiler() {
        Profiler p = (Profiler)profiler.get();
        if(p == null)
        {
            p = new Profiler();
            profiler.set(p);            
        }
        p.setProfilingEnabled(enabled);
        return p;
	}

	public boolean isProfilingEnabled() {
		return enabled;
	}

	public void setProfilingEnabled(boolean enabled) {
		this.enabled = enabled;		
	}
}
