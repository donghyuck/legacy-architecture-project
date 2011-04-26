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
package architecture.ee.jdbc.query.builder.xml.dynamic;

import java.util.Map;

import architecture.ee.jdbc.query.factory.Configuration;
import architecture.ee.jdbc.query.mapping.BoundSql;
import architecture.ee.jdbc.query.sql.SqlSource;

public class DynamicSqlSource implements SqlSource {

	private Configuration configuration;

	private SqlNode rootSqlNode;

	public DynamicSqlSource(Configuration configuration, SqlNode rootSqlNode) {
		this.configuration = configuration;
		this.rootSqlNode = rootSqlNode;
	}

	public BoundSql getBoundSql(Object parameterObject) {
		DynamicContext context = new DynamicContext(parameterObject);
		rootSqlNode.apply(context);
		return new BoundSql(context.getSql());
	}

	public BoundSql getBoundSql(Object parameterObject,
			Map<String, Object> additionalParameters) {
		DynamicContext context = new DynamicContext(parameterObject,
				additionalParameters);
		rootSqlNode.apply(context);
		return new BoundSql(context.getSql());
	}
}
