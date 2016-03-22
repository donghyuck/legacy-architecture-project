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
package architecture.user.security.simple.authentication.impl;

import architecture.common.user.authentication.AuthToken;
import architecture.common.user.authentication.UnAuthorizedException;
import architecture.ee.util.ApplicationHelper;
import architecture.user.security.simple.authentication.AuthProvider;

public class AuthFactoryImpl implements architecture.user.security.simple.authentication.AuthFactory.Implementation {

    public AuthProvider getAuthProvider() {
	return ApplicationHelper.getComponent(AuthProvider.class);
    }

    public boolean isPlainSupported() {
	return getAuthProvider().isPlainSupported();
    }

    public boolean isDigestSupported() {
	return getAuthProvider().isDigestSupported();
    }

    public AuthToken getAuthToken(String username, String password) throws UnAuthorizedException {
	return getAuthProvider().authenticateAndGetAuthToken(username, password);
    }

}
