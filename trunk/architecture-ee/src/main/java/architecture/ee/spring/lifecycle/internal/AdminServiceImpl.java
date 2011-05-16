package architecture.ee.spring.lifecycle.internal;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.ContextLoader;

import architecture.common.event.api.EventListener;
import architecture.common.exception.ComponentNotFoundException;
import architecture.common.lifecycle.ApplicationProperties;
import architecture.common.lifecycle.ComponentImpl;
import architecture.common.lifecycle.Server;
import architecture.common.lifecycle.State;
import architecture.common.lifecycle.StateChangeEvent;
import architecture.common.lifecycle.Version;
import architecture.common.lifecycle.internal.EmptyApplicationProperties;
import architecture.ee.spring.lifecycle.AdminService;

public class AdminServiceImpl extends ComponentImpl implements AdminService, Server {

	private ContextLoader contextLoader;
	private ServletContext servletContext;
	private ConfigurableApplicationContext applicationContext;
	private Version version ;
	private ApplicationHomeImpl applicationHome;
	
	public AdminServiceImpl() {
		super();
		this.contextLoader = null;
		this.servletContext = null;
		this.applicationContext = null;
		this.version = new Version(2, 0, 0, Version.ReleaseStatus.Release_Candidate, 1 );
		this.applicationHome = new ApplicationHomeImpl();
	}
	
	public boolean isSetApplicationContext(){
		if(applicationContext != null)
			return true;		
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
		}
	}

	
	@Override
	public void destroy() {
		if(isSetApplicationContext()){
			contextLoader.closeWebApplicationContext(getServletContext());
			this.applicationContext = null;
		}
	}

	public ApplicationProperties getApplicationProperties() {
		return EmptyApplicationProperties.getInstance();
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

	public String getInstallRootPath() {
		return applicationHome.getInstallRootPath();
	}

	public String getHomePath() {
		return applicationHome.getInstallRootPath();
	}
	
	@EventListener
	public void onEvent(StateChangeEvent event) {		
		Object source = event.getSource();
		if( source instanceof AdminService ){			
			
			//log.debug(((AdminService) source).getApplicationContext().getClass().getName());
			
			if(isSetServletContext()){
				
			}
			
			this.state = event.getNewState();			
			if( event.getNewState() == State.STARTED )
			{			
				//log.debug(((AdminService) source).getApplicationContext());
			}
			
			
			
		}		
		log.debug("[Server] " + event.getOldState().toString() + " > " + event.getNewState().toString());
		
	}

}