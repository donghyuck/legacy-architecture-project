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

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import architecture.common.user.User;
import architecture.common.user.authentication.AnonymousUser;

/**
 * 
 * 
 * @author <a href="mailto:donghyuck.son@gmail.com">Donghyuck Son </a>
 */
public class ExtendedUserDetailsAdaptor implements UserDetails {

    private final User user;

    public ExtendedUserDetailsAdaptor(User user) {
	if (user == null)
	    this.user = new AnonymousUser();
	else
	    this.user = user;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
	return null;
    }

    public String getPassword() {
	return user.getPassword();
    }

    public String getUsername() {
	return user.getUsername();
    }

    public boolean isAccountNonExpired() {
	return user.isEnabled();
    }

    public boolean isAccountNonLocked() {
	return user.isEnabled();
    }

    public boolean isCredentialsNonExpired() {
	return user.isEnabled();
    }

    public boolean isEnabled() {
	return user.isEnabled();
    }

    /** =========================================== **/
    /** INTERNAL PROPOSE METHODS **/
    /** =========================================== **/

    public User getUser() {
	return user;
    }

    public long getUserId() {
	return user.getUserId();
    }

    public long getCreationDate() {
	return user.getCreationDate() != null ? user.getCreationDate().getTime() : -1L;
    }

}
