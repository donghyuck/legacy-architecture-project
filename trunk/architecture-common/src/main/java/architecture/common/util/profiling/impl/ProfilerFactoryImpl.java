package architecture.common.util.profiling.impl;

import architecture.common.util.profiling.Profiler;
import architecture.common.util.profiling.ProfilerFactory;
import architecture.common.util.profiling.ProfilerFactory.Implementation;

public class ProfilerFactoryImpl implements ProfilerFactory.Implementation {

	private ThreadLocal<Profiler> profiler = new ThreadLocal<Profiler>();
	private boolean enabled = false;
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

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
