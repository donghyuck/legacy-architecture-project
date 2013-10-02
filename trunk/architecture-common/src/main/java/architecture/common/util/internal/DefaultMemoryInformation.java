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

import java.lang.management.MemoryPoolMXBean;

public class DefaultMemoryInformation implements MemoryInformation {

	private final MemoryPoolMXBean memoryPool;
	
	public DefaultMemoryInformation(MemoryPoolMXBean memoryPool) {
		this.memoryPool = memoryPool;
	}

	public String getName() {
		return memoryPool.getName();
	}

	public long getTotal() {
		return memoryPool.getUsage().getMax();
	}

	public long getUsed() {
		return memoryPool.getUsage().getUsed();
	}

	public long getFree() {
		return getTotal() - getUsed();
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(memoryPool.getName()).append(": ").append(memoryPool.getUsage().toString());
		return sb.toString();
	}
}
