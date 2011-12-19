/*
 * Copyright 2010, 2011 INKIUM, Inc.
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
package architecture.ee.jdbc.query.factory;

import architecture.ee.jdbc.query.factory.Configuration;
import architecture.ee.jdbc.query.factory.impl.ConfigurationFactoryImpl;

/**
 * @author  donghyuck
 */
public class ConfigurationFactory {

	
	public static interface Implementation {

		public Configuration getConfiguration();

	}

	/**
	 */
	private static Implementation impl = null;

	static {
		impl = new ConfigurationFactoryImpl(); 
	}

	public static Configuration getConfiguration() {
		return impl.getConfiguration();
	}
	
}