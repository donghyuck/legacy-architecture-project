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
package architecture.common.user;

import architecture.common.model.UserModel;


public interface User extends UserModel {
	
	enum Status {
		
		none(0),

		approved(1),

		rejected(2),

		validated(3),

		registered(4);
		
		int id ;
		
		public int getId(){
			return id;
		}
		
		private Status(int id) {
			this.id = id;
		}		
		
		public static Status getById(int i){
			
			for( Status status : values()){
			    if(status.getId() == i)
			    	return status;
			}			
			return none;
		}
	}
	
}
