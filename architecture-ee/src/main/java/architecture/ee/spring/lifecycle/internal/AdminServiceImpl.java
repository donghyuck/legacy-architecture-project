package architecture.ee.spring.lifecycle.internal;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.ContextLoader;

import architecture.common.event.api.EventListener;
import architecture.common.exception.ComponentNotFoundException;
import architecture.common.lifecycle.ApplicationPropertyChangeEvent;
import architecture.common.lifecycle.Component;
import architecture.common.lifecycle.ComponentImpl;
import architecture.common.lifecycle.ConfigRoot;
import architecture.common.lifecycle.ConfigService;
import architecture.common.lifecycle.Repository;
import architecture.common.lifecycle.State;
import architecture.common.lifecycle.StateChangeEvent;
import architecture.common.lifecycle.Version;
import architecture.ee.bootstrap.Bootstrap;
import architecture.ee.spring.lifecycle.SpringAdminService;

public class AdminServiceImpl extends ComponentImpl implements SpringAdminService {

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
	

	public Repository getRepository() {
		return configService.getRepository();
	}

	public ConfigService getConfigService() {
		return configService;
	}

	
	protected <T> T getBootstrapComponent(Class<T> requiredType){
		return Bootstrap.getBootstrapApplicationContext().getBean(requiredType);
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
	protected void initializeInternal() {
		addStateChangeListener(this);		
	}

	@Override
	protected void startInternal(){		
		
		Thread currentThread = Thread.currentThread();
        ClassLoader oldLoader = currentThread.getContextClassLoader();
        
        if(isSetServletContext() && isSetContextLoader()){
			try{
				this.applicationContext = (ConfigurableApplicationContext) getContextLoader().initWebApplicationContext(getServletContext());				
				this.applicationContext.start();

			}finally{
				if(oldLoader != null)
	               currentThread.setContextClassLoader(oldLoader);
			}
		}
        
	}
	
	@Override
	protected void stopInternal() {
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
			//this.state = event.getNewState();
			Component com = (Component) source;
			if( event.getNewState() == State.STARTED )
			{
				
			}			
			log.debug(
				String.format("[%s] %s > %s", com.getName(), event.getOldState().toString(),  event.getNewState().toString())
			);
		}		
	}
	
	@EventListener
	public void onEvent(ApplicationPropertyChangeEvent event) {

	}	

	public ConfigRoot getConfigRoot() {
		return configService.getConfigRoot();
	}

	public boolean isReady() {
		if( isSetApplicationContext() ){
			return true;
		}
		return false;
	}

}