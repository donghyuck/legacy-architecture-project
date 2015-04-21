/*
 * Copyright 2012, 2013 Donghyuck, Son
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
package architecture.common.util.internal;
import java.util.Collections;
import java.util.List;

import architecture.common.util.PlatformHelper;
import architecture.common.util.RuntimeHelper;
import architecture.common.util.RuntimeHelperFactory;

public class RuntimeHelperFactoryImpl implements  RuntimeHelperFactory.Implementation {

	private RuntimeHelper helper = null ;
	
	public RuntimeHelper getRuntimeHelper() {
		
		if( helper == null){
			// Java 1.5 이상인 경우...
			if( PlatformHelper.isJvmVersion(1.5F) ){
				helper = new RuntimeHelperImpl();
			}else{
				helper = new RuntimeHelper(){
					public long getTotalHeapMemory() {
						return 0;
					}

					public long getTotalHeapMemoryUsed() {
						return 0;
					}

					public String getJvmInputArguments() {
						return "Unknown";
					}
					public List<MemoryInformation> getMemoryPoolInformation() {
						return Collections.EMPTY_LIST;
					}

					public long getTotalPermGenMemory() {
						return 0;
					}

					public long getTotalPermGenMemoryUsed() {
						return 0;
					}

					public long getTotalNonHeapMemory() {
						return 0;
					}

					public long getTotalNonHeapMemoryUsed() {
						return 0;
					}
				} ;
			}
		}
		return helper;
	}

}
