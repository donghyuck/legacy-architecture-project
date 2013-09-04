/*
 * Copyright 2012, 2013 Donghyuck, Son
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
package architecture.ee.web.view.freemarker;

import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.common.user.SecurityHelper;
import architecture.ee.util.ApplicationHelper;
import freemarker.cache.FileTemplateLoader;

public class DatabaseTemplateLoader extends FileTemplateLoader {
	
	private Log log = LogFactory.getLog(getClass());
	
	private static final boolean SEP_IS_SLASH = File.separatorChar == '/';
	
	public DatabaseTemplateLoader() throws IOException {
		super();
	}

	public DatabaseTemplateLoader(File baseDir, boolean allowLinking) throws IOException {
		super(baseDir, allowLinking);
	}

	public DatabaseTemplateLoader(File baseDir) throws IOException {
		super(baseDir);
	}

	@Override
	public Object findTemplateSource(String name) throws IOException {
		
		boolean customized = isCustomizedEnabled();
		if( !customized )
		{
			return null;
		}
		
		String nameToUse = SEP_IS_SLASH ? name :  name.replace('/', File.separatorChar) ;
		if(nameToUse.charAt(0) == File.separatorChar ){
			nameToUse = File.separatorChar + SecurityHelper.getUser().getCompany().getName() + nameToUse ;
		}else{
			nameToUse = File.separatorChar + SecurityHelper.getUser().getCompany().getName() + File.separatorChar + nameToUse ;
		}
		log.debug( name + ">" + nameToUse );
		return super.findTemplateSource(nameToUse);
	}
	
	protected final boolean isCustomizedEnabled(){		
		return ApplicationHelper.getApplicationBooleanProperty("view.html.customize.enabled", false);
	}
}
