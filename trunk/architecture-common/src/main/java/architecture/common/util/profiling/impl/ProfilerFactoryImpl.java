package architecture.common.util.profiling.impl;

import architecture.common.util.profiling.Profiler;
import architecture.common.util.profiling.ProfilerFactory;
import architecture.common.util.profiling.ProfilerFactory.Implementation;

/**
 * @author  donghyuck
 */
public class ProfilerFactoryImpl implements ProfilerFactory.Implementation {

	/**
	 * @uml.property  name="profiler"
	 */
	private ThreadLocal<Profiler> profiler = new ThreadLocal<Profiler>();
	/**
	 * @uml.property  name="enabled"
	 */
	private boolean enabled = false;
	
	/**
	 * @return
	 * @uml.property  name="enabled"
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled
	 * @uml.property  name="enabled"
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return
	 * @uml.property  name="profiler"
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
