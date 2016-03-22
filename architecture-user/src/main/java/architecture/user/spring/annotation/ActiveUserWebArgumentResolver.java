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
package architecture.user.spring.annotation;

import java.lang.annotation.Annotation;
import java.security.Principal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

import architecture.common.user.authentication.AnonymousUser;
import architecture.user.security.spring.userdetails.ExtendedUserDetails;

public class ActiveUserWebArgumentResolver implements WebArgumentResolver {

    private Log log = LogFactory.getLog(getClass());

    private static final ExtendedUserDetails EMPTY_EXTENDED_USER_DETAILS = new ExtendedUserDetails(new AnonymousUser());

    public Object resolveArgument(MethodParameter methodParameter, NativeWebRequest webRequest) {
	Annotation[] annotations = methodParameter.getParameterAnnotations();
	if (methodParameter.getParameterType().equals(org.springframework.security.core.userdetails.User.class)
		|| methodParameter.getParameterType()
			.equals(architecture.user.security.spring.userdetails.ExtendedUserDetails.class)) {
	    for (Annotation annotation : annotations) {
		if (ActiveUser.class.isInstance(annotation)) {
		    Principal principal = webRequest.getUserPrincipal();
		    if (principal == null) {
			return EMPTY_EXTENDED_USER_DETAILS;
		    } else {
			return ((Authentication) principal).getPrincipal();
		    }
		}
	    }
	}
	return WebArgumentResolver.UNRESOLVED;
    }
}