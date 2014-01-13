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
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.MethodInvoker;

import architecture.common.user.Company;
import architecture.common.user.SecurityHelper;

import architecture.ee.util.ApplicationHelper;
import architecture.ee.web.community.template.Template;
import architecture.ee.web.community.template.TemplateManager;

import freemarker.cache.FileTemplateLoader;
/**
 * 
 * @author donghyuck
 * @todo : 캐쉬적용이 필요.
 */
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
	public long getLastModified(Object templateSource) {
		if(templateSource instanceof Template){
			return ((Template) templateSource).getModifiedDate().getTime();
		}else{
			return super.getLastModified(templateSource);
		}				
	}

	@Override
	public Reader getReader(Object templateSource, String encoding)
			throws IOException {		
		if(templateSource instanceof Template){
			 return new StringReader( ((Template) templateSource).getBody() );
		}else{
			return super.getReader(templateSource, encoding);
		}				
	}

	@Override
	public void closeTemplateSource(Object templateSource) {
		if(templateSource instanceof Template){
			
		}else{
			super.closeTemplateSource(templateSource);
		}
	}

	@Override
	public Object findTemplateSource(String name) throws IOException {

		if( !isCustomizedEnabled() )
		{
			return null;
		}		
		
		if( usingDatabase()){
			try {				
				MethodInvoker invoker = new MethodInvoker();		
				invoker.setStaticMethod("architecture.ee.web.struts2.util.ActionUtils.getAction");
				invoker.prepare();
				Object action = invoker.invoke();
				if( action instanceof TemplateAware ){
					Template template = ((TemplateAware)action).getTargetTemplate();
					log.debug("##########################################CONTENT:" + template.getTitle() );
					log.debug( name ); 
					log.debug( template.getTitle() );
					log.debug( template.getTemplateType() );
					if(  "ftl".equals(template.getTemplateType()) && name.contains(template.getLocation() ))
						return template;
				}
			} catch (Exception e) {
				log.warn(e);				
			}
			
			log.debug("finding template in database .. : " + name );
			TemplateManager templateManager = ApplicationHelper.getComponent(TemplateManager.class);			
			List<Template> contents = templateManager.getTemplate(getCurrentCompany());
			for( Template template : contents){
				log.debug( name + " - content: " + template.getTitle() + ", type:" + template.getTemplateType() + ", match:" + template.getLocation() .contains(name) );
				if(  template.getLocation() .contains(name)){
					return template;
				}
			}			
		}
		String nameToUse = getCustomizedTemplateFileName(name);
		return super.findTemplateSource(nameToUse);
	}
	
	
	
	protected final String getCustomizedTemplateFileName(String name){
		String nameToUse = SEP_IS_SLASH ? name :  name.replace('/', File.separatorChar) ;		
		if(nameToUse.charAt(0) == File.separatorChar ){
			nameToUse = File.separatorChar + getCurrentCompany().getName() + nameToUse ;
		}else{
			nameToUse = File.separatorChar + getCurrentCompany().getName() + File.separatorChar + nameToUse ;
		}		
		return nameToUse;
	}

	protected final boolean usingDatabase(){		
		return ApplicationHelper.getApplicationBooleanProperty("view.render.freemarker.usingDatabase", false);
	}
	
	protected final boolean isCustomizedEnabled(){		
		return ApplicationHelper.getApplicationBooleanProperty("view.html.customize.enabled", false);
	}
	
	protected final Company getCurrentCompany(){
		return SecurityHelper.getUser().getCompany();		
	}

}
