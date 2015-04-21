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

import net.anotheria.moskito.core.threshold.ThresholdStatus;

public class ThresholdStatusObject {

	private String name;
	private String status;
	private String colorCode;
	private String timestamp;
	private String description;
	private String value;
	private String previousColorCode;
	private String previousStatus;
	private ThresholdStatus statusForSorting;
	
	
	private Date updatedDate;
	private String id;
	private long flipCount;
	
	public ThresholdStatusObject() {
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
	 * @return status
	 */
	public String getStatus() {
		return status;
	}


	/**
	 * @param status 설정할 status
	 */
	public void setStatus(String status) {
		this.status = status;
	}


	/**
	 * @return colorCode
	 */
	public String getColorCode() {
		return colorCode;
	}


	/**
	 * @param colorCode 설정할 colorCode
	 */
	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}


	/**
	 * @return timestamp
	 */
	public String getTimestamp() {
		return timestamp;
	}


	/**
	 * @param timestamp 설정할 timestamp
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}


	/**
	 * @return description
	 */
	public String getDescription() {
		return description;
	}


	/**
	 * @param description 설정할 description
	 */
	public void setDescription(String description) {
		this.description = description;
	}


	/**
	 * @return value
	 */
	public String getValue() {
		return value;
	}


	/**
	 * @param value 설정할 value
	 */
	public void setValue(String value) {
		this.value = value;
	}


	/**
	 * @return previousColorCode
	 */
	public String getPreviousColorCode() {
		return previousColorCode;
	}


	/**
	 * @param previousColorCode 설정할 previousColorCode
	 */
	public void setPreviousColorCode(String previousColorCode) {
		this.previousColorCode = previousColorCode;
	}


	/**
	 * @return previousStatus
	 */
	public String getPreviousStatus() {
		return previousStatus;
	}


	/**
	 * @param previousStatus 설정할 previousStatus
	 */
	public void setPreviousStatus(String previousStatus) {
		this.previousStatus = previousStatus;
	}


	/**
	 * @return statusForSorting
	 */
	public ThresholdStatus getStatusForSorting() {
		return statusForSorting;
	}


	/**
	 * @param statusForSorting 설정할 statusForSorting
	 */
	public void setStatusForSorting(ThresholdStatus statusForSorting) {
		this.statusForSorting = statusForSorting;
	}


	/**
	 * @return updatedDate
	 */
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	public Date getUpdatedDate() {
		return updatedDate;
	}


	/**
	 * @param updatedDate 설정할 updatedDate
	 */
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
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
	 * @return flipCount
	 */
	public long getFlipCount() {
		return flipCount;
	}


	/**
	 * @param flipCount 설정할 flipCount
	 */
	public void setFlipCount(long flipCount) {
		this.flipCount = flipCount;
	}


	@Override public String toString(){
		return getName()+" "+getStatus()+" "+getTimestamp()+" "+getDescription()+" "+getValue();
	}
}
