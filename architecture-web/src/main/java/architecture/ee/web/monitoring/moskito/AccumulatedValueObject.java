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
import java.util.Date;
import java.util.List;

import architecture.common.model.json.CustomJsonDateSerializer;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class AccumulatedValueObject {
	
	private List<String> values;	
	
	private String name ;
	
	private Date date;
		
	public AccumulatedValueObject(Date date) {
		this.date = date;
		values = new ArrayList<String>();
	}

	/**
	 * @return values
	 */
	public List<String> getValues() {
		return values;
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
	 * @param values 설정할 values
	 */
	public void setValues(List<String> values) {
		this.values = values;
	}

	public void addValue(String value){
		this.values.add(value); 
	}
	/**
	 * @return date
	 */
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	public Date getDate() {
		return date;
	}

	/**
	 * @param date 설정할 date
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return firstValue
	 */
	public String getFirstValue() {
		return this.values.get(0);
	}


}
