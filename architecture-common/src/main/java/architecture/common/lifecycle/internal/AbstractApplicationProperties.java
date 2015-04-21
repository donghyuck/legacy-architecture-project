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
package architecture.common.lifecycle.internal;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.common.lifecycle.ApplicationProperties;
import architecture.common.util.StringUtils;

public abstract class AbstractApplicationProperties implements ApplicationProperties {

	protected Log log = LogFactory.getLog(getClass());
	
	public boolean getBooleanProperty(String propertyKey) {
		return Boolean.valueOf(get(propertyKey)).booleanValue();
	}

	public boolean getBooleanProperty(String propertyKey, boolean defaultValue) {
		String value = get(propertyKey);
		if (value != null)
			return Boolean.valueOf(value).booleanValue();
		else
			return defaultValue;
	}

	public int getIntProperty(String property, int defaultValue) {
		return NumberUtils.toInt(get(property), defaultValue);
	}

	public long getLongProperty(String property, long defaultValue) {
		return NumberUtils.toLong(get(property), defaultValue);
	}
	
	public String getStringProperty(String property, String defaultValue) {
		
		return StringUtils.defaultString(get(property), defaultValue);
	}
}
