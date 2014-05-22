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

import net.sf.ehcache.Cache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.MethodInvoker;

import architecture.common.exception.ComponentNotFoundException;
import architecture.common.user.Company;
import architecture.common.user.SecurityHelper;
import architecture.ee.util.ApplicationHelper;
import architecture.ee.web.site.WebSite;
import architecture.ee.web.site.support.WebSiteAware;
import architecture.ee.web.template.Template;
import freemarker.cache.FileTemplateLoader;
/**
 * 
 * @author donghyuck
 * @todo : 캐쉬적용이 필요.
 */
public class DatabaseTemplateLoader extends FileTemplateLoader {
	
	private Log log = LogFactory.getLog(getClass());
	
	private static final boolean SEP_IS_SLASH = File.separatorChar == '/';
	
	private Cache templateListCache = null ;
	
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
		if(isCustomizedEnabled() )
		{
			try {
				MethodInvoker invoker = new MethodInvoker();		
				invoker.setStaticMethod("architecture.ee.web.struts2.util.ActionUtils.getAction");
				invoker.prepare();
				Object action = invoker.invoke();			
				if( action instanceof WebSiteAware ){					
					WebSite site = ((WebSiteAware)action).getWebSite();
					String nameToUse = SEP_IS_SLASH ? name :  name.replace('/', File.separatorChar) ;				
					
					if(nameToUse.charAt(0) == File.separatorChar ){
						nameToUse = File.separatorChar + "sites"+ File.separatorChar  + site.getName().toLowerCase() + nameToUse ;
					}else{
						nameToUse = File.separatorChar + "sites" + File.separatorChar  + site.getName().toLowerCase() + File.separatorChar + nameToUse ;
					}					
					log.debug("find template:" + nameToUse  );					
					return super.findTemplateSource(nameToUse);
				}			
			} catch (Exception e) {
				log.warn(e);			
			}	
		}		
		return null;		
	
		
		
				
/*		if( usingDatabase() ){
			try {				
				MethodInvoker invoker = new MethodInvoker();		
				invoker.setStaticMethod("architecture.ee.web.struts2.util.ActionUtils.getAction");
				invoker.prepare();
				Object action = invoker.invoke();
				
				if( action instanceof TemplateAware ){
					Template template = ((TemplateAware)action).getTargetTemplate();
					if( log.isDebugEnabled() )
						log.debug( name + " < compare > template from action:" + template.getTitle() + ", type=" + template.getTemplateType() + ", locaion=" + template.getLocation());					
					if(  "ftl".equals(template.getTemplateType()) && name.contains(template.getLocation() ))
						return template;
				}
				
			} catch (Exception e) {
				log.warn(e);				
			}
			List<Template> contents = getCurrentCompanyTemplates();
			for( Template template : contents){
				if( log.isDebugEnabled() )
					log.debug( name + "< compare > template from database :" + template.getTitle() + ", type:" + template.getTemplateType() + ", match:" + template.getLocation() .contains(name) );
				if(  template.getLocation() .contains(name)){
					return template;
				}
			}			
		}	*/	

	
	}
	

	
	
	
	/**
	 * @return templateListCache
	 */
	public Cache getTemplateListCache() {
		return templateListCache;
	}

	/**
	 * @param templateListCache 설정할 templateListCache
	 */
	public void setTemplateListCache(Cache templateCache) {
		this.templateListCache = templateCache;
	}
	
	
	protected boolean isCacheSet(){
		
		return templateListCache == null ? false : true;
	}
	
	protected void initialize(){
		
		try {
			templateListCache = ApplicationHelper.getComponent("templateListCache", Cache.class);
			if(log.isDebugEnabled())
				log.debug("cache setted.");
		} catch (ComponentNotFoundException e) {
			if(log.isDebugEnabled())
				log.debug("no cache exist..");
			templateListCache = null;
		}
		
	}

/*	
	protected List<Template> getCurrentCompanyTemplates(){
		List<Template> list = null;
		Company targetCompany = getCurrentCompany();
		
		if(isCacheSet()){
			net.sf.ehcache.Element item =  templateListCache.get(targetCompany.getCompanyId());
			if( item != null ){
				list = (List<Template>)item.getValue();
			}		
		}
		
		if( list == null ){
			TemplateManager templateManager = ApplicationHelper.getComponent(TemplateManager.class);						
			list = templateManager.getTemplate(targetCompany);			
			if(isCacheSet()){
				templateListCache.put(new net.sf.ehcache.Element( targetCompany.getCompanyId(), list ));
			}
		}
		
		return list ;
	}*/
	


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
