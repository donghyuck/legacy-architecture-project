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
package architecture.user.security.spring.userdetails;

import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.fasterxml.jackson.annotation.JsonIgnore;

import architecture.common.user.authentication.AuthToken;

@SuppressWarnings("serial")
public class ExtendedUserDetails extends User implements AuthToken {

    @JsonIgnore
    private final architecture.common.user.User user;

    public ExtendedUserDetails(architecture.common.user.User user) {
	super(user.getUsername(), user.getPasswordHash(), user.isEnabled(), user.isEnabled(), true, user.isEnabled(),
		Collections.EMPTY_LIST);
	this.user = user;
    }

    public ExtendedUserDetails(architecture.common.user.User user, List<GrantedAuthority> authorities) {
	super(user.getUsername(), user.getPasswordHash(), user.isEnabled(), user.isEnabled(), true, user.isEnabled(),
		authorities);
	this.user = user;
    }

    public boolean isAnonymous() {
	return user.isAnonymous();
    }

    /** =========================================== **/
    /** INTERNAL PROPOSE METHODS **/
    /** =========================================== **/

    public architecture.common.user.User getUser() {
	return user;
    }

    public long getUserId() {
	return user.getUserId();
    }

    public long getCreationDate() {
	return user.getCreationDate() != null ? user.getCreationDate().getTime() : -1L;
    }

}
