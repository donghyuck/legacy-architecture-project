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

import org.junit.Test;

import architecture.common.event.api.EventPublisher;
import architecture.common.lifecycle.ApplicationHelperFactory;
import architecture.common.lifecycle.ApplicationStateChangeEvent;
import architecture.common.lifecycle.StateChangeEvent;

public class TestEvent {
	@Test
	public void test(){
		
		
		
	}
	//@Test
	public void testLoadEventContext() {
		
		EventPublisher eventPublisher = ApplicationHelperFactory.getApplicationHelper().getComponent(EventPublisher.class);
		eventPublisher.register(this);
		eventPublisher.publish(new ApplicationStateChangeEvent(this, architecture.common.lifecycle.State.STARTING, architecture.common.lifecycle.State.STARTED));

	}

	//@EventListener
	public void onEvent(StateChangeEvent event) {
		System.out.println(" catch event :: " + event.getClass().getName());
	}

}
