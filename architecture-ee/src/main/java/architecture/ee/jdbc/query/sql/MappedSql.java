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
package architecture.ee.jdbc.query.sql;

import architecture.ee.jdbc.query.factory.Configuration;

/**
 * @author  donghyuck
 */
public class MappedSql implements Sql {

	private String resource;

	/**
	 * @uml.property  name="configuration"
	 * @uml.associationEnd  
	 */
	private Configuration configuration;

	private String id;

	private Integer fetchSize;

	private Integer timeout;

	/**
	 * @uml.property  name="sqlType"
	 * @uml.associationEnd  
	 */
	private SqlType sqlType;

	/**
	 * @uml.property  name="source"
	 * @uml.associationEnd  
	 */
	private SqlSource source;

	private MappedSql() {

	}

}
