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
package architecture.common.lifecycle.service;


import architecture.common.exception.ComponentNotFoundException;
import architecture.common.lifecycle.Component;
import architecture.common.lifecycle.ConfigService;
import architecture.common.lifecycle.State;
import architecture.common.lifecycle.Version;

/**
 * 
 * @author  <a href="mailto:donghyuck.son@gmail.com">Donghyuck Son </a>
 *
 */
public interface AdminService extends Component {

    public abstract ConfigService getConfigService();
    
	public abstract Version getVersion();
	
	public abstract State getState();
	
	public abstract boolean isReady();
			
	public abstract <T> T getComponent(Class<T> requiredType) throws ComponentNotFoundException;
	
	public abstract void refresh();

}
