package architecture.ee.spring.lifecycle.internal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.servlet.ServletContext;

import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.ContextLoader;

import architecture.common.event.api.EventListener;
import architecture.common.exception.ComponentNotFoundException;
import architecture.common.lifecycle.ApplicationConstants;
import architecture.common.lifecycle.ApplicationProperties;
import architecture.common.lifecycle.ApplicationPropertyChangeEvent;
import architecture.common.lifecycle.ComponentImpl;
import architecture.common.lifecycle.ConfigRoot;
import architecture.common.lifecycle.ConfigRootHelper;
import architecture.common.lifecycle.Server;
import architecture.common.lifecycle.State;
import architecture.common.lifecycle.StateChangeEvent;
import architecture.common.lifecycle.Version;
import architecture.common.lifecycle.internal.EmptyApplicationProperties;
import architecture.common.lifecycle.internal.XmlApplicationProperties;
import architecture.ee.spring.lifecycle.AdminService;

public class AdminServiceImpl extends ComponentImpl implements AdminService, Server {

	private ContextLoader contextLoader;
	private ServletContext servletContext;
	private ConfigurableApplicationContext applicationContext;
	private Version version ;
	private ConfigRootHelper helper;
	
    private ApplicationProperties setupProperties = null;
        
	public AdminServiceImpl(){		
		super();
		setName("AdminService");
		this.contextLoader = null;
		this.helper = null;
		this.servletContext = null;
		this.applicationContext = null;
		this.version = new Version(2, 0, 0, Version.ReleaseStatus.Release_Candidate, 1 );	
	}
	
	public void setConfigRootHelper(ConfigRootHelper configRootHelper){
		this.helper = configRootHelper ;
	}
	
	public boolean isSetConfigRootHelper(){
		if(helper != null)
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
		this.helper.setServletContext(servletContext);
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
        
        log.debug(isSetServletContext() && isSetContextLoader());
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
		
	public ApplicationProperties getApplicationProperties() {
		
		if( setupProperties == null){
			getSetupProperties();
		}
		return setupProperties == null ? EmptyApplicationProperties.getInstance() : setupProperties;
	}
	
	private ApplicationProperties getSetupProperties() {
		
		if( setupProperties == null ){	
			try {
				File file = getConfigRoot().getFile("startup-config.xml"); //.getFile("startup-config.xml");				
				if(!file.exists()){					
					boolean error = false;
				    // create default file...
					log.debug("no file now create !!");
					
					Writer writer = null;
					try {			
					writer = new OutputStreamWriter(new FileOutputStream(file), ApplicationConstants.DEFAULT_CHAR_ENCODING);
					XMLWriter xmlWriter = new XMLWriter(writer, OutputFormat.createPrettyPrint());					
					
					StringBuilder sb = new StringBuilder();
					
					org.dom4j.Document document = org.dom4j.DocumentHelper.createDocument();    
					org.dom4j.Element root = document.addElement( "startup-config" );
					// setup start ------------------------------------------------------------
					org.dom4j.Element setupNode = root.addElement("setup");
					setupNode.addElement("complete").setText("false");
					// setup end --------------------------------------------------------------
					
					xmlWriter.write( document );
					
					}catch(Exception e)
			        {
			            log.error((new StringBuilder()).append("Unable to write to file ").append(file.getName()).append(".tmp").append(": ").append(e.getMessage()).toString());
			            error = true;
			        }
			        finally
			        {
			            try
			            {
			                writer.flush();
			                writer.close();
			            }
			            catch(Exception e)
			            {
			                log.error(e);
			                error = true;
			            }
			        }
				}				
				this.setupProperties = new XmlApplicationProperties(file);				
			} catch (Exception e) {
				log.warn("I warning you!");
				log.debug(e.getMessage(), e);
				return EmptyApplicationProperties.getInstance();
			}
		}
		return setupProperties;
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
		if( source instanceof AdminService ){			
			this.state = event.getNewState();			
			if( event.getNewState() == State.STARTED )
			{			
			}
			log.debug("[Server] " + event.getOldState().toString() + " > " + event.getNewState().toString());
		}		
	}
	
	@EventListener
	public void onEvent(ApplicationPropertyChangeEvent event) {		
		log.debug("[Server] " + event );
	}	

	public ConfigRoot getConfigRoot() {
		return helper.getConfigRoot();
	}

	public String getInstallRootPath() {
		return helper.getInstallRootPath();
	}

	public String getRootURI() {
		return helper.getRootURI();
	}

	public boolean isReady() {
		if( isSetApplicationContext() ){
			return true;
		}
		return false;
	}
	
}