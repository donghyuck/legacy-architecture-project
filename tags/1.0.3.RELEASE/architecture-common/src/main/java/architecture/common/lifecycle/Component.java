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

import architecture.common.exception.ComponentDisabledException;
import architecture.common.exception.ConfigurationError;
import architecture.common.exception.ConfigurationWarning;
import architecture.common.exception.RuntimeError;
import architecture.common.exception.RuntimeWarning;

public interface Component {
	
	/**
	 * 컴포넌트 이름 또는 아이디을 리턴한다.
	 * @return
	 */
    public abstract String getName();

    /**
     * 컴포넌트의 상태를 리턴한다.
     * @return
     */
    public abstract State getState();
        
    /**
     * 컴포넌트를 초기화 한다.
     * @throws ComponentDisabledException
     * @throws ConfigurationWarning
     * @throws ConfigurationError
     */
    public abstract void initialize() throws ComponentDisabledException, ConfigurationWarning, ConfigurationError;
    
    /**
     * 컴포넌트를 시작한다.
     * 
     * @throws RuntimeError
     * @throws RuntimeWarning
     */
    public abstract void start() throws RuntimeError, RuntimeWarning;
    
    /**
     * 컴포넌트를 종료한다.
     */
    public abstract void stop();
    
    /**
     * 컴포넌트를 파괴한다.
     */
    public abstract void destroy();
    
    public void removeStateChangeListener(Object listener);
    
    public void addStateChangeListener(Object listener);
    
}

