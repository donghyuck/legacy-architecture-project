/**
 * $RCSfile$
 * $Revision: $
 * $Date: $
 *
 * Copyright (C) 2005-2008 Jive Software. All rights reserved.
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

import java.util.Map;

/**
 * Configuration to use when creating caches. Caches can be used when running stand alone or when running in a cluster. When running in a cluster a few extra parameters might be needed. Read   {@link #getParams()}    for more information.
 * @author    Gaston Dombiak
 */
public class CacheInfo {
    /**
	 * Name of the cache
	 * @uml.property  name="cacheName"
	 */
    private String cacheName;
    /**
	 * Type of cache to use when running in a cluster. When not running in a cluster this value is not used.
	 * @uml.property  name="type"
	 * @uml.associationEnd  
	 */
    private Type type;
    /**
	 * Map with the configuration of the cache. Openfire expects the following properties to exist: <ul> <li><b>back-size-high</b> - Maximum size of the cache. Size is in bytes. Zero means that there is no limit.</li> <li><b>back-size-low</b> - Size in byte of the cache after a clean up. Use zero to place no limit.</li> <li><b>back-expiry</b> - minutes, hours or days before content is expired. 10m, 12h or 2d. Zero means never.</li> </ul>
	 * @uml.property  name="params"
	 */
    private Map<String, String> params;

    /**
     * Creates the configuration to use for the specified cache. Caches can be used when running
     * as a standalone application or when running in a cluster. When running in a cluster a few
     * extra configuration are going to be needed. Read {@link #getParams()} for more information.
     *
     * @param cacheName name of the cache.
     * @param type type of cache to use when running in a cluster. Ignored when running as standalone.
     * @param params extra parameters that define cache properties like max size or expiration.
     */
    public CacheInfo(String cacheName, Type type, Map<String, String> params) {
        this.cacheName = cacheName;
        this.type = type;
        this.params = params;
    }

    /**
	 * @return
	 * @uml.property  name="cacheName"
	 */
    public String getCacheName() {
        return cacheName;
    }

    /**
	 * @return
	 * @uml.property  name="type"
	 */
    public Type getType() {
        return type;
    }

    /**
	 * Returns a map with the configuration to use for the cache. When running standalone the following properties are required. <ul> <li><b>back-size-high</b> - Maximum size of the cache. Size is in bytes. Zero means that there is no limit.</li> <li><b>back-expiry</b> - minutes, hours or days before content is expired. 10m, 12h or 2d. Zero means never.</li> </ul> When running in a cluster this extra property is required. More properties can be defined depending on the clustering solution being used. <ul> <li><b>back-size-low</b> - Size in byte of the cache after a clean up. Use zero to place no limit.</li> </ul>
	 * @return    map with the configuration to use for the cache.
	 * @uml.property  name="params"
	 */
    public Map<String, String> getParams() {
        return params;
    }

    /**
	 * @author               donghyuck
	 */
    public static enum Type {
        /**
		 * @uml.property  name="replicated"
		 * @uml.associationEnd  
		 */
        replicated("replicated"),
        /**
		 * @uml.property  name="optimistic"
		 * @uml.associationEnd  
		 */
        optimistic("optimistic"),
        /**
		 * @uml.property  name="distributed"
		 * @uml.associationEnd  
		 */
        distributed("near-distributed");

        /**
		 * @uml.property  name="name"
		 */
        private String name;
        Type(String name) {
            this.name = name;
        }

        public static Type valueof(String name) {
            if ("optimistic".equals(name)) {
                return optimistic;
            }
            return distributed;
        }

        /**
		 * @return
		 * @uml.property  name="name"
		 */
        public String getName() {
            return name;
        }
    }
}
