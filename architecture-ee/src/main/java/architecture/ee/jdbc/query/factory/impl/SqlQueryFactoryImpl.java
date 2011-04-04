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
package architecture.ee.jdbc.query.factory.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;

import architecture.ee.jdbc.query.SqlQuery;
import architecture.ee.jdbc.query.factory.Configuration;
import architecture.ee.jdbc.query.factory.SqlQueryFactory;
import architecture.ee.spring.jdbc.support.ExtendedJdbcTemplate;

public class SqlQueryFactoryImpl implements SqlQueryFactory {

	private final Configuration configuration;
    private DataSource defaultDataSource = null;

    public SqlQueryFactoryImpl(Configuration configuration) {
		this.configuration = configuration;
	}
	
	public Configuration getConfiguration() {
		return configuration;
	}

	public void setDefaultDataSource(DataSource defaultDataSource) {
		this.defaultDataSource = defaultDataSource;
	}

	public String[] getQueryNames(String namespace) {		
		Collection<String> names = configuration.getMappedStatementNames();
		List<String> list = new ArrayList<String>();
		for(String name : names){
			if(StringUtils.startsWith(name, namespace))
				list.add(name);
		}
		return list.toArray(
		    new String[list.size()]
		);
	}
	
	public String[] getQueryNames() {
		return configuration.getMappedStatementNames().toArray(
		    new String[configuration.getMappedStatementNames().size()]
		);
	}
		
	public SqlQuery getSqlQuery() {
		return new SqlQueryImpl(configuration, defaultDataSource);
	}
	
	public SqlQuery getSqlQuery(DataSource dataSource) {
		return new SqlQueryImpl(configuration, dataSource);
	}

	public SqlQuery getSqlQuery(ExtendedJdbcTemplate jdbcTemplate) {
		return new SqlQueryImpl(configuration, jdbcTemplate);
	}
	
}
