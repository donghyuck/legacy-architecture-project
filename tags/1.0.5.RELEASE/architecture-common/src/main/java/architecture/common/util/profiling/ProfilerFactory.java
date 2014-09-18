/*
 * Copyright 2012 Donghyuck, Son
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
