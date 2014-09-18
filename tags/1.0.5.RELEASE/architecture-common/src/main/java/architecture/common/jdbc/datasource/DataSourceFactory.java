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
package architecture.common.jdbc.datasource;

import javax.sql.DataSource;

import architecture.common.util.ImplFactory;

/**
 * @author   donghyuck
 */
public class DataSourceFactory {

	public static interface Implementation {	
		
		/**
		 * setup 설정 값 에서 제공하는 프로퍼티 값을  사용하여 데이터소스를 생성한다.
		 * 
		 * @param configService
		 * @return
		 */
		public abstract DataSource getDataSource();
		
		public abstract DataSource getDataSource(String name);
	
	}
	
	/**
	 * @uml.property  name="impl"
	 * @uml.associationEnd  
	 */
	private static Implementation impl = null;
    
    static 
    {
        impl = (Implementation)ImplFactory.loadImplFromKey(DataSourceFactory.Implementation.class);
    }
       
    public static DataSource getDataSource(){
    	return impl.getDataSource();
    }

    public static DataSource getDataSource(String name){
    	return impl.getDataSource(name);
    }
}