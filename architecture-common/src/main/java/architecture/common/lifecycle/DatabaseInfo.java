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
package architecture.common.lifecycle;

import architecture.common.jdbc.schema.DatabaseType;

public class DatabaseInfo {

    private static String ISOLATION_LEVELS[] = { "Read committed", "Read uncommitted", "Read committed", null,
	    "Repeatable read", null, null, null, "Serializable" };
    private String name;

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    private String url;
    private String dialect;
    private String isolationLevel;
    private String driverName;
    private String driverVersion;
    private String databaseName;
    private String databaseVersion;
    private Long exampleLatency;

    public DatabaseInfo() {
    }

    public DatabaseInfo(DatabaseType databaseType) {
	driverName = databaseType.jdbcDriverName;
	driverVersion = databaseType.jdbcDriverVersion;
	databaseName = databaseType.databaseProductName;
	databaseVersion = databaseType.databaseProductVersion;
	if (databaseType.transactionIsolation < 0 || databaseType.transactionIsolation >= ISOLATION_LEVELS.length)
	    isolationLevel = null;
	else
	    isolationLevel = ISOLATION_LEVELS[databaseType.transactionIsolation];
    }

    public String getUrl() {
	return url;
    }

    public void setUrl(String url) {
	this.url = url;
    }

    public String getDialect() {
	return dialect;
    }

    public void setDialect(String dialect) {
	this.dialect = dialect;
    }

    public String getIsolationLevel() {
	return isolationLevel;
    }

    public void setIsolationLevel(String isolationLevel) {
	this.isolationLevel = isolationLevel;
    }

    public String getDriverName() {
	return driverName;
    }

    public void setDriverName(String driverName) {
	this.driverName = driverName;
    }

    public String getDriverVersion() {
	return driverVersion;
    }

    public void setDriverVersion(String driverVersion) {
	this.driverVersion = driverVersion;
    }

    public String getDatabaseName() {
	return databaseName;
    }

    public void setDatabaseName(String databaseName) {
	this.databaseName = databaseName;
    }

    public String getDatabaseVersion() {
	return databaseVersion;
    }

    public void setDatabaseVersion(String databaseVersion) {
	this.databaseVersion = databaseVersion;
    }

    public Long getExampleLatency() {
	return exampleLatency;
    }

    public void setExampleLatency(Long exampleLatency) {
	this.exampleLatency = exampleLatency;
    }

    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("DatabaseInfo [url=");
	builder.append(url);
	builder.append(", dialect=");
	builder.append(dialect);
	builder.append(", isolationLevel=");
	builder.append(isolationLevel);
	builder.append(", driverName=");
	builder.append(driverName);
	builder.append(", driverVersion=");
	builder.append(driverVersion);
	builder.append(", databaseName=");
	builder.append(databaseName);
	builder.append(", databaseVersion=");
	builder.append(databaseVersion);
	builder.append(", exampleLatency=");
	builder.append(exampleLatency);
	builder.append("]");
	return builder.toString();
    }

}
