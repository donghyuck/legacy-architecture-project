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
package architecture.ee.test;

import java.util.List;

import org.junit.Test;
import org.springframework.mock.web.MockServletContext;

import architecture.common.lifecycle.ApplicationHelperFactory;
import architecture.common.lifecycle.State;
import architecture.ee.jdbc.query.SqlQuery;
import architecture.ee.jdbc.query.factory.SqlQueryFactory;
import architecture.ee.spring.lifecycle.AdminService;

public class TestSqlFactory {


	public void log(Object obj){
		System.out.println("# " + obj);
	}
	
	@Test
	public void testBoot() {		
		MockServletContext servletContext = new MockServletContext();
		servletContext.addInitParameter(
			"contextConfigLocation", 
			"default-application-context.xml,databaseSubsystemContext.xml"
		);
						
		AdminService admin = ApplicationHelperFactory.getApplicationHelper().getComponent(AdminService.class);
		if(admin.getState() == State.INITIALIZED){
			admin.setServletContext(servletContext);
			log(admin.getState());
			admin.start();
		}	
	}
	

	private SqlQueryFactory getSqlQueryFactory() {
		return ApplicationHelperFactory.getApplicationHelper().getComponent(SqlQueryFactory.class);
	}

	@Test
	public void testGetSqlQueryFacory() throws Exception {
		SqlQueryFactory factory = (SqlQueryFactory) getSqlQueryFactory();
		
		SqlQuery query = getSqlQueryFactory().createSqlQuery();
		List<String> list = query.reset().queryForList("COMMON.SELECT_TABLE_NAMES", String.class);
		System.out.println(list.size());
		
		
	}

}