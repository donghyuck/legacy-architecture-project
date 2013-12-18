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
package architecture.ee.web.community;

import architecture.ee.exception.NotFoundException;

public class CommentNotFoundException extends NotFoundException {

	/**
	 * 
	 */
	public CommentNotFoundException() {
		super();
	}

	/**
	 * @param errorCode
	 * @param msg
	 * @param cause
	 */
	public CommentNotFoundException(int errorCode, String msg, Throwable cause) {
		super(errorCode, msg, cause);
		// TODO 자동 생성된 생성자 스텁
	}

	/**
	 * @param errorCode
	 * @param msg
	 */
	public CommentNotFoundException(int errorCode, String msg) {
		super(errorCode, msg);
	}

	/**
	 * @param errorCode
	 * @param cause
	 */
	public CommentNotFoundException(int errorCode, Throwable cause) {
		super(errorCode, cause);
	}

	/**
	 * @param errorCode
	 */
	public CommentNotFoundException(int errorCode) {
		super(errorCode);
	}

	/**
	 * @param msg
	 * @param cause
	 */
	public CommentNotFoundException(String msg, Throwable cause) {
		super(msg, cause);
	}

	/**
	 * @param msg
	 */
	public CommentNotFoundException(String msg) {
		super(msg);
	}

	/**
	 * @param cause
	 */
	public CommentNotFoundException(Throwable cause) {
		super(cause);
	}


}
