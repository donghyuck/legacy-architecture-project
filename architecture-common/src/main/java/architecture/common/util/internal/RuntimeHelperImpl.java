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

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.RuntimeMXBean;
import java.util.ArrayList;
import java.util.List;

import architecture.common.util.RuntimeHelper;

public class RuntimeHelperImpl implements RuntimeHelper {

    private final MemoryMXBean memoryMB = ManagementFactory.getMemoryMXBean();

    private final RuntimeMXBean runtimeMB = ManagementFactory.getRuntimeMXBean();

    public long getTotalHeapMemory() {
	return memoryMB.getHeapMemoryUsage().getMax();
    }

    public long getTotalHeapMemoryUsed() {
	return memoryMB.getHeapMemoryUsage().getUsed();
    }

    public String getJvmInputArguments() {
	StringBuilder sb = new StringBuilder();
	for (String arg : runtimeMB.getInputArguments()) {
	    sb.append(arg).append(" ");
	}
	return sb.toString();
    }

    public List<MemoryInformation> getMemoryPoolInformation() {
	List<MemoryPoolMXBean> mbs = ManagementFactory.getMemoryPoolMXBeans();
	List<MemoryInformation> list = new ArrayList<MemoryInformation>(mbs.size());
	for (MemoryPoolMXBean mb : mbs) {
	    list.add(new DefaultMemoryInformation(mb));
	}
	return list;
    }

    public long getTotalPermGenMemory() {
	return getPermGen().getTotal();
    }

    public long getTotalPermGenMemoryUsed() {
	return getPermGen().getUsed();
    }

    public long getTotalNonHeapMemory() {
	return memoryMB.getNonHeapMemoryUsage().getMax();
    }

    public long getTotalNonHeapMemoryUsed() {
	return memoryMB.getNonHeapMemoryUsage().getUsed();
    }

    private MemoryInformation getPermGen() {
	for (MemoryInformation mi : getMemoryPoolInformation()) {
	    String name = mi.getName().toLowerCase();
	    if (name.contains("perm gen"))
		return mi;
	}
	return new MemoryInformation() {

	    public String getName() {
		return "";
	    }

	    public long getTotal() {
		return -1L;
	    }

	    public long getUsed() {
		return -1L;
	    }

	    public long getFree() {
		return -1L;
	    }

	};
    }

}
