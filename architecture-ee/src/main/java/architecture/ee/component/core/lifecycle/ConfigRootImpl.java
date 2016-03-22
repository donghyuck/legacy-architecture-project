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
package architecture.ee.component.core.lifecycle;

import java.io.File;
import java.io.IOException;

import org.springframework.core.io.Resource;

import architecture.common.lifecycle.ConfigRoot;

/**
 * @author donghyuck
 */
public class ConfigRootImpl implements ConfigRoot {

    private String rootURL;

    private Resource rootFileObject;

    public ConfigRootImpl(Resource fileObject) {
	this.rootFileObject = fileObject;
	try {
	    this.rootURL = rootFileObject.getURL().toString();
	} catch (IOException e) {
	}
    }

    public ConfigRootImpl(String rootURL) {
	this.rootURL = rootURL;
    }

    private Resource getRootResource() {
	return rootFileObject;
    }

    public String getConfigRootPath() {
	return rootURL;
    }

    public File getFile(String name) {
	try {
	    return getRootResource().createRelative(name).getFile();
	} catch (IOException e) {
	}
	return null;
    }

    public String getURI(String name) {
	try {

	    return getRootResource().createRelative(name).getURI().toString();
	} catch (IOException e) {
	}
	return null;
    }

    public String getRootURI() {
	return rootURL;
    }

}