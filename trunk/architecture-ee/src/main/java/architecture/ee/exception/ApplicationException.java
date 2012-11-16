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

import architecture.common.exception.CodeableException;
import architecture.common.util.L10NUtils;

/**
 * @author  donghyuck
 */
public class ApplicationException extends CodeableException {
	
	/**
     * 
     */
    private static final long serialVersionUID = -7905504453715998652L;

    public ApplicationException() {
		super();
	}

	public ApplicationException(int errorCode) {
		super();
		setErrorCode( errorCode );
	}

	public ApplicationException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public ApplicationException(int errorCode, String msg, Throwable cause) {
		
		super(msg, cause);
		setErrorCode( errorCode );
	}
	

	public ApplicationException(String msg) {
		super(msg);
	}

	public ApplicationException(int errorCode, String msg) {
		super(msg);
		setErrorCode( errorCode );
	}
	
	public ApplicationException(Throwable cause) {
		super(cause);
	}

	public ApplicationException(int errorCode, Throwable cause) {
		super(cause);
		setErrorCode( errorCode );
	}

	public static ApplicationException createApplicationException(Throwable cause, int code, Object...args){
			if( code < 60000){
				String codeString = L10NUtils.codeToString(code);
				String msg = L10NUtils.format(codeString, args);
				ApplicationException e = new ApplicationException(msg, cause);
				e.setErrorCode(code);
				return e;
			} else {
				ApplicationException e = new ApplicationException(cause);
				e.setErrorCode(code);
				return e;
			}
  }

public static ApplicationException createApplicationException(Throwable cause, int code){
		if( code < 60000){
			String codeString = L10NUtils.codeToString(code);
			String msg = L10NUtils.getMessage(codeString);
			ApplicationException e = new ApplicationException(msg, cause);
			e.setErrorCode(code);
			return e;
		} else {
			ApplicationException e = new ApplicationException(cause);
			e.setErrorCode(code);
			return e;
		}
}

}
