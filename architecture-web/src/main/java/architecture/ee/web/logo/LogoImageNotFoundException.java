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
package architecture.ee.web.logo;

import architecture.ee.exception.NotFoundException;

public class LogoImageNotFoundException extends NotFoundException {

	public LogoImageNotFoundException() {
	}

	public LogoImageNotFoundException(int errorCode, String msg, Throwable cause) {
		super(errorCode, msg, cause);
	}

	public LogoImageNotFoundException(int errorCode, String msg) {
		super(errorCode, msg);
	}

	public LogoImageNotFoundException(int errorCode, Throwable cause) {
		super(errorCode, cause);
	}

	public LogoImageNotFoundException(int errorCode) {
		super(errorCode);
	}

	public LogoImageNotFoundException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public LogoImageNotFoundException(String msg) {
		super(msg);
	}

	public LogoImageNotFoundException(Throwable cause) {
		super(cause);
	}

}
