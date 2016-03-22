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

package architecture.ee.jdbc.sqlquery.factory;

import architecture.ee.jdbc.sqlquery.factory.impl.SqlQueryFactoryBuilderImpl;

/**
 * 
 * @author <a href="mailto:donghyuck.son@gmail.com">Donghyuck Son </a>
 *
 */
public class SqlQueryFactoryBuilder {

    public static interface Implementation {

	public SqlQueryFactory getSqlQueryFactory();

	public SqlQueryFactory getSqlQueryFactory(Configuration configuration);
    }

    private static Implementation impl = null;

    static {
	impl = new SqlQueryFactoryBuilderImpl();
    }

    public static SqlQueryFactory getSqlQueryFactory() {
	return impl.getSqlQueryFactory();
    }

    public static SqlQueryFactory getSqlQueryFactory(Configuration configuration) {
	return impl.getSqlQueryFactory(configuration);
    }

}
