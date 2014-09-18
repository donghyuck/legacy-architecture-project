/*
 * Copyright 2010, 2011 INKIUM, Inc.
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
package architecture.ee.jdbc.sqlquery.factory;

import java.io.File;

import javax.sql.DataSource;

import architecture.common.jdbc.incrementer.MaxValueIncrementer;
import architecture.common.spring.jdbc.core.ExtendedJdbcTemplate;
import architecture.ee.jdbc.sqlquery.SqlQuery;

public interface SqlQueryFactory {

	public abstract SqlQuery createSqlQuery();
	
	public abstract SqlQuery createSqlQuery(DataSource dataSource);

	public abstract SqlQuery createSqlQuery(ExtendedJdbcTemplate jdbcTemplate);
		
	public abstract MaxValueIncrementer getMaxValueIncrementer();
	
	public boolean validateFile(File file);
	
	public boolean isFileDeployed(File file);
	
	public String fileCreated(File file);
	
}
