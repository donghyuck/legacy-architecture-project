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
package architecture.ee.jdbc.sqlquery.mapping;

import java.util.List;

import architecture.common.jdbc.ParameterMapping;
import architecture.common.jdbc.ResultMapping;

/**
 * @author donghyuck
 */
public class BoundSql {

    private String sql;

    private Object parameterObject;

    private List<ParameterMapping> parameterMappings;

    private List<ResultMapping> resultMappings;

    public BoundSql(String sql, List<ParameterMapping> parameterMappings, Object parameterObject,
	    List<ResultMapping> resultMappings) {
	this.sql = sql;
	this.parameterMappings = parameterMappings;
	this.resultMappings = resultMappings;
	this.parameterObject = parameterObject;
    }

    public Object getParameterObject() {
	return parameterObject;
    }

    public List<ParameterMapping> getParameterMappings() {
	return parameterMappings;
    }

    public String getSql() {
	return sql;
    }

    public List<ResultMapping> getResultMappings() {
	return resultMappings;
    }
}
