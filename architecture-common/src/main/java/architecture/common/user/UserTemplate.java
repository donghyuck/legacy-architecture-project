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
package architecture.common.user;

import java.util.Map;

import architecture.common.model.impl.UserModelImpl;
/**
 * 
 * @author donghyuck
 */

public class UserTemplate extends UserModelImpl implements User {

	public UserTemplate() {
		super();
	}

	public UserTemplate(long userId) {
		super(userId);
	}

	public UserTemplate(String userName, String password, String email, String name, boolean emailVisible, boolean nameVisible, Map<String, String> props) {
		super(userName, password, email, name, emailVisible, nameVisible, props);
	}

	public UserTemplate(String userName, String password, String email, String firstName, String lastName, boolean emailVisible, boolean nameVisible, Map<String, String> props) {
		super(userName, password, email, firstName, lastName, emailVisible, nameVisible, props);
	}

	public UserTemplate(String userName, String password, String email, String name) {
		super(userName, password, email, name);
	}

	public UserTemplate(String userName, String password, String email) {
		super(userName, password, email);
	}

	public UserTemplate(String userName) {
		super(userName);
	}

	public UserTemplate(User user) {
		super(user);
	}

}