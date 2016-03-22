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
package architecture.common.user;

import architecture.common.user.authentication.AuthToken;
import architecture.common.util.ImplFactory;

public class SecurityHelper {

    public static interface Implementation {

	public void checkUserStatus(String username, User.Status status);

	public AuthToken getAuthToken();

	public User getUser();

	public boolean isUserInRole(String role);

    }

    private static Implementation impl = null;

    static {
	impl = (Implementation) ImplFactory.loadImplFromKey(SecurityHelper.Implementation.class);
    }

    public static void checkUserStatus(String username, User.Status status) {
	impl.checkUserStatus(username, status);
    }

    public static AuthToken getAuthToken() {
	return impl.getAuthToken();
    }

    public static User getUser() {
	return impl.getUser();
    }

    public static boolean isUserInRole(String role) {
	return impl.isUserInRole(role);
    }
}
