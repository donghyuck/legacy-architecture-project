/*
 * Copyright 2016 donghyuck
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

package architecture.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import architecture.common.event.api.EventListener;
import architecture.common.event.spring.EventListenerSupport;


public class TestListener extends EventListenerSupport {

    private static Logger log = LoggerFactory.getLogger(EventTest.class);

    @EventListener
    public void handleEvent(LoginEvent event) {
	System.out.println("+++++++++++++++");
	if (event instanceof LoginEvent) {
	    LoginEvent loginEvent = (LoginEvent) event;
	    log.info(loginEvent.getUsername() + " logged in (" + loginEvent.getSessionId() + ")");
	}
    }

}
