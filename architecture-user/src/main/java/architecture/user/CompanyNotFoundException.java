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
package architecture.user;

import architecture.ee.exception.ApplicationException;

public class CompanyNotFoundException extends ApplicationException  {

	public CompanyNotFoundException() {
		super();
	}

	public CompanyNotFoundException(int errorCode, String msg, Throwable cause) {
		super(errorCode, msg, cause);
	}

	public CompanyNotFoundException(int errorCode, String msg) {
		super(errorCode, msg);
	}

	public CompanyNotFoundException(int errorCode, Throwable cause) {
		super(errorCode, cause);
	}

	public CompanyNotFoundException(int errorCode) {
		super(errorCode);
	}

	public CompanyNotFoundException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public CompanyNotFoundException(String msg) {
		super(msg);
	}

	public CompanyNotFoundException(Throwable cause) {
		super(cause);
	}

}
