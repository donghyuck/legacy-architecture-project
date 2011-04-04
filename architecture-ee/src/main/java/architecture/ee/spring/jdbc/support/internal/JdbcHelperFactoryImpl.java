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
package architecture.ee.spring.jdbc.support.internal;
import javax.sql.DataSource;

import org.springframework.jdbc.support.MetaDataAccessException;

import architecture.ee.spring.jdbc.support.JdbcHelper;
import architecture.ee.spring.jdbc.support.JdbcHelperFactory;

public class JdbcHelperFactoryImpl implements JdbcHelperFactory.Implementation {

	public JdbcHelper getJdbcHelper(){
		return new JdbcHelper();
	}
	
	/**
	 * @todo 캐쉬를 사용하는 것을 고려한다. 
	 * @param dataSource
	 * @return
	 */
	public JdbcHelper getJdbcHelper(DataSource dataSource){
		
		JdbcHelper helper = new JdbcHelper();
		try {
			helper.initialize(dataSource);
		} catch (MetaDataAccessException e) {
			e.printStackTrace();
		}		
		return helper;
	}

}
