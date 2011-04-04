/*
 * Copyright 2010, 2011 INKIUM, Inc.
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

import org.apache.commons.lang.exception.NestableException;

public class ApplicationException extends NestableException{

	public ApplicationException() {
		super();
	}

	public ApplicationException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public ApplicationException(String msg) {
		super(msg);
	}

	public ApplicationException(Throwable cause) {
		super(cause);
	}

}
