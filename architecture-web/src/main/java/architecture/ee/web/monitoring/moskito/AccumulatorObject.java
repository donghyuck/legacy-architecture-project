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

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import net.anotheria.moskito.core.accumulation.AccumulatedValue;
import net.anotheria.moskito.core.accumulation.Accumulator;

public class AccumulatorObject {

	private String name;
	
	private List<AccumulatedValueObject> values;
	
	public AccumulatorObject(Accumulator acc) {
		this.name = acc.getName();
		this.values = new LinkedList<AccumulatedValueObject>();
		for (AccumulatedValue v : acc.getValues()){
			AccumulatedValueObject ao = new AccumulatedValueObject(new Date(v.getTimestamp()));
			ao.addValue(v.getValue());
			values.add(ao);
		}
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
	 * @return values
	 */
	public List<AccumulatedValueObject> getValues() {
		return values;
	}

	/**
	 * @param values 설정할 values
	 */
	public void setValues(List<AccumulatedValueObject> values) {
		this.values = values;
	}


}
