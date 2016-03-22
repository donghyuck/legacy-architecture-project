package architecture.ee.spring.event;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import architecture.common.event.api.EventListener;
import architecture.common.event.api.EventPublisher;
import architecture.common.event.api.EventSource;

/**
 * @author donghyuck
 */
public class AutoListenerEventRegister implements BeanFactoryPostProcessor {

    private Log logger = LogFactory.getLog(getClass());
    /**
     * @uml.property name="eventPublisher"
     * @uml.associationEnd
     */
    private EventPublisher eventPublisher;

    public AutoListenerEventRegister(EventPublisher eventPublisher) {
	this.eventPublisher = eventPublisher;
    }

    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
	processEventSources(beanFactory);
	// processListeners(beanFactory);
    }

    private void processEventSources(ConfigurableListableBeanFactory beanFactory) {
	Map<String, EventSource> sources = beanFactory.getBeansOfType(EventSource.class);
	for (Entry<String, EventSource> entry : sources.entrySet()) {
	    EventSource eventSource = entry.getValue();
	    String eventSourceID = entry.getKey();
	    logger.info((new StringBuilder()).append("Registering bean \"").append(eventSourceID)
		    .append("\" as an EventSource").toString());
	    eventSource.setEventPublisher(eventPublisher);
	}
    }

    private void processListeners(ConfigurableListableBeanFactory beanFactory) {
	for (String name : beanFactory.getBeanDefinitionNames()) {
	    Object obj = beanFactory.getBean(name).getClass();
	    for (Method m : obj.getClass().getMethods()) {
		// logger.debug( m.getName() );
		if (m.getAnnotation(EventListener.class) != null) {
		    // logger.debug( m.getName() + "[true]");
		    eventPublisher.register(obj);
		    break;
		} else {
		    // logger.debug( m.getName() + "[false]");
		}
	    }
	}
    }
}
