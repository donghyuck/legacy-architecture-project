package architecture.ee.spring.context;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;

import architecture.common.exception.ComponentNotFoundException;
import architecture.common.lifecycle.Repository;
import architecture.common.lifecycle.event.StateChangeEvent;
import architecture.ee.services.ApplicationHelper;

public class DefaultApplicationContextHelper implements ApplicationContextAware, ApplicationHelper {

	private Log log = LogFactory.getLog(getClass());
	
	private ApplicationContext applicationContext;

	public DefaultApplicationContextHelper() {
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;		
		
		for( String name : applicationContext.getBeanDefinitionNames() ){
			log.debug("component : " + name);
		}
		
	}

	public ConfigurableApplicationContext getConfigurableApplicationContext(){
		if(isConfigurableApplicationContext()){
			return (ConfigurableApplicationContext) applicationContext;
		}
		throw new IllegalStateException("");
	}
	
	protected boolean isConfigurableApplicationContext(){
		return ( applicationContext != null && applicationContext instanceof ConfigurableApplicationContext );
	}
	
	protected ConfigurableListableBeanFactory getBeanFactory() {
		if (!isConfigurableApplicationContext()) {
			throw new IllegalStateException("");
		}		
		return((ConfigurableApplicationContext) applicationContext).getBeanFactory();
	}

	
	protected void addComponent(String name, Class clazz) {
		GenericBeanDefinition bd = new GenericBeanDefinition();
		bd.setBeanClass(clazz);
		getBeanFactory().registerSingleton(name, bd);
	}
	
 
	public Repository getRepository() {		
		return applicationContext.getBean(Repository.class);
	}

	public boolean isReady() {
		if(isConfigurableApplicationContext()){
			return getConfigurableApplicationContext().isActive();
		}
		return false;
	}
	
	public void autowireComponent(Object obj) {
		if (applicationContext == null) {
			throw new IllegalStateException("");
		}
		applicationContext.getAutowireCapableBeanFactory().autowireBeanProperties(obj, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);
	}
 
	public <T> T createComponent(Class<T> requiredType) {
		if (!isConfigurableApplicationContext()) {
			throw new IllegalStateException("");
		}		
		
		return (T)getConfigurableApplicationContext().getAutowireCapableBeanFactory().createBean(requiredType, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);
	}

	public <T> T getComponent(Class<T> requiredType){
		if (requiredType == null) {
			throw new ComponentNotFoundException("");
		}	
		if (applicationContext == null){
			throw new IllegalStateException("");
		}		
		try {
			return applicationContext.getBean(requiredType);
		} catch (NoSuchBeanDefinitionException e){
			throw new ComponentNotFoundException(e);
		}
	}		
	
	public <T> T getComponent(String requiredName, Class<T> requiredType){
		if (requiredType == null) {
			throw new ComponentNotFoundException("");
		}	
		if (applicationContext == null){
			throw new IllegalStateException("");
		}
		try {
			return applicationContext.getBean(requiredName, requiredType);
		} catch (NoSuchBeanDefinitionException e){
			throw new ComponentNotFoundException(e);
		}
	}	
	 
	public Object getComponent(Object obj) throws ComponentNotFoundException {

		if (obj == null) {
			throw new ComponentNotFoundException("");
		}
		
		if (applicationContext == null) {
			throw new IllegalStateException("");
		}
		if (obj instanceof Class) {
			Class requiredType = (Class)obj;
			String names[] = applicationContext.getBeanNamesForType(requiredType);
			
			if (names == null || names.length == 0) {
				throw new ComponentNotFoundException("");
			} else if (names.length > 1) {
				return new ArrayList(applicationContext.getBeansOfType(requiredType).values());
			}
			obj = names[0];
		}
		
		try {
			return applicationContext.getBean(obj.toString());
		} catch (BeansException e) {
			throw new ComponentNotFoundException("", e);
		}
		
	}
	 
	public Object getInstance(Object obj) {
		if (applicationContext == null) {
			throw new IllegalStateException();
		}
		if (obj instanceof String) {
			try {
				Class clazz = applicationContext.getClassLoader().loadClass((String) obj);
				return createComponent(clazz);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public void refresh() {
		if (getConfigurableApplicationContext() == null) {
			throw new IllegalStateException();
		}
		getConfigurableApplicationContext().refresh();
	}	
	
	public void onEvent(StateChangeEvent event) {		
		log.debug("[Server] " + event.getOldState().toString() + " > " + event.getNewState().toString());
	}

	public void onApplicationEvent(ApplicationEvent event) {
		log.debug("###" + event);
	}	
	
}
