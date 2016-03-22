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
package architecture.common.util.concurrent;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class LockUtils {

    static class InternHolderComparator implements Comparator<InternHolder> {

	InternHolderComparator() {
	}

	public int compare(InternHolder o1, InternHolder o2) {
	    return o1.lastAccessTime <= o2.lastAccessTime ? o1.lastAccessTime != o2.lastAccessTime ? -1 : 0 : 1;
	}

    }

    static class InternHolder {
	public final String string;
	public volatile long lastAccessTime;

	public void updateAccessTime() {
	    lastAccessTime = System.currentTimeMillis();
	}

	public String toString() {
	    return (new StringBuilder()).append("Key:").append(string).append(", lastAccessTime:")
		    .append(lastAccessTime).toString();
	}

	public InternHolder(String s) {
	    string = s;
	    lastAccessTime = System.currentTimeMillis();
	}
    }

    private static final ConcurrentMap<String, InternHolder> map = new ConcurrentHashMap<String, InternHolder>();

    public LockUtils() {
    }

    public static String internFallBack(String s) {
	return s.intern();
    }

    public static String intern(String s) {
	InternHolder result = map.get(s);
	if (result == null) {
	    InternHolder holder = new InternHolder(s);
	    result = map.putIfAbsent(s, holder);
	    if (result == null)
		result = holder;
	}
	result.updateAccessTime();
	return result.string;
    }

    public void printDetails(PrintStream stream) {
	stream.println("----Strings interned Start----");
	for (String s : map.keySet()) {
	    stream.println(map.get(s).toString());
	}
	stream.println("----Strings interned End----");
    }

    public static long internedCount() {
	return (long) map.size();
    }

    public static long internedSize() {
	long size = 0L;
	for (InternHolder holder : map.values()) {
	    size += holder.string.length();
	}
	return size;
    }

    public static void doCleanup(long highWaterMark, long minLRUTime) {
	if ((long) map.size() <= highWaterMark)
	    return;
	ArrayList<InternHolder> holderList = new ArrayList<InternHolder>(map.values());
	Collections.sort(holderList, new InternHolderComparator());
	long now = System.currentTimeMillis();
	for (InternHolder holder : holderList) {
	    if (minLRUTime >= now - holder.lastAccessTime)
		break;
	    map.remove(holder.string);
	}
    }

}
