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

import net.anotheria.moskito.core.stats.TimeUnit;
import net.anotheria.moskito.core.threshold.ThresholdConditionGuard;

public class ThresholdDefinitionObject {

	private String id;
	
	private String name;
	
	private String producerName;
	
	private String statName;
	
	private String valueName;
	
	private String intervalName;
	
	private String descriptionString;
	
	private List<ThresholdConditionGuard> guards;
	
	private TimeUnit timeUnit;
	
	public ThresholdDefinitionObject() {
		guards = new ArrayList<ThresholdConditionGuard>();
	}

	public void addGuard(ThresholdConditionGuard aGuard){
		guards.add(aGuard);
	}

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
	 * @return producerName
	 */
	public String getProducerName() {
		return producerName;
	}

	/**
	 * @param producerName 설정할 producerName
	 */
	public void setProducerName(String producerName) {
		this.producerName = producerName;
	}

	/**
	 * @return statName
	 */
	public String getStatName() {
		return statName;
	}

	/**
	 * @param statName 설정할 statName
	 */
	public void setStatName(String statName) {
		this.statName = statName;
	}

	/**
	 * @return valueName
	 */
	public String getValueName() {
		return valueName;
	}

	/**
	 * @param valueName 설정할 valueName
	 */
	public void setValueName(String valueName) {
		this.valueName = valueName;
	}

	/**
	 * @return intervalName
	 */
	public String getIntervalName() {
		return intervalName;
	}

	/**
	 * @param intervalName 설정할 intervalName
	 */
	public void setIntervalName(String intervalName) {
		this.intervalName = intervalName;
	}

	/**
	 * @return descriptionString
	 */
	public String getDescriptionString() {
		return descriptionString;
	}

	/**
	 * @param descriptionString 설정할 descriptionString
	 */
	public void setDescriptionString(String descriptionString) {
		this.descriptionString = descriptionString;
	}

	/**
	 * @return guards
	 */
	public List<ThresholdConditionGuard> getGuards() {
		return guards;
	}

	/**
	 * @param guards 설정할 guards
	 */
	public void setGuards(List<ThresholdConditionGuard> guards) {
		this.guards = guards;
	}

	/**
	 * @return timeUnit
	 */
	public TimeUnit getTimeUnit() {
		return timeUnit;
	}

	/**
	 * @param timeUnit 설정할 timeUnit
	 */
	public void setTimeUnit(TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
	}
	
	@Override
	public String toString() {
		return id+"/"+producerName+"/"+statName+"/"+valueName+"/"+valueName;
	}
}
