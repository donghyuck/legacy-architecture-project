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
   
package architecture.ee.web.util;

import java.util.Locale;

import architecture.common.event.api.EventPublisher;
import architecture.common.exception.ComponentNotFoundException;
import architecture.common.lifecycle.ConfigService;
import architecture.common.lifecycle.State;
import architecture.ee.util.ApplicationHelper;

public final class WebApplicationHelper implements WebApplicatioinConstants
{	
		
	public static final ConfigService getConfigService(){
		return ApplicationHelper.getConfigService();
	}
	
	public static final <T> T getComponent(Class<T> requiredType) throws ComponentNotFoundException{
		return ApplicationHelper.getComponent(requiredType);
	}
	
	public static final <T> T getComponent(String requriedName, Class<T> requiredType) throws ComponentNotFoundException{
		return ApplicationHelper.getComponent(requriedName, requiredType);
	}

	public static final void autowireComponent(Object obj){
		ApplicationHelper.autowireComponent(obj);
	}
	
	public static final State getState(){
		return ApplicationHelper.getState();
	}
	
	public static boolean isReady(){
		return ApplicationHelper.isReady();		
	}
	
	public static String getApplicationProperty(String name, String defaultValue){		
		return ApplicationHelper.getApplicationProperty(name, defaultValue);
	}
	
	public static boolean getApplicationBooleanProperty(String name, boolean defaultValue){
		return ApplicationHelper.getApplicationBooleanProperty(name, defaultValue);
	}
	
	public static int getApplicationIntProperty(String name, int defaultValue){		
		return ApplicationHelper.getApplicationIntProperty(name, defaultValue);
	}
	
	public static Locale getLocale(){
		return ApplicationHelper.getLocale();
	}
	
    public static EventPublisher getEventPublisher(){		
		return ApplicationHelper.getEventPublisher();
	}
}