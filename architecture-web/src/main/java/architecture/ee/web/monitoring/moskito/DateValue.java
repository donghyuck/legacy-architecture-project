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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import architecture.common.model.json.CustomJsonDateSerializer;

public class DateValue extends StatValue {

	private Date dateValue;
	
	
	/**
	 * @param name
	 * @param doubleValue
	 */
	public DateValue(String name, Date dateValue) {
		super(name);
		this.dateValue = dateValue;
	}

	@Override
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	public Date getValue() {
		return dateValue;
	}

	@Override
	public String getType() {
		return "date";
	}

}
