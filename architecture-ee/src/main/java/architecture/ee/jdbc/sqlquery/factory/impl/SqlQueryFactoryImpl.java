/*
 * Copyright 2012 Donghyuck, Son
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
package architecture.ee.jdbc.sqlquery.factory.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import architecture.common.jdbc.datasource.DataSourceFactory;
import architecture.common.jdbc.incrementer.MaxValueIncrementer;
import architecture.common.spring.jdbc.core.ExtendedJdbcTemplate;
import architecture.common.util.StringUtils;
import architecture.ee.jdbc.sequencer.SequencerFactory;
import architecture.ee.jdbc.sequencer.incrementer.JdbcMaxValueIncrementer;
import architecture.ee.jdbc.sqlquery.SqlQuery;
import architecture.ee.jdbc.sqlquery.factory.Configuration;
import architecture.ee.jdbc.sqlquery.factory.SqlQueryFactory;

/**
 * 
 * 
 * 
 * @author donghyuck
 */
public class SqlQueryFactoryImpl extends AbstractSqlQueryFactory implements SqlQueryFactory {

    private DataSource dataSource = null;

    private MaxValueIncrementer incrementer = null;

    private boolean incrementerSupported = false;

    public SqlQueryFactoryImpl(Configuration configuration) {
	super(configuration);
    }

    public void setIncrementerSupported(boolean incrementerSupported) {
	this.incrementerSupported = incrementerSupported;
    }

    public void initialize() {
	if (getResourceLocations().size() > 0)
	    loadResourceLocations();
    }

    public void setDataSource(DataSource dataSource) {
	this.dataSource = dataSource;
    }

    public String[] getMappedStatementNames(String namespace) {
	Collection<String> names = getConfiguration().getMappedStatementNames();
	List<String> list = new ArrayList<String>();
	for (String name : names) {
	    if (StringUtils.startsWith(name, namespace))
		list.add(name);
	}
	return list.toArray(new String[list.size()]);
    }

    public String[] getMappedStatementNames() {
	return getConfiguration().getMappedStatementNames()
		.toArray(new String[getConfiguration().getMappedStatementNames().size()]);
    }

    public SqlQuery createSqlQuery() {
	return new SqlQueryImpl(getConfiguration(), dataSource, incrementerSupported ? getMaxValueIncrementer() : null);
    }

    public SqlQuery createSqlQuery(DataSource dataSource) {
	return new SqlQueryImpl(getConfiguration(), dataSource, incrementerSupported ? getMaxValueIncrementer() : null);
    }

    public SqlQuery createSqlQuery(ExtendedJdbcTemplate jdbcTemplate) {
	return new SqlQueryImpl(getConfiguration(), jdbcTemplate,
		incrementerSupported ? getMaxValueIncrementer() : null);
    }

    public MaxValueIncrementer getMaxValueIncrementer() {
	if (this.incrementer == null) {
	    JdbcMaxValueIncrementer incrementerToUse = new JdbcMaxValueIncrementer(createSequencerFactory());
	    incrementerToUse.initialize();
	    this.incrementer = incrementerToUse;
	}
	return this.incrementer;
    }

    private SequencerFactory createSequencerFactory() {
	return new SequencerFactory(getConfiguration(),
		dataSource == null ? DataSourceFactory.getDataSource() : dataSource);
    }

}