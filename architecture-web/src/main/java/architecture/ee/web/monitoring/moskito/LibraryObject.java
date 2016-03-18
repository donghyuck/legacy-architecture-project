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

import org.configureme.util.DateUtils;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import architecture.common.model.json.CustomJsonDateSerializer;
import net.anotheria.util.maven.MavenVersion;

public class LibraryObject {
	/**
	 * Name of the lib.
	 */
	private String name;
	/**
	 * Version of the lib.
	 */
	private MavenVersion mavenVersion;

	/**
	 * Last modified timestamp.
	 */
	private Date lastModified;

	
	@JsonSerialize(using = CustomJsonDateSerializer.class)
	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MavenVersion getMavenVersion() {
		return mavenVersion;
	}

	public void setMavenVersion(MavenVersion mavenVersion) {
		this.mavenVersion = mavenVersion;
	}

	public String getTimestamp() {
		return mavenVersion == null ? DateUtils.toISO8601String(lastModified) : mavenVersion.getFileTimestamp() ;
	}

	public String getGroup() {
		return mavenVersion == null ? "-" : mavenVersion.getGroup();
	}

	public String getArtifact() {
		return mavenVersion == null ? "-" : mavenVersion.getArtifact();
	}

	public String getVersion() {
		return mavenVersion == null ? "-" : mavenVersion.getVersion();
	}

	@Override
	public String toString() {
		return "LibraryObject{" + "name='" + name + '\'' + ", mavenVersion=" + mavenVersion + ", lastModified='" + getTimestamp() + '\'' + '}';
	}
}
