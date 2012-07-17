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
package architecture.ee.jdbc.util;

import javax.sql.DataSource;

import architecture.common.util.ImplFactory;

/**
 * @author   donghyuck
 */
public class JdbcHelperFactory {

	public static interface Implementation {

		public JdbcHelper getJdbcHelper(DataSource dataSource);

		public JdbcHelper getJdbcHelper();

	}

	private static Implementation impl = null;

	static {
		impl = (Implementation) ImplFactory.loadImplFromKey(Implementation.class);
	}

	public static JdbcHelper getJdbcHelper() {
		return impl.getJdbcHelper();
	}

	/**
	 * @todo 캐쉬를 사용하는 것을 고려한다.
	 * @param dataSource
	 * @return
	 */
	public static JdbcHelper getJdbcHelper(DataSource dataSource) {
		return impl.getJdbcHelper(dataSource);
	}
}
