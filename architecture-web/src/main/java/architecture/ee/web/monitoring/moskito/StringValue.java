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

public class StringValue extends StatValue {
	
	private String value;


	
	/**
	 * @param name
	 * @param value
	 */
	public StringValue(String name, String value) {
		super(name);
		this.value = value;
	}

	@Override public String getType(){
		return "string";
	}
	
	@Override public String getValue(){
		return value;
	}
	
}
