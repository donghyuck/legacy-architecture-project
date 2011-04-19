/*
 * Copyright 2010, 2011 INKIUM, Inc.
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
package architecture.ee.spring.context.internal;

import java.util.ArrayList;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;

import architecture.common.exception.ComponentNotFoundException;
import architecture.common.lifecycle.ApplicationHelper;

public class ApplicatioinHelperImpl implements ApplicationHelper {

	private ConfigurableApplicationContext applicationContext;
	
	protected ApplicatioinHelperImpl(ConfigurableApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public void autowireComponent(Object obj) {
		if(applicationContext != null)
			applicationContext.getAutowireCapableBeanFactory().autowireBeanProperties(obj, 1, false);		
	}

	public Object createComponent(Class clazz) {
		if(applicationContext != null)			
		    return applicationContext.getAutowireCapableBeanFactory().autowire(clazz, 1, false);		
		throw new IllegalStateException("");
	}

	public Object getComponent(Object obj) throws ComponentNotFoundException {
		 if(applicationContext == null)
	        {
			 throw new IllegalStateException("");
	        }	        
	        if(obj == null)
	        {
    			throw new ComponentNotFoundException("");    		
	        }	        
	        if(obj instanceof Class)
	        {       	
	        	String names[] = applicationContext.getBeanNamesForType((Class)obj);        	
	        	if(names == null || names.length == 0 ){
	        		throw new ComponentNotFoundException("");    	      
	        	}else if (names.length > 1 ){
	        		return new ArrayList(applicationContext.getBeansOfType((Class)obj).values());
	        	}
	        	obj = names[0];
	        } 
	        try
	        {        	
	        	return applicationContext.getBean(obj.toString());
	        }
	        catch(BeansException e)
	        {	
	            throw new ComponentNotFoundException("", e);
	        }
	}

	public Object getInstance(Object obj) {
		if(applicationContext == null)
        {
			throw new IllegalStateException();
        }
        
		if(obj instanceof String){
			try {				
				Class clazz = applicationContext.getClassLoader().loadClass((String)obj);
				return createComponent(clazz);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}					
		}
		return null;
	}

	public void refresh() {			
        
		if(applicationContext == null)
        {
			throw new IllegalStateException();
        }		
		if( applicationContext.getClass().isAssignableFrom(ConfigurableApplicationContext.class) )
			((ConfigurableApplicationContext)applicationContext).refresh();
	}
	
	
	protected ConfigurableListableBeanFactory getBeanFactory() {	
		if(applicationContext == null)
        {
			throw new IllegalStateException("");
        }
		return applicationContext.getBeanFactory();
	}	
	
	protected void addComponent(String name, Class clazz) {
		GenericBeanDefinition bd = new GenericBeanDefinition();
		bd.setBeanClass(clazz);
		getBeanFactory().registerSingleton(name, bd);
	}
	
}
