/*
 * Copyright 2010, 2011 INKIUM, Inc.
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
package architecture.ee.bootstrap;

import javax.servlet.ServletContext;

import architecture.common.util.ImplFactory;

public class BootStrap {

	public static interface Implementation {

		public abstract boolean boot(ServletContext context);

		public abstract void shutdown(ServletContext context);
		
	}
	
	static final Implementation impl = (Implementation)ImplFactory.loadImplFromKey(Implementation.class);
    
	public static boolean boot(ServletContext context){
		return impl.boot(context);
	}

	public static void shutdown(ServletContext context){
		impl.shutdown(context);
	}
	
}
