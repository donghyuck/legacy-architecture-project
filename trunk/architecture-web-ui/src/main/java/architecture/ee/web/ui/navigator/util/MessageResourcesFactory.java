/*
 * $Header: /cvsroot/struts-menu/navigator/src/java/net/sf/navigator/util/MessageResourcesFactory.java,v 1.1 2004/09/17 10:46:05 mraible Exp $
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


import java.io.Serializable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Factory for <code>MessageResources</code> instances.  The general usage
 * pattern for this class is:
 * <ul>
 * <li>Call <code>MessageResourcesFactory().createFactory()</code> to retrieve
 *     a <code>MessageResourcesFactory</code> instance.</li>
 * <li>Set properties as required to configure this factory instance to create
 *     <code>MessageResources</code> instances with desired
 *     characteristics.</li>
 * <li>Call the <code>createResources()</code> method of the factory to
 *     retrieve a newly instantiated <code>MessageResources</code>
 *     instance.</li>
 * </ul>
 *
 * @version $Revision: 1.1 $ $Date: 2004/09/17 10:46:05 $
 */

public abstract class MessageResourcesFactory implements Serializable {


    // ---------------------------------------------------- Instance Properties


    /**
     * The "return null" property value to which newly created
     * MessageResourcess should be initialized.
     */
    protected boolean returnNull = true;

    /**
     * Get default value of the "returnNull" property used to initialize newly created
     * MessageResourcess.
     * @return default value of the "returnNull" property newly created
     * MessageResourcess are initialized to.
     */
    public boolean getReturnNull() {
        return (this.returnNull);
    }

    /**
     * Set the default value of the "returnNull" property newly created
     * MessageResourcess are initialized to.
     * @param  returnNull default value of the "returnNull" MessageResourcess are initialized to.
     */
    public void setReturnNull(boolean returnNull) {
        this.returnNull = returnNull;
    }


    // --------------------------------------------------------- Public Methods


    /**
     * Create and return a newly instansiated <code>MessageResources</code>.
     * This method must be implemented by concrete subclasses.
     *
     * @param config Configuration parameter(s) for the requested bundle
     */
    public abstract MessageResources createResources(String config);


    // ------------------------------------------------------ Static Properties


    /**
     * The Java class to be used for
     * <code>MessageResourcesFactory</code> instances.
     */
    protected static transient Class clazz = null;


    /**
     * Commons Logging instance.
     */
    private static Log LOG = LogFactory.getLog(MessageResourcesFactory.class);


    /**
     * The fully qualified class name to be used for
     * <code>MessageResourcesFactory</code> instances.
     */
    protected static String factoryClass =
        "net.sf.navigator.util.PropertyMessageResourcesFactory";

    /**
     * The fully qualified class name that is used for
     * <code>MessageResourcesFactory</code> instances.
     * @return class name that is used for
     *   <code>MessageResourcesFactory</code> instances
     */
    public static String getFactoryClass() {
        return (MessageResourcesFactory.factoryClass);
    }

    /**
     * Set the fully qualified class name that is used for
     * <code>MessageResourcesFactory</code> instances.
     * @param factoryClass name that is used for
     *   <code>MessageResourcesFactory</code> instances
     */
    public static void setFactoryClass(String factoryClass) {
        MessageResourcesFactory.factoryClass = factoryClass;
        MessageResourcesFactory.clazz = null;
    }


    // --------------------------------------------------------- Static Methods


    /**
     * Create and return a <code>MessageResourcesFactory</code> instance of the
     * appropriate class, which can be used to create customized
     * <code>MessageResources</code> instances.  If no such factory can be
     * created, return <code>null</code> instead.
     */
    public static MessageResourcesFactory createFactory() {

        // Construct a new instance of the specified factory class
        try {
            if (clazz == null)
                clazz = Class.forName(factoryClass);
            MessageResourcesFactory factory =
                (MessageResourcesFactory) clazz.newInstance();
            return (factory);
        } catch (Throwable t) {
            LOG.error("MessageResourcesFactory.createFactory", t);
            return (null);
        }

    }


}
