package architecture.common.util.profiling;

import architecture.common.util.ImplFactory;

public class ProfilerFactory {

	/**
	 * @author   donghyuck
	 */
	public static interface Implementation
    {		
		public abstract Profiler getProfiler();		
		/**
		 * @return
		 * @uml.property  name="profilingEnabled"
		 */
		public abstract boolean isProfilingEnabled();
		/**
		 * @param  enabled
		 * @uml.property  name="profilingEnabled"
		 */
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
