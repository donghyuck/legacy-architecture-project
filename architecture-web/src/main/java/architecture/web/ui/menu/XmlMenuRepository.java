/*
 * Copyright 2016 donghyuck
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

package architecture.web.ui.menu;

import java.io.InputStream;

import org.apache.commons.digester3.Digester;

import architecture.web.ui.LoadableResourceException;

public class XmlMenuRepository extends AbstractMenuRepository {

    public XmlMenuRepository() {
	
    }

    public void reload(InputStream input) throws LoadableResourceException {
	Digester digester = initDigester();
	try {
	    digester.parse(input);
	    
	}catch (Exception e){    
	    throw new LoadableResourceException("Error parsing resource nested exception is: " + e.getMessage());
        } finally {
            try {
                input.close();
            } catch (Exception e) {
            }
        }
	
    }

    
    
}
