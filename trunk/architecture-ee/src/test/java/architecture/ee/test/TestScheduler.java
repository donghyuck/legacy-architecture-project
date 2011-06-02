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

import java.util.Locale;

import org.junit.Test;
import org.springframework.mock.web.MockServletContext;

import architecture.common.event.api.EventListener;
import architecture.common.event.api.EventPublisher;
import architecture.common.lifecycle.ApplicationHelperFactory;
import architecture.common.lifecycle.ApplicationStateChangeEvent;
import architecture.common.lifecycle.State;
import architecture.common.lifecycle.StateChangeEvent;
import architecture.ee.component.AdminService;

public class TestScheduler {

	public void log(Object obj){
		System.out.println("# " + obj);
	}
	
	@Test
	public void testGetBootstrapApplicationContext(){		
		
		MockServletContext servletContext = new MockServletContext();
		servletContext.addInitParameter(
			"contextConfigLocation", 
			"default-application-context.xml, databaseSubsystemContext.xml, daoSubsystemContext.xml, schedulingSubsystemContext.xml"
		);
		
		servletContext.addInitParameter("RUNTIME_SERVER_HOME", "C:/TOOLS/workspace/opensource/architecture_v2/architecture-ee/profile/default");
		
		AdminService admin = ApplicationHelperFactory.getApplicationHelper().getComponent(AdminService.class);
		if(admin.getState() == State.INITIALIZED){
			admin.setServletContext(servletContext);
			admin.start();
		}	
		
		log( Locale.KOREA );
	}
	

}
