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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ProducerObject {

	/**
	 * Id of the producer.
	 */
	private String producerId;
	/**
	 * Category of the producer.
	 */
	private String category;
	/**
	 * Subsystem producer belongs too.
	 */
	private String subsystem;
	/**
	 * Class name, short (class.getSimpleName());
	 */
	private String producerClassName;
	/**
	 * Class name, long (class.getName());
	 */
	private String fullProducerClassName;
	
	private List<StatValue> firstStatsValues;
	
	private List<StatLineObject> lines;

	
	/**
	 * 
	 */
	public ProducerObject() {
		lines = new LinkedList<StatLineObject>();
		firstStatsValues = Collections.EMPTY_LIST;
	}

	/**
	 * @return producerId
	 */
	public String getProducerId() {
		return producerId;
	}

	/**
	 * @param producerId 설정할 producerId
	 */
	public void setProducerId(String producerId) {
		this.producerId = producerId;
	}

	/**
	 * @return category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category 설정할 category
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return subsystem
	 */
	public String getSubsystem() {
		return subsystem;
	}

	/**
	 * @param subsystem 설정할 subsystem
	 */
	public void setSubsystem(String subsystem) {
		this.subsystem = subsystem;
	}

	/**
	 * @return producerClassName
	 */
	public String getProducerClassName() {
		return producerClassName;
	}

	/**
	 * @param producerClassName 설정할 producerClassName
	 */
	public void setProducerClassName(String producerClassName) {
		this.producerClassName = producerClassName;
	}

	/**
	 * @return fullProducerClassName
	 */
	public String getFullProducerClassName() {
		return fullProducerClassName;
	}

	/**
	 * @param fullProducerClassName 설정할 fullProducerClassName
	 */
	public void setFullProducerClassName(String fullProducerClassName) {
		this.fullProducerClassName = fullProducerClassName;
	}

	/**
	 * @return firstStatsValues
	 */
	public List<StatValue> getFirstStatsValues() {
		return firstStatsValues;
	}

	/**
	 * @param firstStatsValues 설정할 firstStatsValues
	 */
	public void setFirstStatsValues(List<StatValue> firstStatsValues) {
		this.firstStatsValues = firstStatsValues;
	}

	/**
	 * @return lines
	 */
	public List<StatLineObject> getLines() {
		return lines;
	}	
	
	public void addStatLine(StatLineObject line){
		lines.add(line);
	}
}
