/*
 * Copyright 2012, 2013 Donghyuck, Son
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
package architecture.user.security.spring.userdetails;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import architecture.common.event.api.EventPublisher;
import architecture.common.event.api.EventSource;

public class ExternalUserDetailsService implements UserDetailsService, EventSource {

	private Log log = LogFactory.getLog( getClass() );
	
	private EventPublisher eventPublisher;
	
	private boolean caseInsensitive;
	
	public ExternalUserDetailsService() {
		caseInsensitive = true;
	}
    /**
	 * @param caseInsensitive
	 */
	public ExternalUserDetailsService(boolean caseInsensitive) {
		this.caseInsensitive = caseInsensitive;
	}
	public void setEventPublisher(EventPublisher eventPublisher)
    {
        this.eventPublisher = eventPublisher;
    }
    
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return null;
	}

}
