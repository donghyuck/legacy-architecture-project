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
package architecture.ee.web.community.streams;

import architecture.ee.exception.ApplicationException;

public class PhotoNotFoundException extends ApplicationException {

	/**
	 * 
	 */
	public PhotoNotFoundException() {
		super();
		// TODO 자동 생성된 생성자 스텁
	}

	/**
	 * @param errorCode
	 * @param msg
	 * @param cause
	 */
	public PhotoNotFoundException(int errorCode, String msg, Throwable cause) {
		super(errorCode, msg, cause);
		// TODO 자동 생성된 생성자 스텁
	}

	/**
	 * @param errorCode
	 * @param msg
	 */
	public PhotoNotFoundException(int errorCode, String msg) {
		super(errorCode, msg);
		// TODO 자동 생성된 생성자 스텁
	}

	/**
	 * @param errorCode
	 * @param cause
	 */
	public PhotoNotFoundException(int errorCode, Throwable cause) {
		super(errorCode, cause);
		// TODO 자동 생성된 생성자 스텁
	}

	/**
	 * @param errorCode
	 */
	public PhotoNotFoundException(int errorCode) {
		super(errorCode);
		// TODO 자동 생성된 생성자 스텁
	}

	/**
	 * @param msg
	 * @param cause
	 */
	public PhotoNotFoundException(String msg, Throwable cause) {
		super(msg, cause);
		// TODO 자동 생성된 생성자 스텁
	}

	/**
	 * @param msg
	 */
	public PhotoNotFoundException(String msg) {
		super(msg);
		// TODO 자동 생성된 생성자 스텁
	}

	/**
	 * @param cause
	 */
	public PhotoNotFoundException(Throwable cause) {
		super(cause);
		// TODO 자동 생성된 생성자 스텁
	}

}
