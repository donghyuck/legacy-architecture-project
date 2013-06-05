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


/**
 * 
 * 
 * @author DongHyuck, Son 
 *
 */
public class ComponentNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6833205543454491023L;

	public ComponentNotFoundException() {
		super();
	}

	public ComponentNotFoundException(String msg) {
		super(msg);
	}

	public ComponentNotFoundException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public ComponentNotFoundException(Throwable cause) {
		super(cause);
	}

}