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
package architecture.common.exception;

import org.apache.commons.lang.exception.NestableRuntimeException;

public class RuntimeWarning extends NestableRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3214160707984165576L;

	public RuntimeWarning() {
		super();
	}

	public RuntimeWarning(String msg) {
		super(msg);
	}

	public RuntimeWarning(String msg, Throwable cause) {
		super(msg, cause);
	}

	public RuntimeWarning(Throwable cause) {
		super(cause);
	}

}
