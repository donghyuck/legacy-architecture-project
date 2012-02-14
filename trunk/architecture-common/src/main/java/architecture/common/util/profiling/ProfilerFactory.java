package architecture.common.util.profiling;

import architecture.common.util.ImplFactory;

public class ProfilerFactory {

	public static interface Implementation
    {		
		public abstract Profiler getProfiler();		
		public abstract boolean isProfilingEnabled();
		public abstract void setProfilingEnabled(boolean enabled);
		
    }
	
	/**
	 * @uml.property  name="impl"
	 * @uml.associationEnd  
	 */
	private static Implementation impl = null;
    
    static 
    {
        impl = (Implementation)ImplFactory.loadImplFromKey(ProfilerFactory.Implementation.class);
    }
    
	public static Profiler getProfiler(){
		return impl.getProfiler();
	}
	
	public static boolean isProfilingEnabled(){
		return impl.isProfilingEnabled();
	}
	
	public static void setProfilingEnabled(boolean enabled){
		impl.setProfilingEnabled(enabled);
	}
}
