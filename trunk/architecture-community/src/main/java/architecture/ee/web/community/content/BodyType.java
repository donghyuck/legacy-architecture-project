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
package architecture.ee.web.community.content;

public enum BodyType {

	RAW(1), XHTML(2);

	private int id;
	
	private BodyType(int id){
		this.id = id ;
	}

	/* (비Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("BodyType:");
		 switch(id)
	        {
	        case 1: // '\001'
	            builder.append("RAW");
	            break;

	        case 2: // '\002'
	            builder.append("XHTML");
	            break;

	        default:
	            builder.append("id=").append(id);
	            break;
	        }
	        return builder.toString();
	}
    

    
}
