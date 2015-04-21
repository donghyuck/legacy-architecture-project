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
package architecture.ee.web.monitoring.moskito;

public abstract class StatValue {

	private String name;

	
	/**
	 * @param name
	 */
	public StatValue(String name) {
		super();
		this.name = name;
	}

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            설정할 name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	public abstract Object getValue();
		
	public abstract String getType();
	
}
