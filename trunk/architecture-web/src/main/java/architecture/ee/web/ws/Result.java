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

public class Result {
	
	private boolean success;
	private Error error;
	
	public Result() {
		error = null;
		success = true;
	}
	/**
	 * @param success
	 */
	public Result(boolean success) {
		super();
		this.success = success;
	}

	/**
	 * @return success
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @param success 설정할 success
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}
	/**
	 * @return error
	 */
	public Error getError() {
		return error;
	}
	/**
	 * @param error 설정할 error
	 */
	public void setError(Error error) {
		this.error = error;
	}

	public static Result newResult(){
		return new Result();
	}
	
	public static Result newResult(Throwable e){
		Result r =new Result(false);
		r.setError(new Error(e));
		return r;
	}
	
}
