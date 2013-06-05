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

import javax.servlet.ServletContext;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.ContextLoader;

import architecture.common.event.api.EventListener;
import architecture.common.exception.ComponentNotFoundException;
import architecture.common.license.License;
import architecture.common.license.LicenseManager;
import architecture.common.lifecycle.Component;
import architecture.common.lifecycle.ConfigService;
import architecture.common.lifecycle.State;
import architecture.common.lifecycle.Version;
import architecture.common.lifecycle.event.ApplicationPropertyChangeEvent;
import architecture.common.lifecycle.event.StateChangeEvent;
import architecture.common.lifecycle.service.AdminService;
import architecture.common.lifecycle.service.PluginService;
import architecture.common.spring.lifecycle.support.SpringLifecycleSupport;
import architecture.ee.component.admin.AdminHelper;
import architecture.ee.spring.lifecycle.SpringAdminService;

/**
 * 
 * @author  <a href="mailto:donghyuck.son@gmail.com">Donghyuck Son </a>
 *
 */
public class AdminServiceImpl extends SpringLifecycleSupport implements SpringAdminService {

	private ContextLoader contextLoader;

	private ServletContext servletContext;

	private ConfigurableApplicationContext applicationContext;
	
	private Version version ;

	private ConfigService configService;
    	
	public AdminServiceImpl(){		
		super();
		setName("AdminService");
		this.contextLoader = null;
		this.configService = null;
		this.servletContext = null;
		this.applicationContext = null;
		this.version = new Version(2, 0, 0, Version.ReleaseStatus.Release_Candidate, 1 );
	}
	
	public ConfigService getConfigService() {
		return configService;
	}

	protected ConfigurableApplicationContext getBootstrapApplicationContext(){
		return architecture.common.lifecycle.bootstrap.Bootstrap.getBootstrapApplicationContext();
	}
	
	protected <T> T getBootstrapComponent(Class<T> requiredType){
		return architecture.common.lifecycle.bootstrap.Bootstrap.getBootstrapComponent(requiredType);
	}		

	public void setConfigService(ConfigService configService){
		this.configService = configService ;
	}
	
	public boolean isSetConfigService(){
		if(configService != null)
			return true;		
		return false; 		
	}
	
	public boolean isSetApplicationContext(){
		if(applicationContext != null){
			return applicationContext.isActive();
		}
		return false; 
	}

	public void setContextLoader(ContextLoader contextLoader) {
		this.contextLoader = contextLoader;
	}

	public ContextLoader getContextLoader() {
		return contextLoader;
	}
    
	public boolean isSetContextLoader (){
		if(contextLoader!=null)
			return true;		
		return false;
	}
	
	public ServletContext getServletContext() {
		return servletContext;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
		if(getRepository().getState() != State.INITIALIZED){
			((RepositoryImpl)getRepository()).setServletContext(servletContext);
		}
	}

	public boolean isSetServletContext(){
		if(servletContext != null)
			return true;
		return false;
	}
		
	@Override
	protected void doInitialize() {
		addStateChangeListener(this);		
	}

	@Override
	protected void doStart(){		
		
		Thread currentThread = Thread.currentThread();
        ClassLoader oldLoader = currentThread.getContextClassLoader();
        
        LicenseManager licenseManager = getBootstrapComponent(LicenseManager.class);
        License license = licenseManager.getLicense();
        
        log.info( 
            license.toString()
        );
        
       // MethodInvoker invoker = new MethodInvoker();
        
        // 플러그인 기능은 평가판 라이선스에서는 제공하지 않는다.
        if( AdminHelper.isSetupComplete() && license.getType() != License.Type.EVALUATION  ){
        	try {
				PluginService pluginService = getBootstrapComponent(PluginService.class);
				pluginService.prepare();
			} catch (Exception e) {
				
			}
        }
        
        // 컨텐스트를 로드합니다
        if(isSetServletContext() && isSetContextLoader()){
			try{				
				this.applicationContext = (ConfigurableApplicationContext) getContextLoader().initWebApplicationContext(getServletContext());
	        	//PluginService pluginService = getBootstrapComponent(PluginService.class);
	        	//pluginService.activate();				
				this.applicationContext.start();
			}finally{
				if(oldLoader != null)
	               currentThread.setContextClassLoader(oldLoader);
			}
		}        
	}
	
	@Override
	protected void doStop() {
		if(isSetApplicationContext()){
			
			this.applicationContext.stop();			
			if( isSetServletContext() ){
				contextLoader.closeWebApplicationContext(getServletContext());
			}else{
				if( applicationContext instanceof org.springframework.context.support.AbstractApplicationContext )
					((org.springframework.context.support.AbstractApplicationContext)applicationContext).close();
			}
		}
	}
	
	@Override
	public void destroy() {
		if(isSetApplicationContext()){
			if( isSetServletContext() ){
				contextLoader.closeWebApplicationContext(getServletContext());
			}else{
				if( applicationContext instanceof org.springframework.context.support.AbstractApplicationContext )
					((org.springframework.context.support.AbstractApplicationContext)applicationContext).close();
			}
		}
	}

	public ConfigurableApplicationContext getApplicationContext() {
		return this.applicationContext;
	}
		
	public void autowireComponent(Object obj) {
		if(isSetApplicationContext()){
			getApplicationContext().getAutowireCapableBeanFactory().autowireBeanProperties(obj, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);			
		}
	}

	public <T> T getComponent(Class<T> requiredType){
		
		if (!isSetApplicationContext()) {
			throw new IllegalStateException("");
		}
		
		if (requiredType == null) {
			throw new ComponentNotFoundException("");
		}	
		
		try {
			return getApplicationContext().getBean(requiredType);
		} catch (NoSuchBeanDefinitionException e){
			throw new ComponentNotFoundException(e);
		}
	}		

	public void refresh() {
		if (!isSetApplicationContext()) {
			throw new IllegalStateException();
		}
		getApplicationContext().refresh();
	}
	
	public Version getVersion() {
		return this.version;
	}

	@EventListener
	public void onEvent(StateChangeEvent event) {		
		Object source = event.getSource();
		if( source instanceof Component ){		
			
			if( source instanceof AdminService ){
				
			}else{
				
			}			
			log.debug(
				String.format("[%s] %s > %s",((Component)source).getName(), event.getOldState().toString(),  event.getNewState().toString())
			);
		}		
	}
		
	@EventListener
	public void onEvent(ApplicationPropertyChangeEvent event) {
		log.debug("property changed " + event.getOldValue() + ">" + event.getNewValue() );
	}	

	public boolean isReady() {
		if( isSetApplicationContext() ){
			return true;
		}
		return false;
	}

}