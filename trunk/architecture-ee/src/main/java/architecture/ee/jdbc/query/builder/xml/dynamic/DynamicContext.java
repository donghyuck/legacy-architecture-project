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

import java.util.HashMap;
import java.util.Map;

/**
 * @author  DongHyuck, Son
 */
public class DynamicContext {

	public static final String ADDITIONAL_PARAMETER_OBJECT_KEY = "_additional_parameter";
	public static final String PARAMETER_OBJECT_KEY = "_parameter";

	/**
	 */
	private final Map<String, Object> bindings;
	private final StringBuilder sqlBuilder = new StringBuilder();

	public DynamicContext(Object parameterObject) {
		this.bindings = new HashMap<String, Object>();
		this.bindings.put(PARAMETER_OBJECT_KEY, parameterObject);
	}

	public DynamicContext(Object parameterObject, Map<String, Object> additionalParameters) {
		this.bindings = new HashMap<String, Object>();
		this.bindings.put(PARAMETER_OBJECT_KEY, parameterObject);
		this.bindings.put(ADDITIONAL_PARAMETER_OBJECT_KEY, additionalParameters);
	}

	/**
	 * @return
	 */
	public Map<String, Object> getBindings() {
		return bindings;
	}

	public void bind(String name, Object value) {
		bindings.put(name, value);
	}

	public void appendSql(String sql) {
		sqlBuilder.append(sql);
		sqlBuilder.append(" ");
	}

	public String getSql() {
		return sqlBuilder.toString().trim();
	}

}