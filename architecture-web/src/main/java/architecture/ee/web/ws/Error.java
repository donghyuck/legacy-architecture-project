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
package architecture.ee.web.ws;

import architecture.common.exception.Codeable;
import architecture.ee.web.util.WebApplicationHelper;

public class Error implements java.io.Serializable {
	
	private String  locale;
	private int code;
	private String exceptionClassName;
	private String exceptionMessage;
	
	/**
	 * @param locale
	 */
	public Error(Throwable e) {
		locale = WebApplicationHelper.getLocale().toString();
		if( e  instanceof Codeable ){
			code = ((Codeable)	e).getErrorCode();				
			exceptionClassName = e.getMessage();
			exceptionMessage = e.getClass().getName();
		}
		if( e.getCause() != null ){
			Throwable cause = e.getCause();
			if( cause  instanceof Codeable ){
				code = ((Codeable)	cause).getErrorCode();				
				exceptionClassName = cause.getMessage();
				exceptionMessage = cause.getClass().getName();			
				
			}
		}
	}

		
	public Error() {
	}

	/**
	 * @return locale
	 */
	public String getLocale() {
		return locale;
	}

	/**
	 * @param locale 설정할 locale
	 */
	public void setLocale(String locale) {
		this.locale = locale;
	}

	/**
	 * @return code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @param code 설정할 code
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * @return exceptionClassName
	 */
	public String getException() {
		return exceptionClassName;
	}

	/**
	 * @param exceptionClassName 설정할 exceptionClassName
	 */
	public void setException(String exceptionClassName) {
		this.exceptionClassName = exceptionClassName;
	}

	/**
	 * @return exceptionMessage
	 */
	public String getMessage() {
		return exceptionMessage;
	}

	/**
	 * @param exceptionMessage 설정할 exceptionMessage
	 */
	public void setMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

	
}
