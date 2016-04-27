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
package architecture.ee.jdbc.sqlquery.builder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.common.util.StringUtils;
import architecture.ee.jdbc.sqlquery.factory.Configuration;
import architecture.ee.jdbc.sqlquery.mapping.MapperSource;
import architecture.ee.jdbc.sqlquery.mapping.MappedStatement;
import architecture.ee.jdbc.sqlquery.mapping.StatementType;
import architecture.ee.jdbc.sqlquery.sql.SqlSource;

/**
 * MappedStatement 객체 생성을 돕는 클래스.
 * 
 * @author DongHyuck, Son
 */
public class SqlBuilderAssistant extends AbstractBuilder {

    private String currentNamespace;
    
    private String resource;
    
    private Log log = LogFactory.getLog(SqlBuilderAssistant.class);

    public SqlBuilderAssistant(Configuration configuration, String resource) {
	super(configuration);
	this.resource = resource;
    }

    /**
     * 
     * @param id
     * @param sqlSource
     * @param fetchSize
     * @param timeout
     * @return
     */
    public MappedStatement addMappedStatement(String id, String description, SqlSource sqlSource, StatementType statementType, Integer fetchSize, Integer timeout) {
	String idToUse = applyCurrentNamespace(id);
	MappedStatement.Builder statementBuilder = new MappedStatement.Builder(configuration, idToUse, sqlSource,statementType);
	statementBuilder.resource(resource);
	statementBuilder.fetchSize(fetchSize);
	statementBuilder.description(description);
	setStatementTimeout(timeout, statementBuilder);
	MappedStatement statement = statementBuilder.build();
	configuration.addMappedStatement(statement);
	return statement;
    }

    public MapperSource addMapperSource(String id, MapperSource  mapperSource){
	String idToUse = applyCurrentNamespace(id);
	mapperSource.setID(idToUse);
	configuration.addMapper(mapperSource);
	return mapperSource;
    }
    
    public String getCurrentNamespace() {
	return currentNamespace;
    }

    public void setCurrentNamespace(String currentNamespace) {
	if (currentNamespace != null) {
	    this.currentNamespace = currentNamespace;
	    if (StringUtils.isNotEmpty(resource))
		configuration.addUriNamespace(resource, currentNamespace);
	}
	if (this.currentNamespace == null) {
	    throw new BuilderException("The mapper element requires a namespace attribute to be specified.");
	}
    }

    public String applyCurrentNamespace(String base) {
	if (base == null)
	    return null;
	if (base.contains("."))
	    return base;
	return currentNamespace + "." + base;
    }

    private void setStatementTimeout(Integer timeout, MappedStatement.Builder statementBuilder) {
	if (timeout == null) {
	    timeout = configuration.getDefaultStatementTimeout();
	}
	statementBuilder.timeout(timeout);
    }
}