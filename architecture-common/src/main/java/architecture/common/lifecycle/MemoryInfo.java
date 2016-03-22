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
package architecture.common.lifecycle;

import architecture.common.util.RuntimeHelper;
import architecture.common.util.RuntimeHelperFactory;

public class MemoryInfo {

    public static class Bytes {

	private final long value;

	public Bytes(long value) {
	    this.value = value;
	}

	public long bytes() {
	    return value;
	}

	public long getBytes() {
	    return bytes();
	}

	public long getMegabytes() {
	    return megabytes();
	}

	public long megabytes() {
	    return value / MEGABYTE;
	}

	public Bytes plus(Bytes b) {
	    return new Bytes(value + b.bytes());
	}

	public Bytes minus(Bytes b) {
	    return new Bytes(value - b.bytes());
	}

	public String toString() {
	    return Long.toString(value);
	}

	public boolean equals(Object obj) {
	    if (this == obj)
		return true;
	    if (obj == null || getClass() != obj.getClass())
		return false;
	    Bytes bytes = (Bytes) obj;
	    return value == bytes.value;
	}

	public int hashCode() {
	    return (int) (value ^ value >>> 32);
	}
    }

    private static final long MEGABYTE = 0x100000L;
    private final Bytes maxHeap;
    private final Bytes freeAllocatedHeap;
    private final Bytes allocatedHeap;
    private final Bytes maxPermGen;
    private final Bytes usedPermGen;

    public MemoryInfo() {

	Runtime rt = Runtime.getRuntime();
	RuntimeHelper info = RuntimeHelperFactory.getRuntimeHelper();

	this.freeAllocatedHeap = new Bytes(rt.freeMemory());
	this.allocatedHeap = new Bytes(rt.totalMemory());
	this.maxHeap = new Bytes(rt.maxMemory());
	this.maxPermGen = new Bytes(info.getTotalPermGenMemory());
	this.usedPermGen = new Bytes(info.getTotalPermGenMemoryUsed());

    }

    public Bytes getAvailableHeap() {
	return maxHeap.minus(allocatedHeap).plus(freeAllocatedHeap);
    }

    public Bytes getFreeAllocatedHeap() {
	return freeAllocatedHeap;
    }

    public Bytes getMaxHeap() {
	return maxHeap;
    }

    public Bytes getAllocateHeap() {
	return allocatedHeap;
    }

    public Bytes getUsedHeap() {
	return getAllocateHeap().minus(getFreeAllocatedHeap());
    }

    public Bytes getMaxPermGen() {
	return maxPermGen;
    }

    public Bytes getUsedPermGen() {
	return usedPermGen;
    }

    public Bytes getAvailablePermGen() {
	return maxPermGen.minus(usedPermGen);
    }

}
