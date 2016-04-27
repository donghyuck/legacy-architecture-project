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

package architecture.ee.spring.jdbc.support;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;

import architecture.common.jdbc.incrementer.MaxValueIncrementer;
import architecture.common.spring.jdbc.core.ExtendedJdbcTemplate;
import architecture.ee.jdbc.sqlquery.factory.Configuration;
import architecture.ee.jdbc.sqlquery.mapping.BoundSql;
import architecture.ee.jdbc.sqlquery.mapping.MappedStatement;
import architecture.ee.spring.util.NativeJdbcExtractorUtils;

/**
 * @author donghyuck
 */
public class ExtendedJdbcDaoSupport extends JdbcDaoSupport {

    protected Log log = LogFactory.getLog(getClass());

    private MaxValueIncrementer maxValueIncrementer = null;

    private Configuration configuration = null;

    public ExtendedJdbcDaoSupport() {
	super();
    }

    public ExtendedJdbcDaoSupport(Configuration configuration) {
	super();
	this.configuration = configuration;
    }

    protected boolean isMaxValueIncrementerSupport() {
	if (maxValueIncrementer != null)
	    return true;
	return false;
    }

    protected long getNextId(String name) {
	return maxValueIncrementer.nextLongValue(name);
    }

    public void setMaxValueIncrementer(MaxValueIncrementer sequenceDao) {
	this.maxValueIncrementer = sequenceDao;
    }

    public void setConfiguration(Configuration configuration) {
	this.configuration = configuration;
    }

    public boolean isSetConfiguration() {
	if (configuration == null)
	    return false;
	else
	    return true;
    }

    protected ExtendedJdbcTemplate createJdbcTemplate(DataSource dataSource) {
	return new ExtendedJdbcTemplate(dataSource);
    }

    public ExtendedJdbcTemplate getExtendedJdbcTemplate() {
	return (ExtendedJdbcTemplate) getJdbcTemplate();
    }

    @Override
    protected void initDao() throws Exception {

    }

    protected void initTemplateConfig() {

    }

    public LobHandler getLobHandler() {

	if (getExtendedJdbcTemplate().getNativeJdbcExtractor() == null) {
	    log.debug("Initializing NativeJdbcExtractor");
	    log.debug("Database Type:" + getExtendedJdbcTemplate().getDatabaseType());
	    NativeJdbcExtractor extractor = NativeJdbcExtractorUtils.getNativeJdbcExtractor();
	    getExtendedJdbcTemplate().setNativeJdbcExtractor(extractor);
	    log.debug("NativeJdbcExtractor:" + extractor.getClass().getName());
	}

	return getExtendedJdbcTemplate().getLobHandler();
    }

    public void setLobHandler(LobHandler lobHandler) {
	getExtendedJdbcTemplate().setLobHandler(lobHandler);
    }

    protected <T> RowMapper<T> createRowMapper(String name, Class<T> requiredType ){	
	 return configuration.getMapper(name).createRowMapper(requiredType);
    }
    
    protected BoundSql getBoundSql(String statement) {
	if (isSetConfiguration()) {
	    MappedStatement stmt = configuration.getMappedStatement(statement);
	    return stmt.getBoundSql(null);
	}
	return null;
    }

    protected BoundSql getBoundSql(String statement, Object... params) {
	if (isSetConfiguration()) {
	    MappedStatement stmt = configuration.getMappedStatement(statement);
	    return stmt.getBoundSql(params);
	}
	return null;
    }

    protected BoundSql getBoundSqlWithAdditionalParameter(String statement, Object additionalParameter) {
	if (isSetConfiguration()) {
	    MappedStatement stmt = configuration.getMappedStatement(statement);
	    return stmt.getBoundSql(null, additionalParameter);
	}
	return null;
    }

    protected BoundSql getBoundSqlWithAdditionalParameter(String statement, Object parameters,
	    Object additionalParameter) {
	if (isSetConfiguration()) {
	    MappedStatement stmt = configuration.getMappedStatement(statement);
	    return stmt.getBoundSql(parameters, additionalParameter);
	}
	return null;
    }

}