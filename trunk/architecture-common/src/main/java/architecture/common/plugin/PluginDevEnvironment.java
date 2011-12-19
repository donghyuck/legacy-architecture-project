/**
 * $RCSfile$
 * $Revision: 1485 $
 * $Date: 2005-06-05 18:36:19 -0300 (Sun, 05 Jun 2005) $
 *
 * Copyright (C) 2004-2008 Jive Software. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package architecture.common.plugin;

import java.io.File;

/**
 * Represents the data model used to represent development mode within the Jive Openfire plugin framework.
 * @author    Derek DeMoro
 */
public class PluginDevEnvironment {
    /**
	 * @uml.property  name="webRoot"
	 */
    private File webRoot;
    /**
	 * @uml.property  name="classesDir"
	 */
    private File classesDir;

    /**
	 * Returns the document root of a plugins web development application.
	 * @return    the document root of a plugin.
	 * @uml.property  name="webRoot"
	 */
    public File getWebRoot() {
        return webRoot;
    }

    /**
	 * Set the document root of a plugin.
	 * @param webRoot    the document root of a plugin.
	 * @uml.property  name="webRoot"
	 */
    public void setWebRoot(File webRoot) {
        this.webRoot = webRoot;
    }

    /**
	 * Returns the classes directory of a plugin in development mode.
	 * @return    the classes directory of a plugin in development mode.
	 * @uml.property  name="classesDir"
	 */
    public File getClassesDir() {
        return classesDir;
    }

    /**
	 * Sets the classes directory of a plugin used in development mode.
	 * @param classesDir    the classes directory.
	 * @uml.property  name="classesDir"
	 */
    public void setClassesDir(File classesDir) {
        this.classesDir = classesDir;
    }
}
