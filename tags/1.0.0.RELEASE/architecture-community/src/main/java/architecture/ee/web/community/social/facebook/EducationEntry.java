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
package architecture.ee.web.community.social.facebook;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class EducationEntry implements Serializable {

	private final Reference school;

	private final Reference year;

	private final List<Reference> concentration;
	
	private final String type;

	public EducationEntry(Reference school, Reference year, List<Reference> concentration, String type) {
		this.school = school;
		this.year = year;
		this.concentration = concentration;
		this.type = type;
	}

	public Reference getSchool() {
		return school;
	}

	public Reference getYear() {
		return year;
	}

	public List<Reference> getConcentration() {
		return concentration;
	}

	public String getType() {
		return type;
	}
}

