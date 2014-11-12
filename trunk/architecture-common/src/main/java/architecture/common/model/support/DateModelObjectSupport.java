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
package architecture.common.model.support;

import java.util.Date;

import architecture.common.model.DateModelObject;
import architecture.common.model.json.CustomJsonDateDeserializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public abstract class DateModelObjectSupport implements DateModelObject {

	private Date creationDate = null;
	
	private Date modifiedDate = null;
	

	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	public Date getCreationDate() {
		return creationDate;
	}

	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	public Date getModifiedDate() {
		return modifiedDate;
	}

	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
}
