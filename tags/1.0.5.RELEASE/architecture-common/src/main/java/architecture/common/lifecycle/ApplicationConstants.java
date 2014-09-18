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
package architecture.common.lifecycle;

public interface ApplicationConstants {
	
	public static final String DEFAULT_ARCHITECTURE_RUNTIME_INIT_FILENAME = "architecture-runtime-init.properties";
	
	public static final String DEFAULT_STARTUP_FILENAME = "startup-config.xml";

    public static final String ARCHITECTURE_PROFILE_ROOT_KEY = "architecture.profile.root";

    public static final String ARCHITECTURE_CONFIG_ROOT_KEY = "architecture.config.root";
    
    public static final String ARCHITECTURE_PROFILE_ROOT_ENV_KEY = "ARCHITECTURE_PROFILE_ROOT";
   
    public static final String ARCHITECTURE_CONFIG_ROOT_ENV_KEY = "ARCHITECTURE_CONFIG_ROOT";
    	
	public static final String EOL = System.getProperty("line.separator");
	
    public static final String DEFAULT_CHAR_ENCODING = "UTF-8";	
        
    
    public static final long SECOND = 1000L;
    
    public static final long MINUTE = 60000L;
    
    public static final long HOUR = 0x36ee80L;
    
    public static final long DAY = 0x5265c00L;
    
    public static final long WEEK = 0x240c8400L;
    
}
