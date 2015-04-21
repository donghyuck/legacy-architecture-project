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
package architecture.ee.exception;

import architecture.common.exception.CodeableRuntimeException;

public class SystemException extends CodeableRuntimeException {

	public SystemException() {
		super();
	}

	public SystemException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public SystemException(String msg) {
		super(msg);
	}

	public SystemException(Throwable cause) {
		super(cause);
	}

}