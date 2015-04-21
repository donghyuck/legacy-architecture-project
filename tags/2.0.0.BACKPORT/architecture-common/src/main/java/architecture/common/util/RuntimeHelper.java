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
package architecture.common.util;

import java.util.List;

import architecture.common.util.internal.MemoryInformation;

public interface RuntimeHelper {

	public abstract long getTotalHeapMemory();
	
	public abstract long getTotalHeapMemoryUsed();
	
	public abstract String getJvmInputArguments();
	
	public abstract List<MemoryInformation> getMemoryPoolInformation();
	
	public abstract long getTotalPermGenMemory();
	
	public abstract long getTotalPermGenMemoryUsed();
	
	public abstract long getTotalNonHeapMemory();
	
	public abstract long getTotalNonHeapMemoryUsed();
	
}
