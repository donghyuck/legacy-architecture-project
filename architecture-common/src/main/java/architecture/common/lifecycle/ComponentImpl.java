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
package architecture.common.lifecycle;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.common.exception.ComponentDisabledException;
import architecture.common.exception.ConfigurationError;
import architecture.common.exception.ConfigurationWarning;
import architecture.common.exception.RuntimeError;
import architecture.common.exception.RuntimeWarning;


public class ComponentImpl implements Component {
    
    protected String name;
    
    protected volatile State state;
    
    private PropertyChangeSupport propertyChangeSupport = null;
    
    protected Log log = LogFactory.getLog(getClass());
    
    public ComponentImpl() {
    	state = State.NONE;
    	setName(getClass().getName());
	}

	public String getName()
    {
        return name;
    }

    protected void setName(String name)
    {
        this.name = name;
    }

    public State getState()
    {
        return state;
    }

    public String getStateString()
    {
        return state.toString();
    }
    
    public void destroy() {
    	setState(State.DESTROYING);
    	doDestroy();
    	setState(State.DESTROYED);
    }

    public void initialize() throws ComponentDisabledException, ConfigurationWarning, ConfigurationError {
    	setState(State.INITIALIZING);
    	doInitialize();
    	setState(State.INITIALIZED);
    }

    public void start() throws RuntimeError, RuntimeWarning {
    	if(state == State.INITIALIZED){
	    	setState(State.STARTING);	    	
	    	doStart();
	    	setState(State.STARTED);
    	}
    }

    public void stop() {  
    	if(state == State.STARTED || state == State.RUNNING ){
	    	setState(State.STOPING); 
	    	doStop();
	    	setState(State.STOPED);
    	}
    }

    protected void setState(State newState) throws RuntimeError, RuntimeWarning, ConfigurationError, ConfigurationWarning
    {	
    	State oldValue = getState();
        state = newState;
        getPropertyChangeSupport().firePropertyChange("state", oldValue, state);
        
    }
    
    protected  void doStart(){}
    
    protected  void doStop(){}
    
    protected  void doDestroy(){}
    
    protected  void doInitialize(){}
    
    protected PropertyChangeSupport getPropertyChangeSupport()
    {
        if(propertyChangeSupport == null)
            propertyChangeSupport = new PropertyChangeSupport(this);
        return propertyChangeSupport;
    }
    
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener propertyChangeListener)
    {
        getPropertyChangeSupport().addPropertyChangeListener(propertyName, propertyChangeListener);
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener propertyChangeListener)
    {
        getPropertyChangeSupport().removePropertyChangeListener(propertyName, propertyChangeListener);
    }

    protected void firePropertyChange(PropertyChangeEvent evt)
    {
        getPropertyChangeSupport().firePropertyChange(evt);
    }
    
}
