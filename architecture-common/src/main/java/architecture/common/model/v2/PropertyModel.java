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
package architecture.common.model.v2;

import java.util.Map;

public interface PropertyModel {

	public abstract Map<String, String> getProperties();
	
	public abstract void setProperties(Map<String, String> properties);
	
	public abstract boolean getBooleanProperty(String name, boolean defaultValue );
	
	public abstract long getLongProperty(String name, long defaultValue );
	
	public abstract int getIntProperty(String name, int defaultValue );
	
	public abstract String getProperty(String name, String defaultValue );
	
}
