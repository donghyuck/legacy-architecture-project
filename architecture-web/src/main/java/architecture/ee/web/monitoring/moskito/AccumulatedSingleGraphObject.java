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

import java.util.ArrayList;
import java.util.List;

public class AccumulatedSingleGraphObject {

	private String name;
	private List<AccumulatedValueObject> data;
	
	public AccumulatedSingleGraphObject(String name) {
		this.name = name;
		data = new ArrayList<AccumulatedValueObject>();
	}

	public void add(AccumulatedValueObject value){
		this.data.add(value);
	}
	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name 설정할 name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return data
	 */
	public List<AccumulatedValueObject> getData() {
		return data;
	}

	/**
	 * @param data 설정할 data
	 */
	public void setData(List<AccumulatedValueObject> data) {
		this.data = data;
	}


}
