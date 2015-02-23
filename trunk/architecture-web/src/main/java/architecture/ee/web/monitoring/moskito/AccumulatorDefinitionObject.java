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

import architecture.common.model.json.CustomJsonDateSerializer;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class AccumulatorDefinitionObject {

	
	private String id;
	/**
	 * @return id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id 설정할 id
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return path
	 */
	public String getPath() {
		return path;
	}
	/**
	 * @param path 설정할 path
	 */
	public void setPath(String path) {
		this.path = path;
	}
	private String name;
	private String path;
	private int numberOfValues;
	
	private Date lastValueDate;
	
	public AccumulatorDefinitionObject() {
		// TODO 자동 생성된 생성자 스텁
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
	 * @return numberOfValues
	 */
	public int getNumberOfValues() {
		return numberOfValues;
	}
	/**
	 * @param numberOfValues 설정할 numberOfValues
	 */
	public void setNumberOfValues(int numberOfValues) {
		this.numberOfValues = numberOfValues;
	}
	/**
	 * @return lastValueDate
	 */
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	public Date getLastValueDate() {
		return lastValueDate;
	}
	/**
	 * @param lastValueDate 설정할 lastValueDate
	 */
	public void setLastValueDate(Date lastValueDate) {
		this.lastValueDate = lastValueDate;
	}


}
