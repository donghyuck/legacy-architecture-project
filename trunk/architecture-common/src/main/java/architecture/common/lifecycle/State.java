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

/**
 * @author                 donghyuck
 */
public enum State {
	
	/**
	 * @uml.property  name="nONE"
	 * @uml.associationEnd  
	 */
	NONE("NONE"),	
	/**
	 * @uml.property  name="iNITIALIZING"
	 * @uml.associationEnd  
	 */
	INITIALIZING("INITIALIZING"),
	/**
	 * @uml.property  name="iNITIALIZED"
	 * @uml.associationEnd  
	 */
	INITIALIZED("INITIALIZED"),	
	/**
	 * @uml.property  name="sTARTING"
	 * @uml.associationEnd  
	 */
	STARTING("STARTING"),
	/**
	 * @uml.property  name="sTARTED"
	 * @uml.associationEnd  
	 */
	STARTED("STARTED"),	
	/**
	 * @uml.property  name="sTOPING"
	 * @uml.associationEnd  
	 */
	STOPING("STOPING"),
	/**
	 * @uml.property  name="rUNNING"
	 * @uml.associationEnd  
	 */
	RUNNING("RUNNING"),
	/**
	 * @uml.property  name="sTOPED"
	 * @uml.associationEnd  
	 */
	STOPED("STOPED"),	
	/**
	 * @uml.property  name="dESTROYING"
	 * @uml.associationEnd  
	 */
	DESTROYING("DESTROYING"),
	/**
	 * @uml.property  name="dESTROYED"
	 * @uml.associationEnd  
	 */
	DESTROYED("DESTROYED"),
	
	/**
	 * @uml.property  name="pOST_UPGRADE_STARTED"
	 * @uml.associationEnd  
	 */
	POST_UPGRADE_STARTED("POST_UPGRADE_STARTED");
	
	private String desc;
	
	private State(String desc)
    {
        this.desc = desc;
    }
	
    public String toString()
    {
        return desc;
    } 
    
}

