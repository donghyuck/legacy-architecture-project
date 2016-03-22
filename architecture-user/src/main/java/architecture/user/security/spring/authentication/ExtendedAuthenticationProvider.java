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
package architecture.user.security.spring.authentication;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import architecture.common.lifecycle.State;
import architecture.common.user.UserManager;
import architecture.common.user.UserTemplate;
import architecture.common.util.L10NUtils;
import architecture.ee.util.ApplicationHelper;
import architecture.user.security.spring.userdetails.ExtendedUserDetails;

public class ExtendedAuthenticationProvider extends DaoAuthenticationProvider {

    private Log log = LogFactory.getLog(getClass());

    protected UserManager userManager;

    public void setUserManager(UserManager userManager) {
	this.userManager = userManager;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
	    UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

	if (authentication.getCredentials() == null)
	    throw new BadCredentialsException(
		    messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));

	// 라이선스 검사.

	super.additionalAuthenticationChecks(userDetails, authentication);
	ExtendedUserDetails user;
	try {
	    user = (ExtendedUserDetails) userDetails;
	} catch (Exception e) {
	    log.error("Unable to coerce user detail to ExtendedUserDetails.");
	    throw new BadCredentialsException(
		    messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
	}
	State state = ApplicationHelper.getState();
	log.debug(state);
	try {
	    if (user.getUser() != null) {
		UserTemplate template = new UserTemplate(user.getUser());
		template.setLastLoggedIn(new Date());
		userManager.updateUser(template);
	    }
	} catch (Exception e) {
	    log.warn(L10NUtils.format("005016", user), e);
	}
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
	try {
	    return super.authenticate(authentication);
	} catch (AuthenticationException e) {
	    if (log.isInfoEnabled())
		log.info(L10NUtils.format("005012", authentication != null ? authentication.getName() : "<unknown>",
			e.getMessage()));
	    if (log.isTraceEnabled())
		log.trace(L10NUtils.getMessage("005013"), e);
	    throw e;
	} catch (RuntimeException e) {
	    log.warn(L10NUtils.format("005014", authentication != null ? authentication.getName() : "<unknown>"), e);
	    throw e;
	}
    }

}
