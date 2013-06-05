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
package architecture.common.lifecycle.event;

import architecture.common.lifecycle.State;


/**
 * @author    donghyuck
 */
public class StateChangeEvent extends Event {
   
	private static final long serialVersionUID = -1533891374616205989L;

	/**
	 * @uml.property  name="oldState"
	 * @uml.associationEnd  
	 */
    private State oldState;
    
    /**
	 * @uml.property  name="newState"
	 * @uml.associationEnd  
	 */
    private State newState;;
    
    public StateChangeEvent(Object source, State oldState, State newState)
    {
        super(source);
        this.oldState = oldState;
        this.newState = newState;
    }

    /**
	 * @return
	 * @uml.property  name="newState"
	 */
    public State getNewState()
    {
        return newState;
    }
    /**
	 * @return
	 * @uml.property  name="oldState"
	 */
    public State getOldState()
    {
        return oldState;
    }
    
}
