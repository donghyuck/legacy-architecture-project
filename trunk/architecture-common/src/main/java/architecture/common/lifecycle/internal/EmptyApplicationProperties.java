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
package architecture.common.lifecycle.internal;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;


public class EmptyApplicationProperties extends AbstractApplicationProperties {

	/**
	 * @author    donghyuck
	 */
	private static class InstanceHolder {

		/**
		 * @uml.property  name="instance"
		 * @uml.associationEnd  
		 */
		private static final EmptyApplicationProperties instance = new EmptyApplicationProperties();

		private InstanceHolder() {
		}
	}

	private EmptyApplicationProperties() {}

	public static EmptyApplicationProperties getInstance() {
		return InstanceHolder.instance;
	}

	public Collection<String> getChildrenNames(String name) {
		return Collections.emptyList();
	}

	public Collection<String> getPropertyNames() {
		return Collections.emptyList();
	}

	public void clear() {
	}

	public boolean containsKey(Object key) {
		return false;
	}

	public boolean containsValue(Object value) {
		return false;
	}

	public Set<java.util.Map.Entry<String, String>> entrySet() {
		return null;
	}

	public String get(Object key) {
		return null;
	}

	public Collection<String> values() {
	   return Collections.emptyList();
	}


	public Set<String> keySet() {
		return Collections.emptySet();
	}

	public String put(String key, String value) {
		return null;
	}

	public void putAll(Map<? extends String, ? extends String> m) {
	}

	public String remove(Object key) {
		return null;
	}

	public int size() {
		return 0;
	}

	public boolean isEmpty() {
		return true;
	}

	public String getStringProperty(String name, String defaultValue) {
		return defaultValue;
	}

 }
