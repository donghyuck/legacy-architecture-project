/*
 * $Header: /cvsroot/struts-menu/navigator/src/java/net/sf/navigator/util/PropertyMessageResourcesFactory.java,v 1.1 2004/09/17 10:46:05 mraible Exp $
 * $Revision: 1.1 $
 * $Date: 2004/09/17 10:46:05 $
 *
 * Copyright 1999-2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 

package architecture.ee.web.ui.navigator.util;


/**
 * Factory for <code>PropertyMessageResources</code> instances.  The
 * configuration paramter for such instances is the base Java package
 * name of the resources entries from which our keys and values will be
 * loaded.
 *
 * @version $Revision: 1.1 $ $Date: 2004/09/17 10:46:05 $
 */

public class PropertyMessageResourcesFactory extends MessageResourcesFactory {


    // --------------------------------------------------------- Public Methods


    /**
     * Create and return a newly instansiated <code>MessageResources</code>.
     * This method must be implemented by concrete subclasses.
     *
     * @param config Configuration parameter(s) for the requested bundle
     */
    public MessageResources createResources(String config) {

        return new PropertyMessageResources(this, config, this.returnNull);

    }


}
