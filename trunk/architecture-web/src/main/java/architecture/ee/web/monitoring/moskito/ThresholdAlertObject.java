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

public class ThresholdAlertObject {

	private String id;
	
	private String name;
	
	private String oldStatus;
	
	private String oldColorCode;

	private String oldValue;
	
	private String newStatus;
	
	private String newColorCode;
	
	private String newValue;
	
	private Date updatedDate;
	
	public ThresholdAlertObject() {
		// TODO 자동 생성된 생성자 스텁
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
	 * @return oldStatus
	 */
	public String getOldStatus() {
		return oldStatus;
	}

	/**
	 * @param oldStatus 설정할 oldStatus
	 */
	public void setOldStatus(String oldStatus) {
		this.oldStatus = oldStatus;
	}

	/**
	 * @return oldColorCode
	 */
	public String getOldColorCode() {
		return oldColorCode;
	}

	/**
	 * @param oldColorCode 설정할 oldColorCode
	 */
	public void setOldColorCode(String oldColorCode) {
		this.oldColorCode = oldColorCode;
	}

	/**
	 * @return oldValue
	 */
	public String getOldValue() {
		return oldValue;
	}

	/**
	 * @param oldValue 설정할 oldValue
	 */
	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	/**
	 * @return newStatus
	 */
	public String getNewStatus() {
		return newStatus;
	}

	/**
	 * @param newStatus 설정할 newStatus
	 */
	public void setNewStatus(String newStatus) {
		this.newStatus = newStatus;
	}

	/**
	 * @return newColorCode
	 */
	public String getNewColorCode() {
		return newColorCode;
	}

	/**
	 * @param newColorCode 설정할 newColorCode
	 */
	public void setNewColorCode(String newColorCode) {
		this.newColorCode = newColorCode;
	}

	/**
	 * @return newValue
	 */
	public String getNewValue() {
		return newValue;
	}

	/**
	 * @param newValue 설정할 newValue
	 */
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	/**
	 * @return updatedDate
	 */
	public Date getUpdatedDate() {
		return updatedDate;
	}

	/**
	 * @param updatedDate 설정할 updatedDate
	 */
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

}
