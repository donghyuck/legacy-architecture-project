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
package architecture.common.adaptor;

/**
 * @author  donghyuck
 */
public interface Context {
	
	/**
	 * @uml.property  name="tYPE"
	 */
	public static final String TYPE = "type";
	
	public static final String DATA = "data";
	
	public static final String BATCH = "batch";
	
	public static final String QUERY_NAME = "queryName";
	
	public static final String QUERY_STRING = "queryString";
	
	/**
	 * @author   donghyuck
	 */
	public enum Type {
		/**
		 * @uml.property  name="iNPUT"
		 * @uml.associationEnd  
		 */
		INPUT, 
		/**
		 * @uml.property  name="oUTPUT"
		 * @uml.associationEnd  
		 */
		OUTPUT;
	};
	
	
	public String getConnectorName();
	
	public <T> T getObject(String name, Class<T> requiredType);
	
	public Object getObject(String name);
	
	public void setObject(String key, Object obj);
	
	public int size();

	/**
	 * @return
	 * @uml.property  name="tYPE"
	 */
	public Type getType();
	
}
