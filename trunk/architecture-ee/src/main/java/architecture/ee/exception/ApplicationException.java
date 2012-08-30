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

import java.text.DecimalFormat;

import org.apache.commons.lang.exception.NestableException;

import architecture.common.exception.Codeable;

/**
 * @author  donghyuck
 */
public class ApplicationException extends NestableException implements Codeable {

	private static final DecimalFormat decimalformat = new DecimalFormat("0000000");
	
	/**
	 * error code 값이 60000 이상인 경우는 DB 에서 정보를 가져온다.
	 * @uml.property  name="errorCode"
	 */
	private int errorCode = 60000 ;
	
	public ApplicationException() {
		super();
	}

	public ApplicationException(int errorCode) {
		this.errorCode = errorCode;
	}

	public ApplicationException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public ApplicationException(int errorCode, String msg, Throwable cause) {
		
		super(msg, cause);
		this.errorCode = errorCode;
	}
	

	public ApplicationException(String msg) {
		super(msg);
	}

	public ApplicationException(int errorCode, String msg) {
		super(msg);
		this.errorCode = errorCode;
	}
	
	public ApplicationException(Throwable cause) {
		super(cause);
	}

	public ApplicationException(int errorCode, Throwable cause) {
		super(cause);
		this.errorCode = errorCode;
	}
	
	/**
	 * @return
	 * @uml.property  name="errorCode"
	 */
	public int getErrorCode() {
		return errorCode;
	}

	public String getErrorCodeString() {
		return decimalformat.format(errorCode);
	}

}
