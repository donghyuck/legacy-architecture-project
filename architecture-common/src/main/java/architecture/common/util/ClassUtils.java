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
package architecture.common.util;

import java.util.HashSet;
import java.util.Set;

public class ClassUtils extends org.apache.commons.lang.ClassUtils {

	/**
	 * Finds all super classes and interfaces for a given class
	 * 
	 * @param cls
	 *            The class to scan
	 * @return The collected related classes found
	 */
	public static Set<Class> findAllTypes(Class cls) {
		final Set<Class> types = new HashSet<Class>();
		findAllTypes(cls, types);
		return types;
	}

	/**
	 * Finds all super classes and interfaces for a given class
	 * 
	 * @param cls
	 *            The class to scan
	 * @param types
	 *            The collected related classes found
	 */
	public static void findAllTypes(Class cls, Set<Class> types) {
		if (cls == null) {
			return;
		}

		// check to ensure it hasn't been scanned yet
		if (types.contains(cls)) {
			return;
		}

		types.add(cls);

		findAllTypes(cls.getSuperclass(), types);
		for (int x = 0; x < cls.getInterfaces().length; x++) {
			findAllTypes(cls.getInterfaces()[x], types);
		}
	}
}
